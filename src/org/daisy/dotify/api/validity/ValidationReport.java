package org.daisy.dotify.api.validity;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides a validation report.
 * @author Joel Håkansson
 *
 */
public final class ValidationReport {
	private final URL source;
	private final boolean success;
	private final List<ValidatorMessage> messages;

	/**
	 * Provides a validation report builder.
	 */
	public static class Builder {
		private final URL source;
		private boolean success = false;
		private final List<ValidatorMessage> messages = new ArrayList<>();
		
		/**
		 * Creates a new builder for validation reports
		 * @param source the source for the report
		 */
		public Builder(URL source) {
			this.source = source;
		}

		/**
		 * Sets the success property of the builder.
		 * @param value true if validation was successful, false otherwise.
		 * @return returns this builder
		 */
		public Builder success(boolean value) {
			this.success = value;
			return this;
		}

		/**
		 * Adds a validation message to this builder.
		 * @param value the message
		 * @return returns this builder
		 */
		public Builder addMessage(ValidatorMessage value) {
			this.messages.add(value);
			return this;
		}

		/**
		 * Builds the validation report based on the current state of the builder.
		 * @return returns a new validation report
		 */
		public ValidationReport build() {
			return new ValidationReport(this);
		}
	}

	private ValidationReport(Builder builder) {
		this.source = builder.source;
		this.success = builder.success;
		this.messages = Collections.unmodifiableList(new ArrayList<>(builder.messages));
	}
	
	/**
	 * Gets the source for this validation report
	 * @return returns the URL to the source
	 */
	public URL getSource() {
		return source;
	}
	
	/**
	 * Returns true if validation was successful and the source is valid, false otherwise.
	 * @return returns true if validation was successful, false otherwise
	 */
	public boolean isSuccessful() {
		return success;
	}
	
	/**
	 * Gets the messages in this report.
	 * @return returns a list of messages
	 */
	public List<ValidatorMessage> getMessages() {
		return messages;
	}

}
