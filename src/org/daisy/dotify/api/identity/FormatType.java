package org.daisy.dotify.api.identity;

import java.util.Map;



/**
 * Provides a format type definition.
 * @author Joel Håkansson
 */
public interface FormatType {

	/**
	 * Gets the identifying name for this format. This should be the shortest
	 * name commonly used when referring to the file format.
	 * @return returns the name, or null if not known
	 */
	public String getName();
	
	/**
	 * Gets the version of the format.
	 * @return returns the format version, or null if not known
	 */
	public String getVersion();

	/**
	 * Gets additional properties for this format.
	 * @return returns additional properties, or an empty map
	 */
	public Map<String, String> getProperties();

}