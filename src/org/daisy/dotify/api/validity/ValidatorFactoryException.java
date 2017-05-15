/**
 * 
 */
package org.daisy.dotify.api.validity;

/**
 * @author Joel Håkansson
 *
 */
public class ValidatorFactoryException extends Exception {

	/**
	 * @param message
	 */
	public ValidatorFactoryException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ValidatorFactoryException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ValidatorFactoryException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ValidatorFactoryException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
