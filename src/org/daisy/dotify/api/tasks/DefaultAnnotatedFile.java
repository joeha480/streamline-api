package org.daisy.dotify.api.tasks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides a default implementation of AnnotatedFile
 * 
 * @author Joel Håkansson
 */
public class DefaultAnnotatedFile implements AnnotatedFile {
	private final File f;
	private final String formatName;
	private final String extension;
	private final String mediaType;
	private final Map<String, Object> props;
	
	/**
	 * Provides a builder for an annotated file
	 * @author Joel Håkansson
	 *
	 */
	public static class Builder {
		private File f;
		private String formatName = null;
		private String extension = null;
		private String mediaType = null;
		private Map<String, Object> props = new HashMap<>();
		
		/**
		 * Creates a new builder with the specified file as its file.
		 * Note that the extension and media type is <b>NOT</b> set
		 * from the supplied file.
		 * 
		 * @param f the file
		 * @throws IllegalArgumentException if file is null
		 */
		public Builder(File f) {
			if (f==null) {
				throw new IllegalArgumentException("Null not allowed");
			}
			this.f = f;
		}
		
		/**
		 * Sets the file.
		 * @param value the file
		 * @return returns this builder
		 * @throws IllegalArgumentException if file is null
		 */
		public Builder file(File value) {
			if (value==null) {
				throw new IllegalArgumentException("Null not allowed");
			}
			this.f = value;
			return this;
		}
		
		/**
		 * Sets the format name.
		 * @param value the name
		 * @return returns this builder
		 */
		public Builder formatName(String value) {
			this.formatName = value;
			return this;
		}

		/**
		 * Sets the file extension
		 * @param value the extension
		 * @return this builder
		 */
		public Builder extension(String value) {
			this.extension = value;
			return this;
		}
		
		/**
		 * Sets the file extension to the extension of the specified file.
		 * @param value the file to use
		 * @return this builder
		 */
		public Builder extension(File value) {
			String inp = value.getName();
			int inx = inp.lastIndexOf('.');
			this.extension = (inx>-1 && inx<inp.length()-1)?inp.substring(inx + 1):null;
			return this;
		}
		
		/**
		 * Sets the media type
		 * @param value the media type
		 * @return this builder
		 */
		public Builder mediaType(String value) {
			this.mediaType = value;
			return this;
		}
		
		/**
		 * Sets the media type to the media type detected by {@link java.nio.file.Files#probeContentType(java.nio.file.Path)} on
		 * the specified file.
		 * @param value the file to use
		 * @return returns this builder
		 * @throws IOException if an I/O error occurs
		 */
		public Builder mediaType(File value) throws IOException {
			this.mediaType = Files.probeContentType(value.toPath());
			return this;
		}
		
		/**
		 * Adds a property to this media type. The value
		 * must be immutable.
		 * @param key the key
		 * @param value the value
		 * @return returns this builder
		 */
		public Builder property(String key, Object value) {
			props.put(key, value);
			return this;
		}
		
		/**
		 * Adds the supplied properties to this builder. All values in
		 * this map must be immutable.
		 * @param values the value to add
		 * @return returns this builder
		 */
		public Builder properties(Map<String, Object> values) {
			props.putAll(values);
			return this;
		}

		/**
		 * Build the annotated file
		 * @return a new Annotated File
		 */
		public DefaultAnnotatedFile build() {
			return new DefaultAnnotatedFile(this);
		}
	}
	
	/**
	 * Creates a new builder with the specified file as its file.
	 * Note that the extension and media type is <b>NOT</b> set
	 * from the supplied file.
	 * 
	 * @param f the file
	 * @return returns a new builder
	 */
	public static Builder with(File f) {
		return new Builder(f);
	}
	
	/**
	 * Creates a new builder with the same details as the supplied file.
	 * @param f the file
	 * @return returns a new builder
	 */
	public static Builder with(AnnotatedFile f) {
		return new Builder(f.getFile()).formatName(f.getFormatName()).extension(f.getExtension()).mediaType(f.getMediaType()).properties(f.getProperties());
	}
	
	/**
	 * Creates a new DefaultAnnotatedFile with the properties of the specified file.
	 * The media type will be probed by {@link java.nio.file.Files#probeContentType(java.nio.file.Path)}.
	 * If this process is unsuccessful, the media type will be null. For more control over the 
	 * process, use {@link #with(File)}.
	 * @param f the file
	 * @return returns a new DefaultAnnotatedFile instance
	 */
	public static DefaultAnnotatedFile create(File f) {
		Builder ret = new Builder(f).extension(f);
		try {
			ret.mediaType(f);
		} catch (IOException e) {
			// no action needed
		}
		return ret.build();
	}

	private DefaultAnnotatedFile(Builder builder) {
		this.f = builder.f;
		this.formatName = builder.formatName;
		this.extension = builder.extension;
		this.mediaType = builder.mediaType;
		this.props = Collections.unmodifiableMap(builder.props);
	}

	@Override
	public File getFile() {
		return f;
	}
	
	@Override
	public String getFormatName() {
		return formatName;
	}

	@Override
	public String getExtension() {
		return extension;
	}

	@Override
	public String getMediaType() {
		return mediaType;
	}

	@Override
	public Map<String, Object> getProperties() {
		return props;
	}

}
