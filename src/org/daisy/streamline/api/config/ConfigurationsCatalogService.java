package org.daisy.streamline.api.config;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Provides an interface for a ConfigurationsCatalog service. The purpose of
 * this interface is to expose an implementation of a ConfigurationsCatalog
 * as an OSGi service.
 * </p>
 * 
 * <p>
 * To comply with this interface, an implementation must be thread safe and
 * address both the possibility that only a single instance is created and used
 * throughout and that new instances are created as desired.
 * </p>
 * 
 * @author Joel Håkansson
 */
public interface ConfigurationsCatalogService {

	
	/**
	 * Gets all configuration details available to the configurations catalog.
	 * @return returns a set of configuration details.
	 */
	public Set<ConfigurationDetails> getConfigurationDetails();
	
	/**
	 * Returns configuration properties associated with the identifier.
	 * @param identifier the configuration key
	 * @return returns properties for the configuration
	 * @throws ConfigurationsProviderException if identifier is unknown.
	 */
	public Map<String, Object> getConfiguration(String identifier) throws ConfigurationsProviderException;
	
	/**
	 * Returns true if user configurations are supported, false otherwise. If this method
	 * returns false, {@link #addConfiguration(String, String, Map)}, {@link #isRemovable(String)},
	 * {@link #removeConfiguration(String)} should not be used.
	 * 
	 * @return true if user configurations are supported, false otherwise
	 */
	public boolean supportsUserConfigurations();
	
	/**
	 * Adds a configuration to this catalog.
	 * @param niceName the display name
	 * @param description the configuration description
	 * @param config the configuration details
	 * @return true if the configuration was successfully added, false otherwise
	 */
	public boolean addConfiguration(String niceName, String description, Map<String, Object> config);
	
	/**
	 * Returns true if the configuration with the specified identifier can be removed.
	 * @param identifier the identifier
	 * @return true if the configuration can be removed, false otherwise
	 */
	public boolean isRemovable(String identifier);
	
	/**
	 * Removes the configuration with the specified identifier.
	 * @param identifier the identifier
	 * @return true if the configuration was successfully removed, false otherwise
	 */
	public boolean removeConfiguration(String identifier);

}
