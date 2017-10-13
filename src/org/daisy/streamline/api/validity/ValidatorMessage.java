package org.daisy.streamline.api.validity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Provides a validation message.
 * @author Joel Håkansson
 */
public final class ValidatorMessage {
	private final Type type;
	private final Optional<URI> uri;
	private final Optional<Exception> exception;
	private final Optional<String> message;
	private final int lineNumber;
	private final int columnNumber;
	
	/**
	 * Provides types of messages
	 */
	public enum Type {
		/**
		 * Defines a fatal validation error. Fatal errors causes the source to be invalid.
		 * Typically, validation does not continue after an error of this type is encountered.
		 */
		FATAL_ERROR(true),
		/**
		 * Defines a validation error. Errors causes the source to be invalid, but
		 * validation typically continues after an error of this type is encountered.
		 */
		ERROR(true),
		/**
		 * Defines a validation warning. Warnings doesn't cause the source to be invalid, but 
		 * indicates problematic areas, for example deprecated or discouraged structural or 
		 * semantic constructs used in the source.  
		 */
		WARNING(false),
		/**
		 * Defines a validation notice. Notices doesn't cause the source to be invalid nor
		 * do they indicate problems. Notices are intended for providing a user with 
		 * interesting details about the source. For example, a validator may choose to
		 * output a notice about the exact format version used in the source.
		 */
		NOTICE(false);

		private final boolean causeForInvalidity;
		Type(boolean causeForInvalidity) {
			this.causeForInvalidity = causeForInvalidity;
		}
		
		/**
		 * Returns true if this message type is cause for invalidity.
		 * @return returns true if this message type is cause for invalidity, false otherwise
		 */
		public boolean causeForInvalidity() {
			return causeForInvalidity;
		}
	}
	
	/**
	 * Provides a builder for {@link ValidatorMessage}s.
	 * @author Joel Håkansson
	 */
	public static class Builder {
		private final Type type;
		private Exception exception = null;
		private String message = null;
		private int lineNumber = -1;
		private int columnNumber = -1;
		private URI uri = null;

		/**
		 * Creates a new builder of the specified type.
		 * @param type the type
		 */
		public Builder(Type type) {
			this.type = type;
		}
		/**
		 * Sets an exception for this builder
		 * @param value the exception
		 * @return returns this builder
		 */
		public Builder exception(Exception value) {
			this.exception = value;
			return this;
		}
		/**
		 * Sets the description message for this builder.
		 * @param value the message
		 * @return returns this builder
		 */
		public Builder message(String value) {
			this.message = value;
			return this;
		}
		/**
		 * Sets the line number for this builder.
		 * @param value the line number
		 * @return returns this builder
		 * @throws IllegalArgumentException if value is &lt; 0
		 */
		public Builder lineNumber(int value) {
			if (value<0) {
				throw new IllegalArgumentException("Value must be greater than or equal to 0.");
			}
			this.lineNumber = value;
			return this;
		}
		/**
		 * Sets the column number for this builder.
		 * @param value the column number
		 * @return returns this builder
		 * @throws IllegalArgumentException if value is &lt; 0
		 */
		public Builder columnNumber(int value) {
			if (value<0) {
				throw new IllegalArgumentException("Value must be greater than or equal to 0.");
			}
			this.columnNumber = value;
			return this;
		}
		/**
		 * Sets the URI for this builder. Setting a URI indicates that the message
		 * concerns an auxiliary file. If this value is not set, the message
		 * is expected to concern the primary file (the validator input). The URI may
		 * be absolute or relative to the primary file.
		 * 
		 * @param value the URI
		 * @return returns this builder
		 */
		public Builder uri(URI value) {
			this.uri = value;
			return this;
		}
		/**
		 * Creates a new {@link ValidatorMessage} based on the current state of this builder.
		 * @return returns a new {@link ValidatorMessage}
		 */
		public ValidatorMessage build() {
			return new ValidatorMessage(this);
		}
	}
	
	/**
	 * Creates a new message builder with the specified type.
	 * @param type the type of message
	 * @return returns a new builder
	 */
	public static Builder with(Type type) {
		return new Builder(type);
	}

	private ValidatorMessage(Builder builder) {
		this.type = builder.type;
		this.uri = Optional.ofNullable(builder.uri);
		this.exception = Optional.ofNullable(builder.exception);
		this.message = Optional.ofNullable(builder.message);
		this.lineNumber = builder.lineNumber;
		this.columnNumber = builder.columnNumber;
	}
	
    /**
     * Gets the line number where the exception occurred.
     *
     * <p>The first line is line 1.</p>
     *
     * @return An integer representing the line number, or -1
     *         if none is available.
     */
	public int getLineNumber() {
		return lineNumber;
	}
	
    /**
     * Gets the column number where the exception occurred.
     *
     * <p>The first column is column 1.</p>
     *
     * @return An integer representing the column number, or -1
     *         if none is available.
     */
	public int getColumnNumber() {
		return columnNumber;
	}
	
	/**
	 * Gets the associated exception, if any.
	 * @return returns an exception related to this message
	 */
	public Optional<Exception> getException() {
		return exception;
	}
	
	/**
	 * Gets a message description, if available.
	 * @return returns the message description
	 */
	public Optional<String> getMessage() {
		return message;
	}
	
	/**
	 * Gets the type of message.
	 * @return returns the message type
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * Gets the URI where the message applies. If this value is not set, the message
	 * applies to the primary file (the validator input). The URI may
	 * be absolute or relative to the primary file.
	 * @return returns the URI
	 */
	public Optional<URI> getURI() {
		return uri;
	}

	@Override
	public String toString() {
		List<String> ins = new ArrayList<>();
		ins.add(toStringTypeLineColumn()+":");
		ins.add(getMessage().orElse(getException().map(v->v.getMessage()).orElse("")));
		ins.add(getException().map(v->"["+v.toString()+"]").orElse(""));
		ins = ins.stream().filter(v -> !v.isEmpty()).collect(Collectors.toList());
		if (ins.size()<2) {
			ins.add("[No message]");
		}
		return ins.stream().collect(Collectors.joining(" "));
	}
	
	private String toStringTypeLineColumn() {
		List<String> ins = new ArrayList<>();
		ins.add(getType().toString());
		ins.add(getLineColumn(getLineNumber(), getColumnNumber()));
		return ins.stream().filter(v -> !v.isEmpty()).collect(Collectors.joining(" "));
	}
	
	private static String getLineColumn(int line, int column) {
		if (line<0 && column<0) {
			return "";
		} else { 
			boolean both = (line>=0 && column>=0);
			return "(at "+(line>=0?"line "+line:"")+
					(both?", ":"")+
					(column>=0?"column "+column:"")
					+")";
		}
	}

}
