package org.daisy.dotify.api.tasks;

/**
 * Provides an annotated file
 * @author Joel Håkansson
 */
public interface Annotated {
	
	/**
	 * Gets the identifying file extension of this file's format without the leading '.' (which may or may not 
	 * correspond with the actual extension of the file retrieved by getFile).
	 * 
	 * @return the identifying file extension, or null if unknown
	 */
	public String getExtension();

	/**
	 * Gets the media type for this file's format.
	 * @return the media type, or null if unknown.
	 * @see <a href="http://www.iana.org/assignments/media-types/media-types.xhtml">Media Types</a>
	 */
	public String getMediaType();

}
