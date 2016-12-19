package org.daisy.dotify.api.identity;

import java.io.File;

import org.daisy.dotify.api.tasks.AnnotatedFile;
import org.daisy.dotify.api.tasks.DefaultAnnotatedFile;

public class IdentitySpecification {

	public AnnotatedFile toAnnotatedFile(File f) {
		return DefaultAnnotatedFile.with(f).build();
	}
}
