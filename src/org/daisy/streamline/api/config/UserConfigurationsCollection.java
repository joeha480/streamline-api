package org.daisy.streamline.api.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * <p>Provides a user configuration collection that lets a user add and
 * remove configurations at runtime. This class is not intended for direct
 * use. The intended use is as part of a {@link UserConfigurationsProvider}
 * implementation. By implementing the {@link UserConfigurationsProvider}, 
 * the configurations will be available through the {@link ConfigurationsCatalogService}
 * API.</p>
 * 
 * <p>This class has all the methods of the 
 * {@link UserConfigurationsProvider} interface but doesn't
 * <code>implements</code> it. This is intentional. For example, this class lacks
 * a constructor without arguments, which is required for service discovery using
 * both SPI and OSGi. This class has also been made final to prohibit a 
 * {@link UserConfigurationsProvider} implementation from extending it.</p>
 * 
 * <p>Note: this class is not connected directly to the {@link ConfigurationsCatalog}
 * because of restrictions imposed by the API design guidelines. To solve the problem,
 * the {@link ExclusiveAccess} interface was created (an implementation of this
 * interface would have been too general to be a public part of this API but to useful
 * to keep private).</p>
 */
public final class UserConfigurationsCollection {
	private static final Logger logger = Logger.getLogger(UserConfigurationsCollection.class.getCanonicalName());
	private static final String MASTER_FILE_NAME = "catalog.ser";
	private static final String CONFIG_EXT = ".ser";
	private final File baseDir;
	private final ExclusiveAccess lock;
	private final File catalog;

	private Inventory inventory;
	private Date sync;

	/**
	 * Creates a new configurations collection. 
	 * @param baseDir the folder to store the configurations
	 * @param lock an exclusive lock, see this interface for more information
	 * @throws NullPointerException if <code>baseDir</code> is null
	 */
	public UserConfigurationsCollection(File baseDir, ExclusiveAccess lock) {
		this.inventory = new Inventory();
		this.sync = null;
		this.baseDir = Objects.requireNonNull(baseDir);
		baseDir.mkdirs();
		this.lock = lock;
		this.catalog = new File(baseDir, MASTER_FILE_NAME);
		try {
			sync(this::cleanupInventory);
		} catch (IOException e) {
			Logger.getLogger(UserConfigurationsCollection.class.getCanonicalName()).log(Level.WARNING, "Failed to read custom configurations.", e);
		}
	}

	public synchronized Set<ConfigurationDetails> getConfigurationDetails() {
		return inventory.entries().stream()
				.map(c->c.getConfiguration().orElse(null))
				.filter(v->v!=null)
				.map(v->v.getDetails())
				.collect(Collectors.toSet());
	}

	public synchronized Map<String, Object> getConfiguration(String key) throws ConfigurationsProviderException {
		return Optional.ofNullable(inventory.get(key))
				.flatMap(v->v.getConfiguration())
				.map(v->v.getMap())
				.orElse(null);
	}

	public synchronized Optional<String> addConfiguration(String niceName, String description, Map<String, Object> config) {
		try {
			if (catalog==null) {
				throw new FileNotFoundException();
			}
			return sync(()-> {
				ConfigurationDetails p = new ConfigurationDetails.Builder(inventory.nextIdentifier())
						.niceName(niceName)
						.description(description).build();
				try {
					InventoryEntry ret = InventoryEntry.create(new Configuration(p, new HashMap<>(config)), newConfigurationFile());
					inventory.add(ret);
					return Optional.of(ret.getIdentifier());
				} catch (IOException e) {
					logger.log(Level.WARNING, "Failed to add configuration.", e);
					return Optional.empty();
				}
			});
		} catch (IOException e) {
			logger.log(Level.WARNING, "Could not add configuration", e);
			return Optional.empty();
		}
	}
	
	public synchronized boolean removeConfiguration(String key) {
		try {
			if (catalog==null) {
				throw new FileNotFoundException();
			}
			return sync(()->inventory.remove(key)!=null);
		} catch (IOException e) {
			logger.log(Level.WARNING, "Failed to remove configuration.", e);
			return false;
		}
	}
	
	public synchronized boolean containsConfiguration(String key) {
		return inventory.keys().contains(key);
	}
	
	private File newConfigurationFile() throws IOException {
		// Note that this file is not a temporary file, 
		// it's just convenient to use it to create a unique name
		return File.createTempFile("config-", CONFIG_EXT, baseDir);
	}

	private synchronized <T> T sync(Supplier<T> func) throws IOException {
		// Check this early, to avoid reading from a file that didn't exist until it was opened.
		boolean fileExists = catalog.exists();
		if (!fileExists && func==null) {
			return null;
		}
		try {
			acquireLock();
		} catch (InterruptedException e1) {
			Thread.currentThread().interrupt();
			return null;
		}
		// We now have exclusive access to the resources.
		// Note that "exclusive access" is an agreement made with other processes running this code.
		// Technically, we only have exclusive access to the lock file itself.
		try {
			// Read configurations from file
			if (sync == null || new Date(catalog.lastModified()).after(sync)) {
				if (fileExists) {
					inventory = Inventory.read(catalog);
					sync = new Date(catalog.lastModified());
				} else {
					// reset inventory
					inventory = new Inventory();
				}
			}

			// Update file
			if (func!=null) {
				T ret = func.get();
				try {
					inventory.write("catalog-", catalog);
					sync = new Date(catalog.lastModified());
				} catch (IOException e) {
					logger.log(Level.WARNING, "Failed to write catalog to file system.", e);
					throw e;
				}
				return ret;
			} else {
				return null;
			}
		} finally {
			lock.release();
		}
	}
	
	/**
	 * Cleans up the inventory.
	 * @return true if the inventory was touched, false otherwise.
	 */
	private boolean cleanupInventory() {
		boolean modified = false;
		modified |= removeUnreadable();
		modified |= recreateMismatching();
		modified |= importConfigurations();
		return modified;
	}
	
	/**
	 * Removes unreadable configurations from the file system.
	 * @return true if some configurations were removed, false otherwise
	 */
	private boolean removeUnreadable() {
		List<File> unreadable = inventory.removeUnreadable();
		unreadable.forEach(v->v.delete());
		return !unreadable.isEmpty();
	}
	
	/**
	 * Recreates mismatching configurations.
	 * @return true if some configurations were recreated, false otherwise
	 */
	private boolean recreateMismatching() {
		List<Configuration> mismatching = inventory.removeMismatching();
		mismatching.forEach(entry->{
			try {
				inventory.add(InventoryEntry.create(entry.copyWithIdentifier(inventory.nextIdentifier()), newConfigurationFile()));
			} catch (IOException e1) {
				logger.log(Level.WARNING, "Failed to write a configuration.", e1);
			}
		});
		return !mismatching.isEmpty();
	}

	/**
	 * Imports new configurations from the file system.
	 * @return true if something was imported, false otherwise
	 */
	private boolean importConfigurations() {
		// Create a set of files in the inventory
		Set<File> files = inventory.entries().stream().map(v->v.getPath()).collect(Collectors.toSet());
		List<File> entriesToImport = Arrays.asList(baseDir.listFiles(f->
			f.isFile()
			&& !f.equals(catalog)	// Exclude the master catalog (should it have the same extension as entries)
			&& f.getName().endsWith(CONFIG_EXT)
			&& !files.contains(f))	// Exclude files already in the inventory
		);
		entriesToImport.forEach(f->{
			try {
				inventory.add(InventoryEntry.create(Configuration.read(f).copyWithIdentifier(inventory.nextIdentifier()), newConfigurationFile()));
				f.delete();
			} catch (IOException e) {
				if (logger.isLoggable(Level.FINE)) {
					logger.log(Level.FINE, "Failed to read: " + f, e);
				}
			}
		});
		return !entriesToImport.isEmpty();
	}
	
	private void acquireLock() throws IOException, InterruptedException {
		// Acquire lock
		int i = 0;
		boolean locked = false;
		while (!(locked = lock.acquire()) && i<20) {
			i++;
			try {
				//Sleep between 50-150 ms
				Thread.sleep(50+(int)Math.random()*100);
			} catch (InterruptedException e) {
				throw e;
			}
		}
		if (!locked) {
			throw new IOException("Failed to acquire lock.");
		}
	}

}