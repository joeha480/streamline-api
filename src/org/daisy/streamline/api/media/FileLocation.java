package org.daisy.streamline.api.media;

import java.io.File;
//TODO: one could question the usefulness of this class since, a simple File object would work equally well.
final class FileLocation {
	private final String value;

	private FileLocation(String value) {
		this.value = value;
	}
	
	static FileLocation with(File value) {
		return new FileLocation(value.getAbsolutePath());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		FileLocation other = (FileLocation) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return value;
	}

}
