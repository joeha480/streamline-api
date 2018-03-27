package org.daisy.streamline.api.config;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

public final class InventoryEntry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 339434372295701516L;
	private final File path;
	private final String identifier;
	private final long modified;
	// Don't serialize this here, as this object is serialized separately
	private transient Optional<Configuration> config;

	InventoryEntry(File path, String identifier, long modified) {
		super();
		this.path = path;
		this.identifier = identifier;
		this.modified = modified;
		// We're using null value on an optional internally, to signal that the file should be read.
		this.config = null;
	}

	public File getPath() {
		return path;
	}

	public String getIdentifier() {
		return identifier;
	}

	public long getModified() {
		return modified;
	}
	
	public Optional<Configuration> getConfiguration() {
		if (config==null) {
			try {
				config = Optional.of(Configuration.read(path));
			} catch (IOException e) {
				config = Optional.empty();
			}
		}
		return config;
	}
	
	void setConfiguration(Configuration c) {
		this.config = Optional.of(c);
	}

}
