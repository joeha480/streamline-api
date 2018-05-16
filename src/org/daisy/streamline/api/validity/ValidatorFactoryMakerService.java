package org.daisy.streamline.api.validity;

import java.util.Collection;
import java.util.Optional;

import org.daisy.streamline.api.media.FileDetails;

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
	 */
	public Validator newValidator(String identifier);
	
	/**
	 * Returns a new validator.
	 * @param details the details for the file to validate
	 * @return a validator
	 */
	public Optional<Validator> newValidator(FileDetails details);
	
	/**
	 * Lists supported format identifiers.
	 * @return a list of format identifiers
	 */
	public Collection<String> listIdentifiers();
	
}
