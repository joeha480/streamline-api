package org.daisy.dotify.api.tasks;

import java.io.File;


/**
 * Provides an annotated file
 * @author Joel Håkansson
 */
public interface AnnotatedFile extends Annotated {

	/**
	 * Gets the file.
	 * @return the file
	 */
	public File getFile();

}
