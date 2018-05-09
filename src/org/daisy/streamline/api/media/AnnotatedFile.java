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
	 * @deprecated use {@link #getPath()}
	 */
	@Deprecated
	public File getFile();
	
	/**
	 * Gets the path.
	 * @return the path
	 */
	public Path getPath();

}
