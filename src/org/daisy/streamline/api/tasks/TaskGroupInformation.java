package org.daisy.streamline.api.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.daisy.streamline.api.option.UserOption;

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
	private final List<UserOption> keys;
	
	/**
	 * Provides a builder for task group information
	 */
	public static class Builder {
		private final String input;
		private final String output;
		private final TaskGroupActivity type;
		// optional
		private String locale = null; 
		private List<UserOption> keys = null;

		/**
		 * Creates a new builder with the specified options
		 * @param input the input format
		 * @param output the output format
		 * @param type the activity type
		 */
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
		 * Sets the required options for the builder.
		 * @param keys the required options
		 * @return returns this builder
		 */
		public Builder setRequiredOptions(List<UserOption> keys) {
			this.keys = keys;
			return this;
		}
		
		/**
		 * Creates a new task group information based on the current state of the builder.
		 * @return returns a new task group information instance
		 */
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
	
	/**
	 * Creates a new builder of convert type with the specified parameters.
	 * @param input the input format
	 * @param output the output format
	 * @return returns a new builder
	 */
	public static Builder newConvertBuilder(String input, String output) {
		return new Builder(input, output, TaskGroupActivity.CONVERT);
	}

	/**
	 * Creates a new builder of enhance type for the specified format.
	 * @param format the format
	 * @return returns a new builder
	 */
	public static Builder newEnhanceBuilder(String format) {
		return new Builder(format, format, TaskGroupActivity.ENHANCE);
	}

/*
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
		this.type = builder.type;
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
	public Collection<UserOption> getRequiredOptions() {
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
		return new TaskGroupSpecification.Builder(getInputFormat(), getOutputFormat(), getLocale(), getActivity());
	}
	
	/**
	 * Creates a new task group specification builder with the current information and for the specified locale.
	 * @param locale the locale
	 * @return returns a new task group specification builder
	 */
	public TaskGroupSpecification.Builder toSpecificationBuilder(String locale) {
		if (locale==null) {
			throw new IllegalArgumentException("Null locale not allowed.");
		} else if (!matchesLocale(locale)) {
			throw new IllegalArgumentException("Argument mismatch: " + getLocale() + " vs " + locale);
		}
		return new TaskGroupSpecification.Builder(getInputFormat(), getOutputFormat(), locale, getActivity());
	}
	
	/**
	 * Returns true if the information matches the specified locale.
	 * @param loc the locale to test
	 * @return returns true if the information is a match for the locale, false otherwise
	 */
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