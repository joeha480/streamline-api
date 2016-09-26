package org.daisy.dotify.api.tasks;

public final class TaskGroupSpecification {
	public enum Type {
		/**
		 * Input format
		 */
		VALIDATING,
		/**
		 * Input format, properties
		 */
		REFINING,
		/**
		 * Input format, output format, locale (properties)
		 */
		CONVERTING
	}

	private final String input, output, locale;
	private final Type type;
	
	public TaskGroupSpecification(String input, Type type) {
		if (type == Type.CONVERTING) {
			throw new IllegalArgumentException("Converting type not allowed on the same format.");
		}
		this.input = input;
		this.output = input;
		this.locale = null;
		this.type = type;
	}
	
	public TaskGroupSpecification(String input, String output, String locale) {
		this.input = input;
		this.output = output;
		this.locale = locale;
		this.type = input.equals(output)?Type.REFINING:Type.CONVERTING;
	}
	
	public static TaskGroupSpecification newRefiningSpec(String input, String locale){
		return null;
	}

	public String getLocale() {
		return locale;
	}

	public String getInputFormat() {
		return input;
	}
	
	public String getOutputFormat() {
		return output;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((input == null) ? 0 : input.hashCode());
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result + ((output == null) ? 0 : output.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TaskGroupSpecification other = (TaskGroupSpecification) obj;
		if (input == null) {
			if (other.input != null) {
				return false;
			}
		} else if (!input.equals(other.input)) {
			return false;
		}
		if (locale == null) {
			if (other.locale != null) {
				return false;
			}
		} else if (!locale.equals(other.locale)) {
			return false;
		}
		if (output == null) {
			if (other.output != null) {
				return false;
			}
		} else if (!output.equals(other.output)) {
			return false;
		}
		return true;
	}

}
