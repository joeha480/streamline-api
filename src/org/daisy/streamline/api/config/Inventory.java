package org.daisy.streamline.api.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

final class Inventory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1189329230377507983L;
	private static final Logger logger = Logger.getLogger(Inventory.class.getCanonicalName());
	private HashMap<String, InventoryEntry> configs;
	private Long index;
	
	Inventory() {
		this.configs = new HashMap<>();
		this.index = 0l;
	}

	/**
	 * Gets the inventory identifiers.
	 * @return a set of identifiers
	 */
	Set<String> keys() {
		return Collections.unmodifiableSet(configs.keySet());
	}
	
	/**
	 * Gets the inventory entries.
	 * @return a collection of entries
	 */
	Collection<InventoryEntry> entries() {
		return Collections.unmodifiableCollection(configs.values());
	}
	
	/**
	 * Gets the entry with the specified key.
	 * @param key the key
	 * @return returns the corresponding entry
	 */
	InventoryEntry get(String key) {
		return configs.get(key);
	}

	/**
	 * Adds a new entry to the inventory.
	 * @param entry the entry 
	 */
	void add(InventoryEntry entry) {
		configs.put(entry.getIdentifier(), entry);
	}

	/**
	 * Removes the entry with the specified key.
	 * @param key the key
	 * @return returns the removed entry
	 */
	InventoryEntry remove(String key) {
		InventoryEntry ret = configs.remove(key);
		if (ret!=null) {
			ret.getPath().delete();
		}
		return ret;
	}

	/**
	 * Gets the current index.
	 * @return the current index
	 */
	long getIndex() {
		return index;
	}

	synchronized String nextIdentifier() {
		index++;
		return UserConfigurationCollection.class.getCanonicalName()+"_"+index;
	}

	/**
	 * Removes configurations with mismatching identifiers. 
	 * @return a list of configurations with mismatching identifiers
	 */
	List<Configuration> removeMismatching() {
		// List mismatching
		List<InventoryEntry> mismatching = configs.values().stream()
			.filter(v->
				(v.getConfiguration().isPresent()
				&& !v.getIdentifier().equals(v.getConfiguration().get().getDetails().getKey()))
			).collect(Collectors.toList());
		// Remove from inventory
		mismatching.forEach(v->configs.remove(v.getIdentifier()));
		// Return the list of removed configurations 
		return mismatching.stream()
				.map(v->v.getConfiguration().get())
				.collect(Collectors.toList());
	}
	
	/**
	 * Removes entries whose configurations are unreadable from the inventory.
	 * @return the list of entries removed from the inventory
	 */
	List<File> removeUnreadable() {
		// List unreadable
		List<InventoryEntry> unreadable = configs.values()
				.stream()
				.filter(v->!v.getConfiguration().isPresent())
				.collect(Collectors.toList());
		// Remove from inventory
		unreadable.forEach(v->configs.remove(v.getIdentifier()));
		// Return the list of removed files
		return unreadable.stream()
				.map(v->v.getPath())
				.collect(Collectors.toList());
	}

	/**
	 * Creates a new inventory from the specified file. See also {@link #write(File)}.
	 * @param catalog the file containing the inventory
	 * @return a new inventory instance
	 * @throws IOException if the file could not be read
	 */
	static Inventory read(File catalog) throws IOException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(catalog))) {
			return (Inventory)ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new IOException("Failed to read/update file: " + catalog, e);
		}
	}
	
	/**
	 * Writes the inventory to the specified file. See also {@link #read(File)}.
	 * @param prefix the file prefix for a temporary backup file
	 * @param catalog the file to write to
	 * @throws IOException if the file could not be updated
	 */
	void write(String prefix, File catalog) throws IOException {
		File backup = File.createTempFile(prefix, null, catalog.getParentFile());
		if (catalog.exists()) {
			// Create a copy of the current state
			Files.copy(catalog.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}

		// Update the file
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(catalog))) {
			oos.writeObject(this);
			backup.delete();
		} catch (IOException e) {
			try {
				Files.move(backup.toPath(), catalog.toPath(), StandardCopyOption.ATOMIC_MOVE);
			} catch (IOException e2) {
				logger.log(Level.INFO, "A backup has been saved at: " + backup, e2);
			}
			throw new IOException("Failed to update file: " + catalog, e);
		}
	}
}
