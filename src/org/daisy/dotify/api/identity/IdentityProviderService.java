package org.daisy.dotify.api.identity;

import java.io.File;

import org.daisy.dotify.api.tasks.AnnotatedFile;

public interface IdentityProviderService {

	public AnnotatedFile identify(File in);
}
