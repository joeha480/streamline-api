package org.daisy.streamline.api.media;

import java.util.Objects;

/**
 * Provides an identifier for a format.
 * @author Joel Håkansson
 */
public final class FormatIdentifier {
	private final String identifier;

	private FormatIdentifier(String identifier) {
		this.identifier = Objects.requireNonNull(identifier);
	}

	/**
	 * Creates a new format identifier.
	 * @param identifier the identifier
	 * @return the new instance
	 */
	public static FormatIdentifier with(String identifier) {
		return new FormatIdentifier(identifier);
	}

	/**
	 * Gets the identifier value.
	 * @return the identifier value
	 */
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
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
		FormatIdentifier other = (FormatIdentifier) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return identifier;
	}

}
