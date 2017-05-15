package org.daisy.dotify.api.validity;

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
	 * Returns a new validator for the specified format.
	 * @param identifier the file format
	 * @return returns true if a validator for the format exists, false otherwise
	 */
	public Validator newValidator(FormatDetails identifier);
	
}
