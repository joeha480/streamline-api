package org.daisy.streamline.api.identity;

import org.daisy.streamline.api.tasks.AnnotatedFile;


/**
 * Provides an interface for identifying a file format.
 * 
 * @author Joel Håkansson
 */
public interface Identifier {

	/**
	 * Identifies the file format of the specified file.
	 * @param f the file to identify
	 * @return returns the identified file
	 * @throws IdentificationFailedException if identification is unsuccessful
	 */
	public AnnotatedFile identify(AnnotatedFile f) throws IdentificationFailedException;
	
}
