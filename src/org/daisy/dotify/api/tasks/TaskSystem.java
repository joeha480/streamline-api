package org.daisy.dotify.api.tasks;

import java.util.Collections;
import java.util.Map;

/**
 * TaskSystem is an interface used when compiling a series of InternalTasks
 * that may require both global Transformer parameters and individual
 * constructor arguments.
 * 
 * Implement this interface to create a new TaskSystem.
 * @author Joel Håkansson
 *
 */
public interface TaskSystem extends TaskGroup {
	
	/**
	 * Compile the task system using the supplied parameters
	 * @param parameters the parameters to pass to the task system
	 * @return returns a compiled task system
	 * @throws TaskSystemException if something goes wrong when compiling the task system
	 */
	public CompiledTaskSystem compile(Map<String, Object> parameters) throws TaskSystemException;
	

	/**
	 * Compile the task system with no parameters. To specify parameters, use {@link #compile(Map)}.
	 * @return returns a compiled task system
	 * @throws TaskSystemException if something goes wrong when compiling the task system
	 */
	public default CompiledTaskSystem compile()  throws TaskSystemException {
		return compile(Collections.emptyMap());
	}

}
