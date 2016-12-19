package org.daisy.dotify.api.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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
	
	public static class Builder {
		private final String input;
		private final String output;
		private final String locale;
		
		public Builder(String input, String output, String locale) {
			this.input = input;
			this.output = output;
			this.locale = locale;
		}
		
		public TaskGroupSpecification build() {
			return new TaskGroupSpecification(this);
		}
	}
	
	public TaskGroupSpecification(String input, String output, String locale) {
		this.input = input;
		this.output = output;
		this.locale = locale;
		this.activity = input.equals(output)?TaskGroupActivity.ENHANCE:TaskGroupActivity.CONVERT;
	}
	
	private TaskGroupSpecification(Builder builder) {
		this.input = builder.input;
		this.output = builder.output;
		this.locale = builder.locale;
		this.activity = input.equals(output)?TaskGroupActivity.ENHANCE:TaskGroupActivity.CONVERT;
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
