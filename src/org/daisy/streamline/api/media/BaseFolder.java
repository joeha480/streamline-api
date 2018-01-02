package org.daisy.streamline.api.media;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provides a location for a file set.
 * 
 * @author Joel Håkansson
 */
public final class BaseFolder {
	private final Path dir;
	
	private BaseFolder(Path dir) {
		this.dir = dir;
	}
	
	/**
	 * Creates a new base folder at the specified location.
	 * @param dir the location
	 * @return the new instance
	 */
	public static BaseFolder with(Path dir) {
		return new BaseFolder(dir);
	}
	
	/**
	 * Creates a new base folder at the specified location.
	 * @param first the path string or initial part of the path string
	 * @param more additional strings to be joined to form the path string
	 * @return the new instance
	 */
	public static BaseFolder with(String first, String... more) {
		return new BaseFolder(Paths.get(first, more));
	}
	
	/**
	 * 
	 * @return the path
	 */
	public Path getPath() {
		return dir;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dir == null) ? 0 : dir.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseFolder other = (BaseFolder) obj;
		if (dir == null) {
			if (other.dir != null)
				return false;
		} else if (!dir.equals(other.dir))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return dir.toString();
	}

}
