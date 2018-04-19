package org.daisy.streamline.api.media;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Provides a default implementation of AnnotatedFile
 * 
 * @author Joel Håkansson
 */
public class DefaultAnnotatedFile implements AnnotatedFile {
	private final Path f;
	private final String formatName;
	private final Optional<FormatIdentifier> formatIdentifier;
	private final String extension;
	private final String mediaType;
	private final Map<String, Object> props;
	
	/**
	 * Provides a builder for an annotated file
	 * @author Joel Håkansson
	 *
	 */
	public static class Builder {
		private Path f;
		private String formatName = null;
		private FormatIdentifier formatIdentifier = null;
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
		 * @deprecated use {@code Builder(Path)}
		 */
		@Deprecated
		public Builder(File f) {
			if (f==null) {
				throw new IllegalArgumentException("Null not allowed");
			}
			this.f = f.toPath();
		}
		
		/**
		 * Creates a new builder with the specified file as its file.
		 * Note that the extension and media type is <b>NOT</b> set
		 * from the supplied file.
		 * 
		 * @param f the file
		 * @throws NullPointerException if file is null
		 */
		public Builder(Path f) {
			this.f = Objects.requireNonNull(f);
		}
		
		/**
		 * Sets the file.
		 * @param value the file
		 * @return returns this builder
		 * @throws IllegalArgumentException if file is null
		 * @deprecated use {@link #file(Path)}
		 */
		@Deprecated
		public Builder file(File value) {
			if (value==null) {
				throw new IllegalArgumentException("Null not allowed");
			}
			this.f = value.toPath();
			return this;
		}
		
		/**
		 * Sets the file.
		 * @param value the file
		 * @return returns this builder
		 * @throws NullPointerException if file is null
		 */
		public Builder file(Path value) {
			if (value==null) {
				throw new IllegalArgumentException("Null not allowed");
			}
			this.f = Objects.requireNonNull(value);
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
		 * Sets the formatIdentifier for this file type.
		 * @param value the formatIdentifier
		 * @return this builder
		 */
		public Builder formatIdentifier(FormatIdentifier value) {
			this.formatIdentifier = value;
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
		 * @deprecated use {@link #extension(Path)}
		 */
		@Deprecated
		public Builder extension(File value) {
			String inp = value.getName();
			this.extension = findExtension(inp);
			return this;
		}
		
		/**
		 * Sets the file extension to the extension of the specified file.
		 * @param value the file to use
		 * @return this builder
		 */
		public Builder extension(Path value) {
			String inp = value.getFileName().toString();
			this.extension = findExtension(inp);
			return this;
		}
		
		private static String findExtension(String inp) {
			int inx = inp.lastIndexOf('.');
			return (inx>-1 && inx<inp.length()-1)?inp.substring(inx + 1):null;
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
		 * @deprecated use {@link #mediaType(Path)}
		 */
		@Deprecated
		public Builder mediaType(File value) throws IOException {
			this.mediaType = Files.probeContentType(value.toPath());
			return this;
		}
		
		/**
		 * Sets the media type to the media type detected by {@link java.nio.file.Files#probeContentType(java.nio.file.Path)} on
		 * the specified file.
		 * @param value the path to use
		 * @return returns this builder
		 * @throws IOException if an I/O error occurs
		 */
		public Builder mediaType(Path value) throws IOException {
			this.mediaType = Files.probeContentType(value);
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
	 * @deprecated use {@link #with(Path)}
	 */
	@Deprecated
	public static Builder with(File f) {
		return new Builder(f);
	}
	
	/**
	 * Creates a new builder with the specified file as its file.
	 * Note that the extension and media type is <b>NOT</b> set
	 * from the supplied file.
	 * 
	 * @param f the file
	 * @return returns a new builder
	 */
	public static Builder with(Path f) {
		return new Builder(f);
	}
	
	/**
	 * Creates a new builder with the same details as the supplied file.
	 * @param f the file
	 * @return returns a new builder
	 */
	public static Builder with(AnnotatedFile f) {
		return new Builder(f.getPath())
				.formatName(f.getFormatName())
				.extension(f.getExtension())
				.mediaType(f.getMediaType())
				.formatIdentifier(f.getFormatIdentifier().orElse(null))
				.properties(f.getProperties());
	}
	
	/**
	 * Creates a new builder with the specified details and file.
	 * @param fd the file details
	 * @param f the file
	 * @return returns a new builder
	 */
	public static Builder with(FileDetails fd, File f) {
		return new Builder(f)
				.formatName(fd.getFormatName())
				.extension(fd.getExtension())
				.mediaType(fd.getMediaType())
				.formatIdentifier(fd.getFormatIdentifier().orElse(null))
				.properties(fd.getProperties());
	}
	
	/**
	 * Creates a new DefaultAnnotatedFile with the properties of the specified file.
	 * The media type will be probed by {@link java.nio.file.Files#probeContentType(java.nio.file.Path)}.
	 * If this process is unsuccessful, the media type will be null. For more control over the 
	 * process, use {@link #with(File)}.
	 * @param f the file
	 * @return returns a new DefaultAnnotatedFile instance
	 * @deprecated use {@link #create(Path)}
	 */
	@Deprecated
	public static DefaultAnnotatedFile create(File f) {
		Builder ret = new Builder(f).extension(f);
		try {
			ret.mediaType(f);
		} catch (IOException e) {
			// no action needed
		}
		return ret.build();
	}
	
	/**
	 * Creates a new DefaultAnnotatedFile with the properties of the specified file.
	 * The media type will be probed by {@link java.nio.file.Files#probeContentType(java.nio.file.Path)}.
	 * If this process is unsuccessful, the media type will be null. For more control over the 
	 * process, use {@link #with(File)}.
	 * @param f the path
	 * @return returns a new DefaultAnnotatedFile instance
	 */
	public static DefaultAnnotatedFile create(Path f) {
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
		this.formatIdentifier = Optional.ofNullable(builder.formatIdentifier);
		this.props = Collections.unmodifiableMap(builder.props);
	}

	@Override
	@Deprecated
	public File getFile() {
		return f.toFile();
	}

	@Override
	public Path getPath() {
		return f;
	}
	
	@Override
	public String getFormatName() {
		return formatName;
	}

	@Override
	public Optional<FormatIdentifier> getFormatIdentifier() {
		return formatIdentifier;
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
