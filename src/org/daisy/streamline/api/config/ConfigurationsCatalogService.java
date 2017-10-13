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

}
