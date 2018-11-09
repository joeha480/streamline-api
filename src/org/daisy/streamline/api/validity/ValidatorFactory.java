package org.daisy.streamline.api.validity;

import java.util.Collection;
import java.util.Optional;

import org.daisy.streamline.api.media.FileDetails;

/**
 * <p>
 * Provides an interface for a validator service. The purpose of this
 * interface is to expose an implementation of {@link Validator} as a
 * service.
 * </p>
 * 
 * <p>
 * To comply with this interface, an implementation must be thread safe and
 * address both the possibility that only a single instance is created and used
 * throughout and that new instances are created as desired.
 * </p>
 * 
 * @author Joel Håkansson
 */
public interface ValidatorFactory {

	/**
	 * Lists supported format identifiers.
	 * @return returns a collection of supported format identifiers
	 */
	public Collection<String> listIdentifiers();

	/**
	 * Creates a new validator instance.
	 * @param identifier the format identifier
	 * @return returns a new validator
	 * @throws ValidatorFactoryException if a validator for the specified identifier cannot be created
	 */
	public Validator newValidator(String identifier) throws ValidatorFactoryException;
	
	/**
	 * Creates a new validator instance.
	 * @param details the details for the file to validate
	 * @return a validator
	 * @throws ValidatorFactoryException if a validator for the specified identifier cannot be created
	 */
	public Validator newValidator(FileDetails details) throws ValidatorFactoryException;
	
	/**
	 * <p>Returns a double if this factory can create a validator for the supplied details.
	 * The higher the value, the higher the accuracy of the implementation for the 
	 * specified details. A positive value implies that a format specific implementation
	 * is available. A negative value implies that some generic method is employed that
	 * may be used on some set of similar formats, but which may not validate the
	 * content on all levels of abstraction.</p>
	 * <p>For example, a file may be a <em>valid</em> ZIP container, but the data inside
	 * it may not be validated against applicable format specifications. Similarly, an 
	 * XML-file may be validated using a DTD (or even just the XML-specification), but
	 * the format to which the DTD belongs could mandate additional constraints which
	 * cannot be validated without providing a format specific implementation. In these
	 * cases, a negative value of some magnitude should be used.</p>
	 * 
	 * @param details the details
	 * @return a double if a validator can be created,
	 * 		 or an empty optional if no validator can be created.
	 */
	public Optional<Double> supportsDetails(FileDetails details);

	/**
	 * <p>Informs the implementation that it was discovered and instantiated using
	 * information collected from a file within the <code>META-INF/services</code> directory.
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
