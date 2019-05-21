package org.daisy.streamline.api.details;

import java.util.List;

/**
 * Provides a supplier of {@link FormatDetails}.
 * @author Joel Håkansson
 *
 */
public interface FormatDetailsSupplier {

	/**
	 * Lists the details provided by this implementation.
	 * @return a list of format details
	 */
	public List<FormatDetails> listDetails();

}
