package org.daisy.dotify.consumer.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.daisy.dotify.api.config.ConfigurationsCatalogService;
import org.daisy.dotify.api.config.ConfigurationsProvider;
import org.daisy.dotify.api.config.ConfigurationsProviderException;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

/**
 * Provides a catalog of configurations available to the input system. The catalog 
 * implementation can be overridden if a different behavior is desired. In most cases, 
 * the default configuration catalog should suffice. To override this class, extend it
 * and add a reference to the implementation to the java service registry.
 * 
 * Provides a default configurations catalog. The default configurations catalog
 * will scan the service registry for configurations providers and collect the keys
 * of every provider. If more than one provider contains the same key, the 
 * most recently added will be used, and a debug message will be sent to the log.
 * 
 * @author Joel Håkansson
 */
@Component
public class ConfigurationsCatalog implements ConfigurationsCatalogService {
	private static final Logger logger = Logger.getLogger(ConfigurationsCatalog.class.getCanonicalName());
	private final Map<String, ConfigurationsProvider> map;
	private final List<ConfigurationsProvider> providers;
	
	/**
	 * Creates a new empty instance. This method is public because it is required by OSGi.
	 * In an SPI context, use newInstance()
	 */
	public ConfigurationsCatalog() {
		this.map = Collections.synchronizedMap(new HashMap<String, ConfigurationsProvider>());
		providers = new CopyOnWriteArrayList<>();
	}

	/**
	 * <p>
	 * Creates a new instance of a configurations catalog and populates it using the SPI
	 * (java service provider interface).</p>
	 * 
	 * <p>
	 * In an OSGi context, an instance should be retrieved using the service
	 * registry. It will be registered under the PaperCatalogService
	 * interface.
	 * </p>
	 * 
	 * @return returns an instance of a configurations catalog
	 */
	public static final ConfigurationsCatalog newInstance() {
		ConfigurationsCatalog ret =  new ConfigurationsCatalog();
		Iterator<ConfigurationsProvider> i = ServiceLoader.load(ConfigurationsProvider.class).iterator();
		while (i.hasNext()) {
			ConfigurationsProvider provider = i.next();
			provider.setCreatedWithSPI();
			ret.addFactory(provider);
		}
		return ret;
	}
	
	/**
	 * Adds a factory (intended for use by the OSGi framework)
	 * @param factory the factory to add
	 */
	@Reference(type = '*')
	public void addFactory(ConfigurationsProvider factory) {
		if (logger.isLoggable(Level.FINER)) {
			logger.finer("Adding factory: " + factory);
		}
		providers.add(factory);
	}

	/**
	 * Removes a factory (intended for use by the OSGi framework)
	 * @param factory the factory to remove
	 */
	// Unbind reference added automatically from addFactory annotation
	public void removeFactory(ConfigurationsProvider factory) {
		if (logger.isLoggable(Level.FINER)) {
			logger.finer("Removing factory: " + factory);
		}
		// this is to avoid adding items to the cache that were removed while
		// iterating
		synchronized (map) {
			providers.remove(factory);
			map.clear();
		}
	}
	


	@Override
	public Set<String> getKeys() {
		Set<String> keys = new HashSet<>();
		for (ConfigurationsProvider p : providers) {
			keys.addAll(p.getConfigurationKeys());
		}
		return keys;
	}
	
	private ConfigurationsProvider assertProvider(String identifier) {
		if (identifier==null) {
			return null;
		}
		ConfigurationsProvider provider = map.get(identifier);
		if (provider==null) {
			// this is to avoid adding items to the cache that were removed
			// while iterating
			synchronized (map) {
				for (ConfigurationsProvider p : providers) {
					for (String key : p.getConfigurationKeys()) {
						if (identifier.equals(key)) {
							if (logger.isLoggable(Level.FINE)) {
								logger.fine("Found a factory for " + identifier + " (" + p.getClass() + ")");
							}
							ConfigurationsProvider o = map.put(key, p);
							if (logger.isLoggable(Level.FINE) && o!=null) {
								logger.fine("Configuration with identifier " + key + " in " + o.getClass().getCanonicalName()
										+ " replaced by configuration in " + p.getClass().getCanonicalName());
							}
							provider = p;
							break;
						}
					}
				}
			}
		}
		return provider;
	}

	@Override
	public Properties getConfiguration(String identifier) throws ConfigurationsProviderException {
		ConfigurationsProvider provider = assertProvider(identifier);
		if (provider!=null) {
			return provider.getConfiguration(identifier);
		} else {
			throw new ConfigurationsProviderException("Failed to locate resource with identifier: " + identifier);
		}
	}

	@Override
	public String getConfigurationDescription(String identifier) {
		ConfigurationsProvider provider = assertProvider(identifier);
		if (provider!=null) {
			return provider.getConfigurationDescription(identifier);
		} else {
			return "";
		}
	}

}
