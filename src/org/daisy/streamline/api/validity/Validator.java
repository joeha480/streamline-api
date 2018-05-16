package org.daisy.streamline.api.validity;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.daisy.streamline.api.media.InputStreamSupplier;
import org.daisy.streamline.api.option.UserOption;

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
	public default ValidationReport validate(URL input) {
		return validate(input, Collections.emptyMap());
	}
	
	/**
	 * Validates the resource at the given URL.
	 * @param input the resource URL
	 * @param options the validation options
	 * @return returns the validation report
	 */
	public ValidationReport validate(URL input, Map<String, Object> options);
	
	/**
	 * Validates the given resource.
	 * @param input the resource
	 * @return returns the validation report
	 */
	public default ValidationReport validate(InputStreamSupplier input) {
		return validate(input, Collections.emptyMap());
	}
	
	/**
	 * Validates the given resource.
	 * @param input the resource
	 * @param options the validation options
	 * @return returns the validation report
	 */
	public ValidationReport validate(InputStreamSupplier input, Map<String, Object> options);

	/**
	 * Gets a list of parameters applicable to this instance
	 * @return returns a list of parameters
	 */
	public default List<UserOption> listOptions() {
		return Collections.emptyList();
	}

}
