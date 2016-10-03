package org.daisy.dotify.api.tasks;

public class InMemoryResult extends AbstractAnnotated implements TaskResult {
	private final byte[] data;

	public InMemoryResult(byte[] data, String ext, String mediaType) {
		super(ext, mediaType);
		if (data==null) {
			throw new IllegalArgumentException("Data cannot be null.");
		}
		this.data = data;
	}

	@Override
	public TaskSource toSource() {
		return new InMemorySource(data, ext, mediaType);
	}

}
