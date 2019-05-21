package org.daisy.streamline.api.details;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.daisy.streamline.api.media.FormatIdentifier;

/**
 * Provides details about a format.
 * @author Joel Håkansson
 *
 */
public final class FormatDetails {
	private final FormatIdentifier identifier;
	private final Set<String> extensions;
	private final String displayName;
	private final Optional<String> description;
	
	/**
	 * Provides a builder for {@link FormatDetails}.
	 */
	public static class Builder {
		private final FormatIdentifier identifier;
		private String displayName;
		private Set<String> extensions = null;
		private String description = null;
		
		private Builder(FormatIdentifier identifier) {
			this.identifier = identifier;
			this.displayName = identifier.getIdentifier();
		}
		
		/**
		 * Adds an extension for the format details.
		 * @param values the extensions
		 * @return this builder
		 */
		public Builder extensions(String ... values) {
			this.extensions = new HashSet<>(Arrays.asList(values));
			return this;
		}
		
		/**
		 * Sets the display name for the format details.
		 * @param value the display name
		 * @return this builder
		 * @throws NullPointerException if <code>value</code> is null
		 */
		public Builder displayName(String value) {
			displayName = Objects.requireNonNull(value);
			return this;
		}
		
		/**
		 * Sets the description for the format details.
		 * @param value the description
		 * @return this builder
		 */
		public Builder description(String value) {
			description = value;
			return this;
		}
		
		/**
		 * Creates a new builder with the current configuration 
		 * of this builder.
		 * @return a new {@link FormatDetails} instance
		 */
		public FormatDetails build() {
			return new FormatDetails(this);
		}
	}
	
	/**
	 * Creates a new format details builder with the specified identifier.
	 * @param identifier the format identifier
	 * @return a new builder
	 */
	public static Builder with(FormatIdentifier identifier) {
		return new Builder(identifier);
	}
	
	private FormatDetails(Builder builder) {
		this.identifier = builder.identifier;
		this.extensions = Collections.unmodifiableSet(builder.extensions==null?new HashSet<>():builder.extensions);
		this.displayName = builder.displayName;
		this.description = Optional.ofNullable(builder.description);
	}

	/**
	 * Gets the {@link FormatIdentifier} that identifies the details.
	 * @return the format identifier
	 */
	public FormatIdentifier getIdentifier() {
		return identifier;
	}
	
	/**
	 * Gets the extensions the details.
	 * @return a set of file name extensions
	 */
	public Set<String> getExtensions() {
		return extensions;
	}
	
	/**
	 * Gets the display name for this format, may be localized.
	 * @return the display name, never null
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Gets the description for this format, may be localized.
	 * @return an optional with the description, or an empty optional if no description is available
	 */
	public Optional<String> getDescription() {
		return description;
	}
	
}
