package org.daisy.streamline.api.config;

import java.io.Serializable;
import java.util.Objects;

/**
 * Provides details about a configuration. Two configurations are considered equal if
 * they have the same key.
 * @author Joel Håkansson
 */
public class ConfigurationDetails implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6169245964235216805L;
	private final String key;
	private final String description;
	private final String niceName;

	
	/**
	 * Provides a configuration details builder.
	 * @author Joel Håkansson
	 */
	public static class Builder {
		private String key = "";
		private String description = "";
		private String niceName = "";

		/**
		 * Creates a new builder with the specified identifier.
		 * @param key the identifier
		 * @throws NullPointerException if key is null
		 */
		public Builder(String key) {
			Objects.requireNonNull(key);
			this.key = key;
			this.niceName = key;
		}
		
		/**
		 * Creates a new builder with the specified details as a template.
		 * @param template the template
		 */
		public Builder(ConfigurationDetails template) {
			this.key = template.key;
			this.description = template.description;
			this.niceName = template.niceName;
		}
		
		/**
		 * Sets the identifier
		 * @param value the identifier
		 * @return returns this builder
		 */
		public Builder identifier(String value) {
			Objects.requireNonNull(value);
			this.key = value;
			return this;
		}
		
		/**
		 * Sets a description for the configuration.
		 * @param value the value
		 * @return returns this builder
		 */
		public Builder description(String value) {
			this.description = value;
			return this;
		}
		
		/**
		 * Sets a nice name to use instead of key when presented to a user.
		 * 
		 * @param value the value
		 * @return returns this builder
		 */
		public Builder niceName(String value) {
			this.niceName = value;
			return this;
		}
		
		/**
		 * Creates new configuration details based on the current state of the builder. 
		 * @return returns a new {@link ConfigurationDetails} instance
		 */
		public ConfigurationDetails build() {
			return new ConfigurationDetails(this);
		}
	}
	

	private ConfigurationDetails(Builder builder) {
		this.key = builder.key;
		this.description = builder.description;
		this.niceName = builder.niceName;
	}
	
	/**
	 * Creates a new builder based on this instance.
	 * @return returns a new builder
	 */
	public ConfigurationDetails.Builder builder() {
		return new Builder(this);
	}

	/**
	 * Gets the key for this configuration.
	 * @return returns the configuration key
	 */
	public String getKey() {
		return key;
	}


	/**
	 * Gets the description for this configuration.
	 * @return returns the configuration description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * Gets a nice name for this configuration for use when presenting to a user.
	 * @return returns the nice name
	 */
	public String getNiceName() {
		return niceName;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
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
		ConfigurationDetails other = (ConfigurationDetails) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

}
