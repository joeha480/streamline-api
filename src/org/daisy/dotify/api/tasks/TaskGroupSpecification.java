package org.daisy.dotify.api.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
	private final Collection<String> keys;
	private final Map<String, String> keyValues;
	
	public static class Builder {
		private final String input;
		private final String output;
		private final String locale;
		private Collection<String> keys;
		private Map<String, String> keyValues;
		
		public Builder(String input, String output, String locale) {
			this.input = input;
			this.output = output;
			this.locale = locale;
			this.keys = new ArrayList<>();
			this.keyValues = new HashMap<>();
		}
		
		public Builder addRequired(String key) {
			keys.add(key);
			return this;
		}
		
		public Builder addRequired(String key, String value) {
			keyValues.put(key, value);
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
		this.keyValues = Collections.emptyMap();
	}
	
	private TaskGroupSpecification(Builder builder) {
		this.input = builder.input;
		this.output = builder.output;
		this.locale = builder.locale;
		this.keys = Collections.unmodifiableCollection(new ArrayList<>(builder.keys));
		this.keyValues = Collections.unmodifiableMap(new HashMap<>(builder.keyValues));
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
	 * Returns a collection of keys that <b>must</b> be provided when compiling the task group.
	 * The collection returned  should be specified in the returned task group's options.
	 * @return returns the collection of keys that must be provided
	 */
	public Collection<String> requiresKeys() {
		return keys;
	}
	
	/**
	 * Returns a map of key/value combinations that <b>must</b> be provided when compiling the task group.
	 * The map returned should be specified in the returned task group's options.
	 * @return returns a map of key/value combinations that must be provided
	 */
	public Map<String, String> requiresKeyValue() {
		return keyValues;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((input == null) ? 0 : input.hashCode());
		result = prime * result + ((keyValues == null) ? 0 : keyValues.hashCode());
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
		if (keyValues == null) {
			if (other.keyValues != null) {
				return false;
			}
		} else if (!keyValues.equals(other.keyValues)) {
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
