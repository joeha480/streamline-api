package org.daisy.streamline.api.option;

/**
 * Provides a value and the description of that value, to be used
 * in a finite list of acceptable values for a task option.
 * 
 * @author Joel Håkansson
 *
 */
public final class TaskOptionValue {
	private final String 	name,
							description;
	
	/**
	 * Provides a task option value builder.
	 */
	public static class Builder {
		private final String name;
		private String description = "";
		
		/**
		 * Creates a new builder with the specified option value name 
		 * @param name the option value's name
		 */
		public Builder(String name) {
			this.name = name;
		}
		
		/**
		 * Sets the value's description.
		 * @param value the description
		 * @return returns this builder
		 */
		public Builder description(String value) {
			this.description = value;
			return this;
		}
		
		/**
		 * Creates a new task option value based on the current state of the builder.
		 * @return returns a new task option value
		 */
		public TaskOptionValue build() {
			return new TaskOptionValue(this);
		}
	}
	
	/**
	 * Creates a new task option value with the specified
	 * name.
	 * @param name the name of the value
	 * @return returns a new builder
	 */
	public static TaskOptionValue.Builder withName(String name) {
		return new TaskOptionValue.Builder(name);
	}

	private TaskOptionValue(Builder builder) {
		this.name = builder.name;
		this.description = builder.description;
	}

	/**
	 * Gets the name for the option value. Note that the name should be 
	 * a unique <b>value</b> in the list of values for the option. It is
	 * <b>NOT</b> the option's key.
	 * @return returns the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the description of the option.
	 * @return returns the description
	 */
	public String getDescription() {
		return description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		TaskOptionValue other = (TaskOptionValue) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}
