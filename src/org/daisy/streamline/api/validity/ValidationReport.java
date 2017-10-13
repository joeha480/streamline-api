package org.daisy.streamline.api.validity;

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
	private final boolean valid;
	private final List<ValidatorMessage> messages;

	/**
	 * Provides a validation report builder.
	 */
	public static class Builder {
		private final URL source;
		private boolean valid = true;
		private final List<ValidatorMessage> messages = new ArrayList<>();
		
		/**
		 * Creates a new builder for validation reports. Initially, 
		 * the source is valid.
		 * @param source the source for the report
		 */
		public Builder(URL source) {
			this.source = source;
		}

		/**
		 * Sets the valid property of the builder, defaults to true.
		 * @param value true if the source is valid, false otherwise.
		 * @return returns this builder
		 */
		public Builder valid(boolean value) {
			this.valid = value;
			return this;
		}

		/**
		 * Adds a validation message to this builder. This method will check
		 * if the message causes invalidity, and if so sets valid to false.
		 * @param value the message
		 * @return returns this builder
		 */
		public Builder addMessage(ValidatorMessage value) {
			this.messages.add(value);
			if (value.getType().causeForInvalidity()) {
				this.valid = false;
			}
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
		this.valid = builder.valid;
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
	 * Returns true if the document at {@link #getSource()} is valid, false otherwise.
	 * @return returns true if validation was successful, false otherwise
	 */
	public boolean isValid() {
		return valid;
	}
	
	/**
	 * Gets the messages in this report.
	 * @return returns a list of messages
	 */
	public List<ValidatorMessage> getMessages() {
		return messages;
	}

}
