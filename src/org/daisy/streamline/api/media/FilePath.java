package org.daisy.streamline.api.media;

import java.nio.file.Path;

final class FilePath {
	private String value;

	private FilePath(String value) {
		this.value = value;
	}

	/**
	 * @param parent the base path
	 * @param child the child path
	 * @throws  IllegalArgumentException if {@code child} is not a {@code Path} that can be relativized against this path
	 */
	static FilePath with(Path parent, Path child) {
		return new FilePath(parent.relativize(child).toString());
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
		FilePath other = (FilePath) obj;
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