package org.daisy.streamline.api.media;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Provides a default implementation of {@link AnnotatedInputStream}.
 * @author Joel Håkansson
 */
public final class DefaultAnnotatedInputStream implements AnnotatedInputStream {
	private final InputStreamSupplier stream;
	private final FileDetails details;
	
	/**
	 * Provides a builder for {@link DefaultAnnotatedInputStream}s.
	 */
	public static class Builder {
		private final InputStreamSupplier stream;
		private FileDetails details;
		
		/**
		 * Creates a new builder with the specified input stream.
		 * @param stream the input stream
		 */
		public Builder(InputStreamSupplier stream) {
			this.stream = stream;
			DefaultFileDetails.Builder detailsBuilder = new DefaultFileDetails.Builder();
			if (stream.getSystemId()!=null) {
				detailsBuilder.extension(findExtension(stream.getSystemId()));
			}
			this.details = detailsBuilder.build();
		}
		
		private static String findExtension(String inp) {
			int inx = inp.lastIndexOf('.');
			return (inx>-1 && inx<inp.length()-1)?inp.substring(inx + 1):null;
		}
		
		/**
		 * Sets the file details for this builder.
		 * @param details the details
		 * @return this builder
		 */
		public Builder details(FileDetails details) {
			this.details = details;
			return this;
		}
		
		/**
		 * Builds a new {@link DefaultAnnotatedInputStream} based on the current state of this builder.
		 * @return the new instance
		 */
		public DefaultAnnotatedInputStream build() {
			return new DefaultAnnotatedInputStream(this); 
		}
		
	}
	
	private DefaultAnnotatedInputStream(Builder builder) {
		this.stream = builder.stream;
		this.details = builder.details;
	}
	
	/**
	 * Creates a new instance with the properties of the specified source.
	 * @param source the source
	 * @return returns a new instance
	 */
	public static DefaultAnnotatedInputStream create(InputStreamSupplier source) {
		return new Builder(source).build();
	}

	@Override
	public String getFormatName() {
		return details.getFormatName();
	}

	@Override
	public String getExtension() {
		return details.getExtension();
	}

	@Override
	public String getMediaType() {
		return details.getMediaType();
	}

	@Override
	public Map<String, Object> getProperties() {
		return details.getProperties();
	}

	@Override
	public InputStream newInputStream() throws IOException {
		return stream.newInputStream();
	}

	@Override
	public String getSystemId() {
		return stream.getSystemId();
	}

}
