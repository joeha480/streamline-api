package org.daisy.dotify.api.identity;

import org.daisy.dotify.api.tasks.AnnotatedFile;


public interface Identifier {

	public AnnotatedFile identify(AnnotatedFile f) throws IdentificationFailedException;
	
}
