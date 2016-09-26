package org.daisy.dotify.api.tasks;

public interface PortPropertyAttribute {
	public enum Category {
		REQUIRED,
		
	}
	
	public String getName();
	public String getValue();
	
	public void getCategory();

}
