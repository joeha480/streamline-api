package org.daisy.streamline.api.media;

import java.io.File;
import java.nio.file.Path;


/**
 * Provides an annotated file
 * @author Joel Håkansson
 */
public interface AnnotatedFile extends FileDetails {

	/**
	 * Gets the file.
	 * @return the file
	 */
	public File getFile();
	
	public Path getPath();

}
