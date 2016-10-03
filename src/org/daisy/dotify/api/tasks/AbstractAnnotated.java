package org.daisy.dotify.api.tasks;

public abstract class AbstractAnnotated implements Annotated {
	protected final String ext;
	protected final String mediaType;

	public AbstractAnnotated(String ext, String mediaType) {
		this.ext = ext;
		this.mediaType = mediaType;
	}

	@Override
	public String getExtension() {
		return ext;
	}

	@Override
	public String getMediaType() {
		return mediaType;
	}

}
