package org.daisy.streamline.api.tasks;

import java.util.Objects;
import java.util.Optional;

import org.daisy.streamline.api.media.FormatIdentifier;

/**
 * Provides information about a task system. 
 *
 * @author Joel Håkansson
 */
public class TaskSystemInformation {
	private final FormatIdentifier input;
	private final FormatIdentifier output;
	private final Optional<String> locale;

	/**
	 * Provides a builder for task group information
	 */
	public static class Builder {
		private final FormatIdentifier input;
		private final FormatIdentifier output;
		// optional
		private String locale = null; 

		/**
		 * Creates a new builder with the specified options
		 * @param input the input format
		 * @param output the output format
		 */
		public Builder(FormatIdentifier input, FormatIdentifier output) {
			Objects.requireNonNull(input, "Input format cannot be null.");
			Objects.requireNonNull(output, "Output format cannot be null.");
			this.input = input;
			this.output = output;
		}

		/**
		 * Sets the locale for the builder.
		 * @param value the locale
		 * @return returns this builder
		 */
		public Builder locale(String value) {
			this.locale = value;
			return this;
		}

		/**
		 * Creates a new task group information based on the current state of the builder.
		 * @return returns a new task group information instance
		 */
		public TaskSystemInformation build() {
			return new TaskSystemInformation(this);
		}
	}
	
	private TaskSystemInformation(Builder builder) {
		this.input = builder.input;
		this.output = builder.output;
		this.locale = Optional.ofNullable(builder.locale);
	}
	
	/**
	 * Gets the input format for the task group
	 * @return returns the input format
	 */
	public FormatIdentifier getInputType() {
		return input;
	}
	
	/**
	 * Gets the output format for the task group
	 * @return returns the output format
	 */
	public FormatIdentifier getOutputType() {
		return output;
	}
	
	/**
	 * Gets the locale if a locale is required.
	 * @return returns the locale
	 */
	public Optional<String> getLocale() {
		return locale;
	}

	/**
	 * Returns true if the information matches the specified locale.
	 * @param loc the locale to test
	 * @return returns true if the information is a match for the locale, false otherwise
	 */
	public boolean matchesLocale(String loc) {
		return getLocale()==null || (getLocale().isPresent() && getLocale().get().startsWith(loc));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((input == null) ? 0 : input.hashCode());
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result + ((output == null) ? 0 : output.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskSystemInformation other = (TaskSystemInformation) obj;
		if (input == null) {
			if (other.input != null)
				return false;
		} else if (!input.equals(other.input))
			return false;
		if (locale == null) {
			if (other.locale != null)
				return false;
		} else if (!locale.equals(other.locale))
			return false;
		if (output == null) {
			if (other.output != null)
				return false;
		} else if (!output.equals(other.output))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TaskSystemInformation [input=" + input + ", output=" + output + ", locale=" + locale + "]";
	}

}