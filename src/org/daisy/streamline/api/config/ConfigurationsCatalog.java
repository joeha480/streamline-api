package org.daisy.streamline.api.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

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
	private Optional<UserConfigurationsProvider> userConfigurations;
	
	/**
	 * Creates a new empty instance. This method is public because it is required by OSGi.
	 * In an SPI context, use newInstance()
	 */
	public ConfigurationsCatalog() {
		this.map = Collections.synchronizedMap(new HashMap<String, ConfigurationsProvider>());
		providers = new CopyOnWriteArrayList<>();
		this.userConfigurations = Optional.empty();
	}

	/**
	 * <p>
	 * Creates a new instance of a configurations catalog and populates it using the SPI
	 * (java service provider interface).</p>
	 * 
	 * <p>
	 * In an OSGi context, an instance should be retrieved using the service
	 * registry. It will be registered under the ConfigurationsCatalogService
	 * interface.
	 * </p>
	 * 
	 * @return returns an instance of a configurations catalog
	 */
	public static final ConfigurationsCatalog newInstance() {
		ConfigurationsCatalog ret =  new ConfigurationsCatalog();
		for (ConfigurationsProvider provider : ServiceLoader.load(ConfigurationsProvider.class)) {
			provider.setCreatedWithSPI();
			ret.addFactory(provider);
		}
		Iterator<UserConfigurationsProvider> iter = ServiceLoader.load(UserConfigurationsProvider.class).iterator();
		if (iter.hasNext()) {
			UserConfigurationsProvider provider = iter.next();
			provider.setCreatedWithSPI();
			ret.setUserConfigurations(provider);
		}
		return ret;
	}
	
	/**
	 * Adds a factory (intended for use by the OSGi framework)
	 * @param factory the factory to add
	 */
	@Reference(cardinality=ReferenceCardinality.MULTIPLE, policy=ReferencePolicy.DYNAMIC)
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
	
	/**
	 * Sets a provider (intended for use by the OSGi framework)
	 * @param provider the provider to add
	 */
	@Reference(cardinality=ReferenceCardinality.OPTIONAL, policy=ReferencePolicy.DYNAMIC)
	public void setUserConfigurations(UserConfigurationsProvider provider) {
		this.userConfigurations = Optional.of(provider);
	}
	
	/**
	 * Unsets a provider (intended for use by the OSGi framework)
	 * @param provider the factory to provider
	 */
	// Unset reference added automatically from setUserConfigurations annotation
	public void unsetUserConfigurations(UserConfigurationsProvider provider) {
		if (userConfigurations.isPresent() && userConfigurations.get().equals(provider)) {
			this.userConfigurations = Optional.empty();
		}
	}

	@Override
	public Set<ConfigurationDetails> getConfigurationDetails() {
		Set<ConfigurationDetails> keys = new HashSet<>();
		for (ConfigurationsProvider p : providers) {
			keys.addAll(p.getConfigurationDetails());
		}
		userConfigurations.ifPresent(v->keys.addAll(v.getConfigurationDetails()));
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
					for (ConfigurationDetails details : p.getConfigurationDetails()) {
						if (identifier.equals(details.getKey())) {
							if (logger.isLoggable(Level.FINE)) {
								logger.fine("Found a factory for " + identifier + " (" + p.getClass() + ")");
							}
							ConfigurationsProvider o = map.put(details.getKey(), p);
							if (logger.isLoggable(Level.FINE) && o!=null) {
								logger.fine("Configuration with identifier " + details.getKey() + " in " + o.getClass().getCanonicalName()
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
	public Map<String, Object> getConfiguration(String identifier) throws ConfigurationsProviderException {
		if (userConfigurations.isPresent()) {
			Map<String, Object> c = userConfigurations.get().getConfiguration(identifier);
			if (c!=null) {
				return c;
			}
		}
		ConfigurationsProvider provider = assertProvider(identifier);
		if (provider!=null) {
			return provider.getConfiguration(identifier);
		} else {
			throw new ConfigurationsProviderException("Failed to locate resource with identifier: " + identifier);
		}
	}
	
	@Override
	public boolean supportsUserConfigurations() {
		return userConfigurations.isPresent();
	}

	@Override
	public boolean addConfiguration(String niceName, String description, Map<String, Object> config) {
		return userConfigurations.flatMap(v->v.addConfiguration(niceName, description, config)).map(v2->true).orElse(false);
	}
	
	@Override
	public boolean removeConfiguration(String identifier) {
		return userConfigurations.map(v->v.removeConfiguration(identifier)).orElse(false);
	}

	@Override
	public boolean isRemovable(String identifier) {
		return userConfigurations.map(v->v.containsConfiguration(identifier)).orElse(false);
	}

}
