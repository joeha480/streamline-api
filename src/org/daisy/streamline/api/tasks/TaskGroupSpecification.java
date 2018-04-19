package org.daisy.streamline.api.tasks;

import java.util.Objects;

/**
 * Provides a specification for creating a task group instance.
 * @author Joel Håkansson
 *
 */
public final class TaskGroupSpecification {

	private final String input;
	private final String output;
	private final String locale;
	private final TaskGroupActivity activity;
	
	/**
	 * Provides a task group specification builder.
	 */
	public static class Builder {
		private final String input;
		private final String output;
		private final String locale;
		private final TaskGroupActivity activity;
		
		/**
		 * Creates a new builder with the specified parameters.
		 * @param input the input format
		 * @param output the output format
		 * @param locale the locale
		 */
		public Builder(String input, String output, String locale) {
			this(input, output, locale, detectActivity(input, output));
		}

		/**
		 * Creates a new builder with the specified parameters.
		 * @param input the input format
		 * @param output the output format
		 * @param locale the locale
		 * @param activity the activity type
		 */
		public Builder(String input, String output, String locale, TaskGroupActivity activity) {
			this.input = input;
			this.output = output;
			this.locale = locale;
			this.activity = activity;
		}
		
		/**
		 * Creates a new task group specification using the current state of the builder.
		 * @return returns a new task group specification
		 */
		public TaskGroupSpecification build() {
			return new TaskGroupSpecification(this);
		}
	}
	
	/**
	 * Creates a new task group specification with the specified parameters.
	 * @param input the input format
	 * @param output the output format
	 * @param locale the locale
	 */
	public TaskGroupSpecification(String input, String output, String locale) {
		this(input, output, locale, detectActivity(input, output));
	}

	/**
	 * Creates a new task group specification with the specified parameters.
	 * @param input the input format
	 * @param output the output format
	 * @param locale the locale
	 * @param activity the activity type
	 */	
	public TaskGroupSpecification(String input, String output, String locale, TaskGroupActivity activity) {
		this.input = input;
		this.output = output;
		this.locale = locale;
		this.activity = activity;
	}
	
	private TaskGroupSpecification(Builder builder) {
		this.input = builder.input;
		this.output = builder.output;
		this.locale = builder.locale;
		this.activity = builder.activity;
	}
	
	/**
	 * Provides a task group specification builder.
	 * @param info the task group information
	 * @return returns a builder for task group specifications
	 * @throws NullPointerException if the task group information's locale is null
	 */
	public static TaskGroupSpecification.Builder with(TaskGroupInformation info) {
		return new TaskGroupSpecification.Builder(info.getInputFormat(), info.getOutputFormat(), Objects.requireNonNull(info.getLocale()), info.getActivity());
	}
	
	/**
	 * Creates a new task group specification builder with the current information and for the specified locale.
	 * @param info the task group information
	 * @param locale the locale
	 * @return returns a new task group specification builder
	 * @throws NullPointerException if locale is null
	 * @throws IllegalArgumentException if the locale specified in the task group information doesn't match the
	 * 			specified locale
	 */
	public static TaskGroupSpecification.Builder with(TaskGroupInformation info, String locale) {
		Objects.requireNonNull(locale);
		if (!info.matchesLocale(locale)) {
			throw new IllegalArgumentException("Argument mismatch: " + info.getLocale() + " vs " + locale);
		}
		return new TaskGroupSpecification.Builder(info.getInputFormat(), info.getOutputFormat(), locale, info.getActivity());
	}

	private static TaskGroupActivity detectActivity(String input, String output) {
		return input.equals(output)?TaskGroupActivity.ENHANCE:TaskGroupActivity.CONVERT;
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
	public String getInputFormat() {
		return input;
	}
	
	/**
	 * Gets the output format for the task group
	 * @return returns the output format
	 */
	public String getOutputFormat() {
		return output;
	}
	
	/**
	 * Gets the type of activity requested.
	 * @return returns the activity type
	 */
	public TaskGroupActivity getActivity() {
		return activity; 
	}
	
	/**
	 * Returns true if this specification matches the specified
	 * task group information.
	 * @param info the information to test
	 * @return returns true if the specification matches
	 */
	public boolean matches(TaskGroupInformation info) {
		return	getActivity().equals(info.getActivity()) &&
				getInputFormat().equals(info.getInputFormat()) &&
				getOutputFormat().equals(info.getOutputFormat()) &&
				info.matchesLocale(getLocale());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activity == null) ? 0 : activity.hashCode());
		result = prime * result + ((input == null) ? 0 : input.hashCode());
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result + ((output == null) ? 0 : output.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TaskGroupSpecification other = (TaskGroupSpecification) obj;
		if (activity != other.activity) {
			return false;
		}
		if (input == null) {
			if (other.input != null) {
				return false;
			}
		} else if (!input.equals(other.input)) {
			return false;
		}
		if (locale == null) {
			if (other.locale != null) {
				return false;
			}
		} else if (!locale.equals(other.locale)) {
			return false;
		}
		if (output == null) {
			if (other.output != null) {
				return false;
			}
		} else if (!output.equals(other.output)) {
			return false;
		}
		return true;
	}

}
