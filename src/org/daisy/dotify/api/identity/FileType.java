package org.daisy.dotify.api.identity;

import java.io.File;

public interface FileType {

	/**
	 * Gets the file.
	 * @return the file
	 */
	public File getFile();

	/**
	 * Gets the identifying file extension of this file's format without the leading '.' (which may or may not 
	 * correspond with the actual extension of an associated file).
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
