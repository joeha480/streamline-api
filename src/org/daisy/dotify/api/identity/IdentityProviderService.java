package org.daisy.dotify.api.identity;

import java.io.File;

import org.daisy.dotify.api.tasks.AnnotatedFile;

/**
 * <p>
 * Provides an interface for an IdentityProvider service. The purpose of
 * this interface is to expose an implementation of an IdentityProvider as
 * an OSGi service.
 * </p>
 * 
 * <p>
 * To comply with this interface, an implementation must be thread safe and
 * address both the possibility that only a single instance is created and used
 * throughout and that new instances are created as desired.
 * </p>
 * 
 * @author Joel Håkansson
 * 
 */
public interface IdentityProviderService {

	/**
	 * Identifies the file type of the supplied file.
	 * @param in the file to identify
	 * @return returns a file annotated with the type details
	 */
	public AnnotatedFile identify(File in);
}
