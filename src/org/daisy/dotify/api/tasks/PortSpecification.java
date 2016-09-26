package org.daisy.dotify.api.tasks;

import java.util.List;

public interface PortSpecification {
	public enum Category {
		
		/**
		 * 
		 */
		DETECTING,
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
	
	public enum Strength {
		/**
		 * No information about the probability of the actual content being what it says it is
		 */
		NONE,
		/**
		 * Some method of detection has been used to determine the contents type, but the checks may
		 * be insufficient
		 */
		DETECTED,
		/**
		 * The content was created with the intent of being compliant with the format's specification
		 */
		INTENDED,
		/**
		 * The content has been verified as compliant to the greatest extent possible
		 */
		CONFIRMED
	}
	
	public String getFormat();

	public Strength getStrength();
	
	public List<PortPropertyAttribute> getProperties();
}
