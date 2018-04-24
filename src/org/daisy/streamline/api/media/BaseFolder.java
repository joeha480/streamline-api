package org.daisy.streamline.api.media;

import java.io.File;
import java.nio.file.Path;

/**
 * Provides a location for a file set.
 * 
 * @author Joel Håkansson
 */
public final class BaseFolder {
	private final File dir;
	
	private BaseFolder(File dir) {
		this.dir = dir;
	}
	
	/**
	 * Creates a new base folder at the specified location.
	 * @param dir the location
	 * @return the new instance
	 */
	public static BaseFolder with(File dir) {
		return new BaseFolder(dir);
	}

	/**
	 * Gets the location
	 * @return the location
	 */
	public File getLocation() {
		return dir;
	}
	
	/**
	 * 
	 * @return the path
	 */
	public Path getPath() {
		return dir.toPath();
	}
}
