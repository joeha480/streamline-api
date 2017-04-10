package org.daisy.dotify.api.config;

import java.util.Map;
import java.util.Set;

/**
 * Provides an interface for configurations providers. A configurations
 * provider handles a set of configurations, each accessible via a configuration
 * key.
 * @author Joel Håkansson
 *
 */
public interface ConfigurationsProvider {
	
	/**
	 * Gets all configuration keys available in the provider.
	 * @return returns a set of configuration keys.
	 */
	public Set<String> getConfigurationKeys();
	
	/**
	 * Returns the properties associated with the specified by the identifier.
	 * @param identifier the configuration key
	 * @return returns the configuration properties
	 * @throws ConfigurationsProviderException if a configuration cannot be returned.
	 */
	public Map<String, Object> getConfiguration(String identifier) throws ConfigurationsProviderException;
	
	/**
	 * Gets the description for a specified configuration.
	 * @param identifier the configuration key
	 * @return returns the description, or null
	 */
	public String getConfigurationDescription(String identifier);

	/**
	 * <p>Informs the implementation that it was discovered and instantiated using
	 * information collected from a file within the <tt>META-INF/services</tt> directory.
	 * In other words, it was created using SPI (service provider interfaces).</p>
	 * 
	 * <p>This information, in turn, enables the implementation to use the same mechanism
	 * to set dependencies as needed.</p>
	 * 
	 * <p>If this information is <strong>not</strong> given, an implementation
	 * should avoid using SPIs and instead use
	 * <a href="http://wiki.osgi.org/wiki/Declarative_Services">declarative services</a>
	 * for dependency injection as specified by OSGi. Note that this also applies to
	 * several newInstance() methods in the Java API.</p>
	 * 
	 * <p>The class that created an instance with SPI must call this method before
	 * putting it to use.</p>
	 */
	public default void setCreatedWithSPI() {}

}
