package org.daisy.streamline.api.identity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.daisy.streamline.api.media.AnnotatedFile;
import org.daisy.streamline.api.media.AnnotatedInputStream;
import org.daisy.streamline.api.media.DefaultAnnotatedFile;
import org.daisy.streamline.api.media.DefaultAnnotatedInputStream;
import org.daisy.streamline.api.media.InputStreamSupplier;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * Provides file identification based on its contents.
 * 
 * @author Joel Håkansson
 */
@Component
public class IdentityProvider implements IdentityProviderService {
	private final List<IdentifierFactory> filters;
	private static final Logger logger = Logger.getLogger(IdentityProvider.class.getCanonicalName());
	
	/**
	 * Creates a new empty identity provider.
	 */
	public IdentityProvider() {
		this.filters = new CopyOnWriteArrayList<>();
	}
	
	/**
	 * <p>
	 * Creates a new IdentityProviderService and populates it using the SPI (java
	 * service provider interface).
	 * </p>
	 * 
	 * <p>
	 * In an OSGi context, an instance should be retrieved using the service
	 * registry. It will be registered under the IdentityProviderService
	 * interface.
	 * </p>
	 * 
	 * @return returns a new IdentityProviderService
	 */
	public static final IdentityProviderService newInstance() {
		IdentityProvider ret = new IdentityProvider();
		for (IdentifierFactory factory : ServiceLoader.load(IdentifierFactory.class)) {
			factory.setCreatedWithSPI();
			ret.addFactory(factory);
		}
		return ret;
	}
	
	/**
	 * Adds a factory (intended for use by the OSGi framework)
	 * @param factory the factory to add
	 */
	@Reference(cardinality=ReferenceCardinality.MULTIPLE, policy=ReferencePolicy.DYNAMIC)
	public void addFactory(IdentifierFactory factory) {
		if (logger.isLoggable(Level.FINER)) {
			logger.finer("Adding factory: " + factory);
		}
		filters.add(factory);
	}

	/**
	 * Removes a factory (intended for use by the OSGi framework)
	 * @param factory the factory to remove
	 */
	// Unbind reference added automatically from addFactory annotation
	public void removeFactory(IdentifierFactory factory) {
		if (logger.isLoggable(Level.FINER)) {
			logger.finer("Removing factory: " + factory);
		}
		filters.remove(factory);
	}

	@Override
	public AnnotatedFile identify(File in) {
		AnnotatedFile f = DefaultAnnotatedFile.create(in.toPath());

		// get a list of factories
		List<IdentifierFactory> factories = new ArrayList<>(filters);		
		while (!factories.isEmpty()) {
			try {
				f = identify(f, factories);
			} catch (IdentificationFailedException e) {
				if (logger.isLoggable(Level.FINE)) {
					logger.log(Level.FINE, "No matching identifier factories.", e);
				}
				break;
			}
		}
		return f;
	}
	
	@Override
	public AnnotatedInputStream identify(InputStreamSupplier in) {
		AnnotatedInputStream stream = DefaultAnnotatedInputStream.create(in);

		// get a list of factories
		List<IdentifierFactory> factories = new ArrayList<>(filters);
		while (!factories.isEmpty()) {
			try {
				stream = identify(stream, factories);
			} catch (IdentificationFailedException e) {
				if (logger.isLoggable(Level.FINE)) {
					logger.log(Level.FINE, "No matching identifier factories.", e);
				}
				break;
			}
		}
		return stream;
	}

	private AnnotatedFile identify(AnnotatedFile f, List<IdentifierFactory> factories) throws IdentificationFailedException {
		IdentificationFailedException ex = new IdentificationFailedException();
		for (IdentifierFactory id : factories) {
			if (id.accepts(f)) {
				try {
					AnnotatedFile x = id.newIdentifier().identify(f);
					// identification was successful, remove this from future iterations
					factories.remove(id);
					return x;
				} catch (IdentificationFailedException e) {
					ex.addSuppressed(e);
				}
			}
		}
		throw ex;
	}

	private AnnotatedInputStream identify(AnnotatedInputStream stream, List<IdentifierFactory> factories) throws IdentificationFailedException {
		IdentificationFailedException ex = new IdentificationFailedException();
		for (IdentifierFactory id : factories) {
			if (id.accepts(stream)) {
				try {
					AnnotatedInputStream ret = id.newIdentifier().identify(stream);
					// identification was successful, remove this from future iterations
					factories.remove(id);
					return ret;
				} catch (IdentificationFailedException e) {
					ex.addSuppressed(e);
				}
			}
		}
		throw ex;
	}

}