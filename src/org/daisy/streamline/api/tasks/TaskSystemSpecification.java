package org.daisy.streamline.api.tasks;

import org.daisy.streamline.api.media.FormatIdentifier;

/**
 * Provides a specification for creating a task system.
 * @author Joel Håkansson
 */
public final class TaskSystemSpecification {
	private final FormatIdentifier input;
	private final FormatIdentifier output;
	private final String locale;
	
	/**
	 * Provides a task group specification builder.
	 */
	public static class Builder {
		private final FormatIdentifier input;
		private final FormatIdentifier output;
		private final String locale;

		/**
		 * Creates a new builder with the specified parameters.
		 * @param input the input format
		 * @param output the output format
		 * @param locale the locale
		 */
		public Builder(FormatIdentifier input, FormatIdentifier output, String locale) {
			this.input = input;
			this.output = output;
			this.locale = locale;
		}
		
		/**
		 * Creates a new task group specification using the current state of the builder.
		 * @return returns a new task group specification
		 */
		public TaskSystemSpecification build() {
			return new TaskSystemSpecification(this);
		}
	}
	
	private TaskSystemSpecification(Builder builder) {
		this.input = builder.input;
		this.output = builder.output;
		this.locale = builder.locale;
	}
	
	/**
	 * Gets the locale for the task group
	 * @return returns the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * Gets the input format for the task group
	 * @return returns the input format
	 */
	public FormatIdentifier getInputFormat() {
		return input;
	}
	
	/**
	 * Gets the output format for the task group
	 * @return returns the output format
	 */
	public FormatIdentifier getOutputFormat() {
		return output;
	}
	
	/**
	 * Returns true if this specification matches the specified
	 * task system information.
	 * @param info the information to test
	 * @return returns true if the specification matches
	 */
	public boolean matches(TaskSystemInformation info) {
		return	getInputFormat().equals(info.getInputType()) &&
				getOutputFormat().equals(info.getOutputType()) &&
				info.matchesLocale(getLocale());
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
		TaskSystemSpecification other = (TaskSystemSpecification) obj;
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
		return "TaskSystemSpecification [input=" + input + ", output=" + output + ", locale=" + locale + "]";
	}

}
