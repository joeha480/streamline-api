package org.daisy.streamline.api.validity;

import java.util.Collection;

import org.daisy.streamline.api.media.FormatIdentifier;

/**
 * <p>
 * Provides an interface for a ValidatorFactoryMaker service. The purpose of
 * this interface is to expose an implementation of a ValidatorFactoryMaker
 * as an OSGi service.
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
public interface ValidatorFactoryMakerService {
	
	/**
	 * Returns a new validator.
	 * @param identifier the format identifier
	 * @return returns true if a validator for the format identifier exists, false otherwise
	 * @deprecated use {@link #newValidator(FormatIdentifier)}
	 */
	@Deprecated
	public Validator newValidator(String identifier);
	
	/**
	 * Lists supported format identifiers.
	 * @return a list of format identifiers
	 * @deprecated use {@link #listFormats()}
	 */
	@Deprecated
	public Collection<String> listIdentifiers();
	
	/**
	 * Returns a new validator.
	 * @param identifier the format identifier
	 * @return returns true if a validator for the format identifier exists, false otherwise
	 */
	public Validator newValidator(FormatIdentifier identifier);
	
	/**
	 * Lists supported format identifiers.
	 * @return a list of format identifiers
	 */
	public Collection<FormatIdentifier> listFormats();
	
}
