package org.daisy.streamline.api.option;

import java.util.Optional;

/**
 * Provides a value and the description of that value, to be used
 * in a finite list of acceptable values for a user option.
 * 
 * @author Joel Håkansson
 *
 */
public final class UserOptionValue {
	private final String 	name,
							displayName,
							description;
	
	/**
	 * Provides a user option value builder.
	 */
	public static class Builder {
		private final String name;
		private String displayName = null;
		private String description = "";
		
		/**
		 * Creates a new builder with the specified option value name.
		 * @param name the option value's name
		 */
		public Builder(String name) {
			this.name = name;
		}
		
		/**
		 * Sets the value's display name.
		 * @param value the display name
		 * @return this builder
		 */
		public Builder displayName(String value) {
			this.displayName = value;
			return this;
		}
		
		/**
		 * Sets the value's description.
		 * @param value the description
		 * @return this builder
		 */
		public Builder description(String value) {
			this.description = value;
			return this;
		}
		
		/**
		 * Creates a new user option value based on the current state of the builder.
		 * @return a new user option value
		 */
		public UserOptionValue build() {
			return new UserOptionValue(this);
		}
	}
	
	/**
	 * Creates a new user option value with the specified
	 * name.
	 * @param name the name of the value
	 * @return a new builder
	 */
	public static UserOptionValue.Builder withName(String name) {
		return new UserOptionValue.Builder(name);
	}

	private UserOptionValue(Builder builder) {
		this.name = builder.name;
		this.displayName = Optional.ofNullable(builder.displayName).orElse(name);
		this.description = builder.description;
	}

	/**
	 * Gets the name for the option value. Note that the name should be 
	 * a unique <b>value</b> in the list of values for the option. It is
	 * <b>NOT</b> the option's key.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the display name for the option value.
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Gets the description of the option.
	 * @return the description
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
		UserOptionValue other = (UserOptionValue) obj;
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
