package org.daisy.dotify.api.validity;

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
	
	enum Type {
		FATAL_ERROR,
		ERROR,
		WARNING,
		NOTICE;
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
		public Builder uri(URI value) {
			if (value.isOpaque() || value.isAbsolute()) {
				throw new IllegalArgumentException("Opaque or absolute uri.");
			}
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
	
	public Optional<URI> getURI() {
		return uri;
	}

	@Override
	public String toString() {
		List<String> ins = new ArrayList<>();
		ins.add(toStringTypeLineColumn()+":");
		ins.add(getMessage().orElse(""));
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
