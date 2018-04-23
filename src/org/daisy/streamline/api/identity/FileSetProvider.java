package org.daisy.streamline.api.identity;

import org.daisy.streamline.api.media.AnnotatedFile;
import org.daisy.streamline.api.media.FileDetails;
import org.daisy.streamline.api.media.FileSet;


/**
 * Provides an interface for creating a file set.
 * 
 * @author Joel Håkansson
 */
public interface FileSetProvider {

	/**
	 * Returns true if this factory can create a file set for the specified type, 
	 * false otherwise.
	 * 
	 * @param type the media type
	 * @return returns true if file sets can be created by this factory, false otherwise
	 */
	public boolean accepts(FileDetails type);

	/**
	 * Builds a file set based on the supplied file.
	 * @param f the file to create a file set for
	 * @return returns the file set
	 * @throws FileSetException if a file set could not be created
	 */
	public FileSet create(AnnotatedFile f) throws FileSetException;

	/**
	 * <p>Informs the implementation that it was discovered and instantiated using
	 * information collected from a file within the <tt>META-INF/services</tt> directory.
	 * In other words, it was created using SPI (service provider interfaces).</p>
	 * 
	 * <p>This information, in turn, enables the implementation to use the same mechanism
	 * to set dependencies as needed.</p>
	 * 
	 * <p>If this information is <strong>not</strong> given, an implementation
	 * should avoid using SPIs and instead use
	 * <a href="http://wiki.osgi.org/wiki/Declarative_Services">declarative services</a>
	 * for dependency injection as specified by OSGi. Note that this also applies to
	 * several newInstance() methods in the Java API.</p>
	 * 
	 * <p>The class that created an instance with SPI must call this method before
	 * putting it to use.</p>
	 */
	public default void setCreatedWithSPI() {}

}
