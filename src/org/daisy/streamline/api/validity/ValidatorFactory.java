package org.daisy.streamline.api.validity;

import java.util.Collection;

import org.daisy.streamline.api.media.FormatIdentifier;

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
	 * @deprecated use {@link #listFormats()}
	 */
	@Deprecated
	public Collection<String> listIdentifiers();
	
	/**
	 * Lists supported format identifiers.
	 * @return returns a collection of supported format identifiers
	 */
	public Collection<FormatIdentifier> listFormats();

	/**
	 * Creates a new validator instance.
	 * @param identifier the format identifier
	 * @return returns a new validator
	 * @throws ValidatorFactoryException if a validator for the specified indentifer cannot be created
	 * @deprecated use {@link #newValidator(FormatIdentifier)}
	 */
	@Deprecated
	public Validator newValidator(String identifier) throws ValidatorFactoryException;
	
	/**
	 * Creates a new validator instance.
	 * @param identifier the format identifier
	 * @return returns a new validator
	 * @throws ValidatorFactoryException if a validator for the specified indentifer cannot be created
	 */
	public Validator newValidator(FormatIdentifier identifier) throws ValidatorFactoryException;

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
