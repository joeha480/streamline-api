package org.daisy.streamline.api.tasks;

import java.util.Collections;
import java.util.List;

import org.daisy.streamline.api.option.UserOption;

/**
 * <p>Base class for internal tasks. This class is only
 * intended to be extended by classes in this package. Refer to the 
 * direct subclasses of this class for possible extension points.</p>
 * 
 * @author Joel Håkansson
 */
public abstract class InternalTask {
	/**
	 * Defines the task types.
	 */
	public enum Type {
		/**
		 * Defines a read only task.
		 */
		READ_ONLY,
		/**
		 * Defines a read/write task.
		 */
		READ_WRITE,
		/**
		 * Defines an expanding task.
		 */
		EXPANDING;
	}
	protected String name = null;

	// This constructor should not be part of the public API, it's probably a mistake and should be removed in the next major version.
	@Deprecated
	protected InternalTask() { }

	/**
	 * <p>Creates a new internal task with the specified name.</p>
	 * <p>Note: The constructor is intended for internal use only. It
	 * has package visibility rather than protected visibility 
	 * for this reason. Also, since the class is abstract, it cannot
	 * be instantiated from this package either, which is typically 
	 * prevented with protected visibility.</p>
	 * @param name a descriptive name for the task
	 */
	InternalTask(String name) {
		this.name = name;
	}

	/**
	 * Get the name of the internal task
	 * @return returns the name of this internal task
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets a list of parameters applicable to this instance
	 * @return returns a list of parameters
	 */
	public List<UserOption> getOptions() {
		return Collections.emptyList();
	}
	
	/**
	 * Gets the type of task.
	 * @return the type of task
	 */
	public abstract Type getType();

	/**
	 * Returns this task as a read only task. See also {@link #getType()}.
	 * @return the instance as a read only task
	 * @throws ClassCastException if the task is not of this type
	 */
	public ReadOnlyTask asReadOnlyTask() {
		throw new ClassCastException();
	}

	/**
	 * Returns this task as a read/write task. See also {@link #getType()}.
	 * @return the instance as a read/write task
	 * @throws ClassCastException if the task is not of this type
	 */
	public ReadWriteTask asReadWriteTask() {
		throw new ClassCastException();
	}
	
	/**
	 * Returns this task as an expanding task. See also {@link #getType()}.
	 * @return the instance as an expanding task
	 * @throws ClassCastException if the task is not of this type
	 */
	public ExpandingTask asExpandingTask() {
		throw new ClassCastException();
	}
}
