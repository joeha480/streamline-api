package org.daisy.streamline.api.details;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.daisy.streamline.api.media.FormatIdentifier;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component
public class FormatDetailsProvider implements FormatDetailsProviderService {
	private static final Logger LOGGER = Logger.getLogger(FormatDetailsProvider.class.getCanonicalName());
	private final List<FormatDetailsSupplier> suppliers; 

	public FormatDetailsProvider() {
		this.suppliers = new CopyOnWriteArrayList<>();
	}
	
	/**
	 * <p>
	 * Creates a new FormatDetailsProviderService and populates it using the SPI (java
	 * service provider interface).
	 * </p>
	 * 
	 * <p>
	 * In an OSGi context, an instance should be retrieved using the service
	 * registry. It will be registered under the FormatDetailsProviderService
	 * interface.
	 * </p>
	 * 
	 * @return returns a new FormatDetailsProviderService
	 */
	public static final FormatDetailsProviderService newInstance() {
		FormatDetailsProvider ret = new FormatDetailsProvider();
		for (FormatDetailsSupplier factory : ServiceLoader.load(FormatDetailsSupplier.class)) {
			ret.addFactory(factory);
		}
		return ret;
	}
	
	/**
	 * Adds a factory (intended for use by the OSGi framework)
	 * @param factory the factory to add
	 */
	@Reference(cardinality=ReferenceCardinality.MULTIPLE, policy=ReferencePolicy.DYNAMIC)
	public void addFactory(FormatDetailsSupplier factory) {
		if (LOGGER.isLoggable(Level.FINER)) {
			LOGGER.finer("Adding factory: " + factory);
		}
		suppliers.add(factory);
	}
	
	/**
	 * Removes a factory (intended for use by the OSGi framework)
	 * @param factory the factory to remove
	 */
	// Unbind reference added automatically from addFactory annotation
	public void removeFactory(FormatDetailsSupplier factory) {
		if (LOGGER.isLoggable(Level.FINER)) {
			LOGGER.finer("Removing factory: " + factory);
		}
		suppliers.remove(factory);
	}
	
	@Override
	public Optional<FormatDetails> getDetails(FormatIdentifier identifier) {
		return suppliers.stream()
				.flatMap(v->v.listDetails().stream())
				.filter(v->v.getIdentifier().equals(identifier))
				.findAny();
	}
	
	@Override
	public List<FormatDetails> findByExtension(String ext) {
		return suppliers.stream()
				.flatMap(v->v.listDetails().stream())
				.filter(v->v.getExtensions().stream().anyMatch(vx->vx.equals(ext)))
				.collect(Collectors.toList());
	}

}
