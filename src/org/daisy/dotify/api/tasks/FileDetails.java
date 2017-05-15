package org.daisy.dotify.api.tasks;

import java.util.Map;

import org.daisy.dotify.api.validity.FormatDetails;

/**
 * Provides details regarding a file.
 * @author Joel Håkansson
 */
public interface FileDetails extends FormatDetails {

	/**
	 * Gets additional properties about the file. To conform with this interface
	 * the returned map and all value objects must be immutable.
	 * @return returns a map of properties
	 */
	public Map<String, Object> getProperties();

}
