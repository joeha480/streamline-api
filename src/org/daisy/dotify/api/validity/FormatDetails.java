package org.daisy.dotify.api.validity;

/**
 * Provides details about a file format.
 * @author Joel Håkansson
 */
public interface FormatDetails {

	/**
	 * Gets the identifying name for the file format. This should be the shortest
	 * name commonly used when referring to the file format.
	 * @return returns the name, or null
	 */
	public String getFormatName();
	
	/**
	 * Gets the version of the format.
	 * @return returns the version, or null
	 */
	public String getFormatVersion();

	/**
	 * Gets the identifying file extension of the file format without the leading '.'.
	 * 
	 * @return the identifying file extension, or null
	 */
	public String getExtension();

	/**
	 * Gets the media type for this file's format.
	 * @return the media type, or null.
	 * @see <a href="http://www.iana.org/assignments/media-types/media-types.xhtml">Media Types</a>
	 */
	public String getMediaType();
	

	/**
	 * Tests whether this object is of type other.
	 * Dog is an animal
	 * A.isOfType(B)
	 * @param other the second object
	 * @return returns true if this object is a super type of other
	 */
	public default boolean isOfType(FormatDetails other) {
		int i = 0;
		if (other.getFormatName()!=null) {
			if (other.getFormatName().equals(getFormatName())) {
				i++;
			} else {
				return false;
			}
		}
		if (other.getFormatVersion()!=null) {
			if (other.getFormatVersion().equals(getFormatVersion())) {
				i++;
			} else {
				return false;
			}
		}
		if (other.getExtension()!=null) {
			if (other.getExtension().equals(getExtension())) {
				i++;
			} else {
				return false;
			}
		}
		if (other.getMediaType()!=null) {
			if (other.getMediaType().equals(getMediaType())) {
				i++;
			} else {
				return false;
			}
		}
		return true;
	}
}
