package org.daisy.dotify.consumer.identity;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.daisy.dotify.api.identity.IdentificationFailedException;
import org.daisy.dotify.api.identity.Identifier;
import org.daisy.dotify.api.identity.IdentifierFactory;
import org.daisy.dotify.api.identity.IdentityProviderService;
import org.daisy.dotify.api.identity.IdentitySpecification;
import org.daisy.dotify.api.tasks.AnnotatedFile;
import org.daisy.dotify.api.tasks.DefaultAnnotatedFile;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component
public class IdentityProvider implements IdentityProviderService {
	private final List<IdentifierFactory> filters;
	private static final Logger logger = Logger.getLogger(IdentityProvider.class.getCanonicalName());
	
	public IdentityProvider() {
		this.filters = new CopyOnWriteArrayList<>();
	}
	
	@Reference(type = '*')
	public void addFactory(IdentifierFactory factory) {
		if (logger.isLoggable(Level.FINER)) {
			logger.finer("Adding factory: " + factory);
		}
		filters.add(factory);
	}

	// Unbind reference added automatically from addFactory annotation
	public void removeFactory(IdentifierFactory factory) {
		if (logger.isLoggable(Level.FINER)) {
			logger.finer("Removing factory: " + factory);
		}
		// this is to avoid adding items to the cache that were removed while
		// iterating
		synchronized (map) {
			filters.remove(factory);
			map.clear();
		}
	}
	
	public Collection<IdentifierFactory> getMatching(IdentitySpecification spec) {
		return Collections.emptyList();
	}

	@Override
	public AnnotatedFile identify(File in) {
		AnnotatedFile f = DefaultAnnotatedFile.with(in).extension(in).build();
		IdentitySpecification emptySpec = null;
		AnnotatedFile prev;
		do {
			prev = f;
			try {
				f = identify(f, null);
			} catch (IdentificationFailedException e) {
				
			}
		} while (f!=prev);
		return f;
	}

	private AnnotatedFile identify(AnnotatedFile f, IdentitySpecification spec) throws IdentificationFailedException {
		IdentificationFailedException ex = new IdentificationFailedException();
		for (IdentifierFactory id : getMatching(spec)) {
			Identifier i = id.newIdentifier();
			try {
				return i.identify(f);
			} catch (IdentificationFailedException e) {
				ex.addSuppressed(e);
			}
		}
		throw ex;
	}

}
