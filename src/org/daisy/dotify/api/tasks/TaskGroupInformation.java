package org.daisy.dotify.api.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Provides information about a task group. This information acts as an
 * aid in the process of selecting an implementation, but also as the 
 * key to finding the factory that creates the implementation. To 
 * minimize the possibility of implementations shadowing each other, all 
 * significant information is used to calculate equals/hashcode.
 *   
 * @author Joel Håkansson
 *
 */
public class TaskGroupInformation {

	private final String input;
	private final String output;
	private final String locale;
	private final TaskGroupActivity type;
	private final List<TaskOption> keys;
	
	public static class Builder {
		private final String input;
		private final String output;
		private final TaskGroupActivity type;
		// optional
		private String locale = null; 
		private List<TaskOption> keys = null;

		public Builder(String input, String output, TaskGroupActivity type) {
			Objects.requireNonNull(input, "Input format cannot be null.");
			Objects.requireNonNull(output, "Output format cannot be null.");
			Objects.requireNonNull(type, "Type cannot be null.");
			switch (type) {
			case CONVERT:
				if (input.equals(output)) {
					throw new IllegalArgumentException("Convert: Input and output formats must not be equal.");
				}
				break;
			case ENHANCE:
				if (!input.equals(output)) {
					throw new IllegalArgumentException("Enhance: Input and output formats must be equal.");
				}
				break;
			default:
			}
			
			this.input = input;
			this.output = output;
			this.type = type;
		}

		public Builder locale(String value) {
			this.locale = value;
			return this;
		}
		
		public Builder setRequiredOptions(List<TaskOption> keys) {
			this.keys = keys;
			return this;
		}
		
		public TaskGroupInformation build() {
			return new TaskGroupInformation(this);
		}
	}
	
	/**
	 * Creates a new builder with the same settings as this information.
	 * @return returns a new builder
	 */
	public Builder newCopyBuilder() {
		return new Builder(getInputFormat(), getOutputFormat(), getActivity()).locale(getLocale()).setRequiredOptions(keys);
	}
	
	public static Builder newConvertBuilder(String input, String output) {
		return new Builder(input, output, TaskGroupActivity.CONVERT);
	}

	public static Builder newEnhanceBuilder(String format) {
		return new Builder(format, format, TaskGroupActivity.ENHANCE);
	}

/*
	public static Builder newIdentifyBuilder(String output) {
		return new Builder(null, output, null);
	}
	
	public static Builder newIdentifyBuilder(String input, String output) {
		return new Builder(input, output, null);
	}
	
	public static Builder newValidateBuilder(String format) {
		return new Builder(format, format, null);
	}
	
	public static Builder newFixBuilder(String format) {
		return new Builder(format, format, null);
	}
*/
	private TaskGroupInformation(Builder builder) {
		this.input = builder.input;
		this.output = builder.output;
		if (input.equals(output)) {
			this.type = TaskGroupActivity.ENHANCE;
		} else {
			this.type = TaskGroupActivity.CONVERT;
		}
		this.locale = builder.locale;
		if (builder.keys==null) {
			this.keys = Collections.emptyList(); 
		} else {
			this.keys = new ArrayList<>(builder.keys);
		}
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
	 * Gets the locale if a locale is specified, null otherwise.
	 * @return returns the locale, or null
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * Gets the task group activity type
	 * @return the activity type of the task group
	 */
	public TaskGroupActivity getActivity() {
		return type;
	}
	
	/**
	 * Returns a collection of options that <b>must</b> be provided when compiling the task group.
	 * @return returns the collection of options that must be provided
	 */
	public Collection<TaskOption> getRequiredOptions() {
		return keys;
	}
	
	/**
	 * Provides a task group specification builder.
	 * @return returns a builder for task group specifications
	 */
	public TaskGroupSpecification.Builder toSpecificationBuilder() {
		if (getLocale()==null) {
			throw new IllegalArgumentException("No locale.");
		}
		return new TaskGroupSpecification.Builder(getInputFormat(), getOutputFormat(), getLocale());
	}
	
	public TaskGroupSpecification.Builder toSpecificationBuilder(String locale) {
		if (locale==null) {
			throw new IllegalArgumentException("Null locale not allowed.");
		} else if (!matchesLocale(locale)) {
			throw new IllegalArgumentException("Argument mismatch: " + getLocale() + " vs " + locale);
		}
		return new TaskGroupSpecification.Builder(getInputFormat(), getOutputFormat(), locale);
	}
	
	public boolean matchesLocale(String loc) {
		return getLocale()==null || getLocale().startsWith(loc);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((input == null) ? 0 : input.hashCode());
		result = prime * result + ((keys == null) ? 0 : keys.hashCode());
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result + ((output == null) ? 0 : output.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		TaskGroupInformation other = (TaskGroupInformation) obj;
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
		if (type != other.type) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "TaskGroupInformation [input=" + input + ", output=" + output + ", locale=" + locale + ", type=" + type
				+ ", keys=" + keys + "]";
	}

}