package org.daisy.streamline.api.media;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides a default implementation of {@link FileDetails}.
 * @author Joel Håkansson
 */
public final class DefaultFileDetails implements FileDetails {
	private final String formatName;
	private final String extension;
	private final String mediaType;
	private final Map<String, Object> props;

	/**
	 * Provides a builder for {@link DefaultFileDetails}.
	 */
	public static class Builder {
		private String formatName = null;
		private String extension = null;
		private String mediaType = null;
		private Map<String, Object> props = new HashMap<>();

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
		 * Sets the media type
		 * @param value the media type
		 * @return this builder
		 */
		public Builder mediaType(String value) {
			this.mediaType = value;
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
		 * Builds a {@link DefaultFileDetails} based on the current state of this builder.
		 * @return the new instance
		 */
		public DefaultFileDetails build() {
			return new DefaultFileDetails(this);
		}

	}
	
	private DefaultFileDetails(Builder builder) {
		this.formatName = builder.formatName;
		this.extension = builder.extension;
		this.mediaType = builder.mediaType;
		this.props = Collections.unmodifiableMap(builder.props);
	}
	
	/**
	 * Creates a new builder with the same details as the supplied file.
	 * @param template the details
	 * @return returns a new builder
	 */
	public static Builder with(FileDetails template) {
		return new Builder().formatName(template.getFormatName()).extension(template.getExtension()).mediaType(template.getMediaType()).properties(template.getProperties());
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((extension == null) ? 0 : extension.hashCode());
		result = prime * result + ((formatName == null) ? 0 : formatName.hashCode());
		result = prime * result + ((mediaType == null) ? 0 : mediaType.hashCode());
		result = prime * result + ((props == null) ? 0 : props.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultFileDetails other = (DefaultFileDetails) obj;
		if (extension == null) {
			if (other.extension != null)
				return false;
		} else if (!extension.equals(other.extension))
			return false;
		if (formatName == null) {
			if (other.formatName != null)
				return false;
		} else if (!formatName.equals(other.formatName))
			return false;
		if (mediaType == null) {
			if (other.mediaType != null)
				return false;
		} else if (!mediaType.equals(other.mediaType))
			return false;
		if (props == null) {
			if (other.props != null)
				return false;
		} else if (!props.equals(other.props))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DefaultFileDetails [formatName=" + formatName + ", extension=" + extension + ", mediaType=" + mediaType
				+ ", props=" + props + "]";
	}

}