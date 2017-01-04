package org.daisy.dotify.api.tasks;

import java.util.List;

/**
 * Provides a compiled task system, which provides the tasks to 
 * execute as well as its name and relevant options.
 *  
 * @author Joel Håkansson
 */
public interface CompiledTaskSystem extends List<InternalTask> {
	
	/**
	 * Get a descriptive name for the task system
	 * @return returns the name for the task system
	 */
	public String getName();

	/**
	 * Gets a list of options for the task system
	 * @return returns a list of options
	 */
	public List<TaskOption> getOptions();

}
