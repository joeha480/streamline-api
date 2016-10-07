package org.daisy.dotify.api.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Provides a task option.
 * @author Joel Håkansson
 *
 */
public final class TaskOption {
	private final List<TaskOptionValue> values;
	private final String 	key,
							description,
							defaultValue;
	
	private Set<String> valuesSet = null;
	
	public static class Builder {
		private final String key;
		private String description = "";
		private String defaultValue = "";
		private List<TaskOptionValue> values;
		
		public Builder(String key) {
			this.key = key;
			this.values = new ArrayList<TaskOptionValue>();
		}
		
		public Builder description(String value) {
			this.description = value;
			return this;
		}
		
		public Builder defaultValue(String value) {
			this.defaultValue = value;
			return this;
		}
		
		public Builder addValue(TaskOptionValue value) {
			values.add(value);
			return this;
		}
		
		public TaskOption build() {
			return new TaskOption(this);
		}
	}
	
	/**
	 * Creates a new task option with the specified key.
	 * @param key the key
	 * @return returns a new builder
	 */
	public static TaskOption.Builder withKey(String key) {
		return new TaskOption.Builder(key);
	}

	private TaskOption(Builder builder) {
		this.key = builder.key;
		this.description = builder.description;
		this.defaultValue = builder.defaultValue;
		if (builder.values.size()>0) {
			TaskOptionValue[] v = builder.values.toArray(new TaskOptionValue[builder.values.size()]);
			this.values = Collections.unmodifiableList(Arrays.asList(v));
		} else {
			this.values = null;
		}
	}

	/**
	 * Gets the key for the option
	 * @return returns the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Gets the description of the option.
	 * @return returns the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the default value for the option, if not set.
	 * @return returns the default value
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	
	/**
	 * Returns true if this argument has a finite list of acceptable values.
	 * @return returns true if a finite list of acceptable values exist, false otherwise
	 */
	public boolean hasValues() {
		return values!=null && values.size()>0;
	}
	
	/**
	 * Gets the list of acceptable values.
	 * @return returns the list of acceptable values, or null if the list of possible values 
	 * is infinite
	 */
	public List<TaskOptionValue> getValues() {
		return values;
	}
	
	/**
	 * Returns true if this option accepts the specified value
	 * @param value the value to test
	 * @return true if the option accepts the value, false otherwise
	 */
	public boolean acceptsValue(String value) {
		if (value==null) {
			return false;
		} else if (hasValues()) {
			return getValuesList().contains(value);
		} else {
			return true;
		}
	}
	
	private Set<String> getValuesList() {
		if (hasValues() && valuesSet==null) {
			valuesSet = new HashSet<>();
			for (TaskOptionValue val : getValues()) {
				valuesSet.add(val.getName());
			}
		}
		return valuesSet;
	}

}
