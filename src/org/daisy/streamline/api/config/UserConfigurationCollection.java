package org.daisy.streamline.api.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Provides a custom configuration collection that lets a user add and
 * remove configurations. The collection data is stored in the users
 * home directory.
 */
public final class UserConfigurationCollection {
	private static final Logger logger = Logger.getLogger(UserConfigurationCollection.class.getCanonicalName());
	private static final String MASTER_FILE_NAME = "catalog.ser";
	private static final String CONFIG_EXT = ".ser";
	private final File configDir;
	private final SingletonAccess lock;
	private final File catalog;

	private Inventory inventory;
	private Date sync;

	public UserConfigurationCollection(File configDir, SingletonAccess lock) {
		this.inventory = new Inventory();
		this.sync = null;
		this.configDir = configDir;
		if (this.configDir!=null) {
			configDir.mkdirs();
			this.lock = lock;
			this.catalog = new File(configDir, MASTER_FILE_NAME);
		} else {
			this.lock = null;
			this.catalog = null;
		}
		if (this.catalog!=null) {
			try {
				sync(this::cleanupInventory);
			} catch (IOException e) {
				Logger.getLogger(UserConfigurationCollection.class.getCanonicalName()).log(Level.WARNING, "Failed to read custom configurations.", e);
			}
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
					InventoryEntry ret = createEntry(new Configuration(p, new HashMap<>(config)));
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
	
	private InventoryEntry createEntry(Configuration c) throws IOException {
		File f = File.createTempFile("config-", CONFIG_EXT, configDir);
		c.write(f);
		InventoryEntry ret = new InventoryEntry(f, c.getDetails().getKey(), f.lastModified());
		// Manually set the configuration to avoid re-reading it from file
		ret.setConfiguration(c);
		return ret;
	}

	private synchronized <T> T sync(Supplier<T> func) throws IOException {
		// Check this early, to avoid reading from a file that didn't exist until it was opened.
		boolean fileExists = catalog.exists();
		if (!fileExists && func==null) {
			return null;
		}
		acquireLock();
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
			lock.releaseLock();
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
				inventory.add(createEntry(entry.copyWithIdentifier(inventory.nextIdentifier())));
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
		List<File> entriesToImport = Arrays.asList(configDir.listFiles(f->
			f.isFile()
			&& !f.equals(catalog)	// Exclude the master catalog (should it have the same extension as entries)
			&& f.getName().endsWith(CONFIG_EXT)
			&& !files.contains(f))	// Exclude files already in the inventory
		);
		entriesToImport.forEach(f->{
			try {
				inventory.add(createEntry(Configuration.read(f).copyWithIdentifier(inventory.nextIdentifier())));
				f.delete();
			} catch (IOException e) {
				if (logger.isLoggable(Level.FINE)) {
					logger.log(Level.FINE, "Failed to read: " + f, e);
				}
			}
		});
		return !entriesToImport.isEmpty();
	}
	
	private void acquireLock() throws IOException {
		// Acquire lock
		int i = 0;
		boolean locked = false;
		while (!(locked = lock.acquireLock()) && i<20) {
			i++;
			try {
				//Sleep between 50-150 ms
				Thread.sleep(50+(int)Math.random()*100);
			} catch (InterruptedException e) { }
		}
		if (!locked) {
			throw new IOException("Failed to acquire lock.");
		}
	}

}