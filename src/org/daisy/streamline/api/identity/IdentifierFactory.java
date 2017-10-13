package org.daisy.streamline.api.identity;

import org.daisy.streamline.api.tasks.FileDetails;

/**
 * Provides a factory for identifiers.
 * @author Joel Håkansson
 */
public interface IdentifierFactory {
	
	/**
	 * Creates a new identifier.
	 * @return returns a new identifier instance
	 */
	public Identifier newIdentifier();
	
	/**
	 * Returns true if this factory can enhance upon the supplied media type.
	 * In other words, the factory provides identifiers for sub types of the
	 * already identified type. The factory should return true if null is
	 * provided.
	 * 
	 * @param type the media type, or null
	 * @return returns true if the media type can be improved by this factory, false otherwise
	 */
	public boolean accepts(FileDetails type);
	
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
