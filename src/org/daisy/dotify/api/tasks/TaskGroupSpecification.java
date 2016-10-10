package org.daisy.dotify.api.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Provides a specification for a task group.
 * @author Joel Håkansson
 *
 */
public final class TaskGroupSpecification {
	/**
	 * Specifies the type of task group
	 */
	public enum Type {
		 // Identify, Verify, Fix

		/**
		 * An enhancing task group
		 */
		ENHANCE,
		/**
		 * A converting task group
		 */
		CONVERT
	}

	private final String input;
	private final String output;
	private final String locale;
	private final Collection<TaskOption> keys;
	
	public static class Builder {
		private final String input;
		private final String output;
		private final String locale;
		private Collection<TaskOption> options;
		
		public Builder(String input, String output, String locale) {
			this.input = input;
			this.output = output;
			this.locale = locale;
			this.options = new ArrayList<>();
		}
		
		public Builder addRequired(TaskOption value) {
			options.add(value);
			return this;
		}
		
		public TaskGroupSpecification build() {
			return new TaskGroupSpecification(this);
		}
	}
	
	public TaskGroupSpecification(String input, String output, String locale) {
		this.input = input;
		this.output = output;
		this.locale = locale;
		this.keys = Collections.emptySet();
	}
	
	private TaskGroupSpecification(Builder builder) {
		this.input = builder.input;
		this.output = builder.output;
		this.locale = builder.locale;
		this.keys = Collections.unmodifiableCollection(new ArrayList<>(builder.options));
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
	 * Gets the type of task group specification
	 * @return the type of the task group specification
	 */
	public Type getType() {
		if (input.equals(output)) {
			return Type.ENHANCE;
		} else {
			return Type.CONVERT;
		}
	}
	
	/**
	 * Returns a collection of options that <b>must</b> be provided when compiling the task group.
	 * @return returns the collection of options that must be provided
	 */
	public Collection<TaskOption> getRequiredOptions() {
		return keys;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((input == null) ? 0 : input.hashCode());
		result = prime * result + ((keys == null) ? 0 : keys.hashCode());
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
		if (input == null) {
			if (other.input != null) {
				return false;
			}
		} else if (!input.equals(other.input)) {
			return false;
		}
		if (keys == null) {
			if (other.keys != null) {
				return false;
			}
		} else if (!keys.equals(other.keys)) {
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
