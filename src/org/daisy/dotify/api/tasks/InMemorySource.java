package org.daisy.dotify.api.tasks;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class InMemorySource extends AbstractAnnotated implements TaskSource {
	private final byte[] source;

	public InMemorySource(byte[] source, String ext, String mediaType) {
		super(ext, mediaType);
		if (source==null) {
			throw new IllegalArgumentException("Source cannot be null.");
		}
		this.source = source;
	}

	@Override
	public InputStream newInputStream() {
		return new ByteArrayInputStream(source);
	}

}
