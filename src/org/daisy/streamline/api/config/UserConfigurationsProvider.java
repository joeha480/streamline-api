package org.daisy.streamline.api.config;

import java.util.Map;
import java.util.Optional;

/**
 * Provides an interface for user configurations providers. A user configurations
 * provider handles a set of configurations, each accessible via a configuration
 * key. In addition, configurations can be added and removed.
 * @author Joel Håkansson
 *
 */
public interface UserConfigurationsProvider extends ConfigurationsProvider {

	/**
	 * Adds a configuration to this catalog.
	 * @param niceName the display name
	 * @param description the configuration description
	 * @param config the configuration details
	 * @return the identifier for the new configuration
	 */
	public Optional<String> addConfiguration(String niceName, String description, Map<String, Object> config);
	
	/**
	 * Removes the configuration with the specified identifier.
	 * @param identifier the identifier
	 * @return true if the configuration was successfully removed, false otherwise
	 */
	public boolean removeConfiguration(String identifier);
	
	/**
	 * Returns true if the provider contains the specified key.
	 * @param identifier the key
	 * @return true if the provider contains the key, false otherwise
	 */
	public boolean containsConfiguration(String identifier);

}
