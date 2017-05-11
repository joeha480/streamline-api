package org.daisy.dotify.api.validity;

import java.net.URL;

/**
 * Provides an interface for validators
 * @author Joel Håkansson
 */
public interface Validator {
	
	/**
	 * Validates the resource at the given URL.
	 * @param input the resource URL
	 * @return returns the validation report
	 */
	public ValidationReport validate(URL input);

}
