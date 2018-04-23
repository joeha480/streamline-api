package org.daisy.streamline.api.identity;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.daisy.streamline.api.media.AnnotatedFile;
import org.daisy.streamline.api.media.BaseFolder;
import org.daisy.streamline.api.media.DefaultFileSet;
import org.daisy.streamline.api.media.FileSet;
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
public class FileSetMaker implements FileSetMakerService {
	private final List<FileSetProvider> filters;
	private static final Logger logger = Logger.getLogger(FileSetMaker.class.getCanonicalName());
	
	/**
	 * Creates a new empty identity provider.
	 */
	public FileSetMaker() {
		this.filters = new CopyOnWriteArrayList<>();
	}
	
	/**
	 * <p>
	 * Creates a new {@link FileSetMakerService} and populates it using the SPI (java
	 * service provider interface).
	 * </p>
	 * 
	 * <p>
	 * In an OSGi context, an instance should be retrieved using the service
	 * registry. It will be registered under the IdentityProviderService
	 * interface.
	 * </p>
	 * 
	 * @return returns a new {@link FileSetMakerService}
	 */
	public static final FileSetMakerService newInstance() {
		FileSetMaker ret = new FileSetMaker();
		for (FileSetProvider factory : ServiceLoader.load(FileSetProvider.class)) {
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
	public void addFactory(FileSetProvider factory) {
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
	public void removeFactory(FileSetProvider factory) {
		if (logger.isLoggable(Level.FINER)) {
			logger.finer("Removing factory: " + factory);
		}
		filters.remove(factory);
	}

	@Override
	public FileSet create(AnnotatedFile in) {
		return filters.stream()
			.filter(f->f.accepts(in))
			.map(f->{
					try {
						return f.create(in);
					} catch (FileSetException e) {
						logger.log(Level.WARNING, "Failed to create file set with provider: " + f, e);
						return null;
					}
				})
			.filter(v->v!=null)
			.findFirst()
			.orElse(DefaultFileSet.with(BaseFolder.with(in.getFile().getParentFile()), in).build());
	}

}