package org.daisy.streamline.api.media;

/**
 * <p>
 * Provides an interface for a FileSetMaker service. The purpose of
 * this interface is to expose an implementation of a FileSetMaker as
 * an OSGi service.
 * </p>
 * 
 * <p>
 * To comply with this interface, an implementation must be thread safe and
 * address both the possibility that only a single instance is created and used
 * throughout and that new instances are created as desired.
 * </p>
 * 
 * @author Joel Håkansson
 * 
 */
public interface FileSetMakerService {

	/**
	 * Builds a file set based on the supplied file.
	 * @param f the file to create a file set for
	 * @return returns the file set
	 */
	public FileSet create(AnnotatedFile f);

}
