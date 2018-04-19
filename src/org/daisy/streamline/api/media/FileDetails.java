package org.daisy.streamline.api.media;

import java.util.Map;
import java.util.Optional;

/**
 * Provides details regarding a file.
 * @author Joel Håkansson
 */
public interface FileDetails {
	
	/**
	 * Gets the identifying name for the file format. This should be the shortest
	 * name commonly used when referring to the file format.
	 * @return returns the name, or null if not known
	 */
	public String getFormatName();
	
	/**
	 * Gets the format's unique identifier
	 * @return the format's unique identifier
	 */
	public Optional<FormatIdentifier> getFormatIdentifier();
	
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
	
	/**
	 * Gets additional properties about the file. To conform with this interface
	 * the returned map and all value objects must be immutable.
	 * @return returns a map of properties
	 */
	public Map<String, Object> getProperties();

}
