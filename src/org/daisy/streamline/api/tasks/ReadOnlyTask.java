package org.daisy.streamline.api.tasks;

import java.io.File;

import org.daisy.streamline.api.media.AnnotatedFile;
import org.daisy.streamline.api.media.FileSet;

/**
 * Provides an abstract base for read only tasks. A read only task is 
 * a task that does not produce an altered output.
 * 
 * @author Joel Håkansson
 *
 */
public abstract class ReadOnlyTask extends InternalTask { //NOPMD

	/**
	 * Creates a new read only task with the specified name
	 * @param name the name of the task
	 */
	public ReadOnlyTask(String name) {
		super(name);
	}

	/**
	 * Apply the task to <code>input</code>
	 * @param input input file
	 * @throws InternalTaskException throws InternalTaskException if something goes wrong.
	 * @deprecated use {@link #execute(AnnotatedFile)}
	 */
	@Deprecated
	public abstract void execute(File input) throws InternalTaskException;
	
	/**
	 * Apply the task to <code>input</code>
	 * @param input input file
	 * @throws InternalTaskException throws InternalTaskException if something goes wrong
	 */
	public abstract void execute(AnnotatedFile input) throws InternalTaskException;
	
	/**
	 * Apply the task to <code>input</code>
	 * @param input input file set
	 * @throws InternalTaskException throws InternalTaskException if something goes wrong
	 */
	public void execute(FileSet input) throws InternalTaskException {
		execute(input.getManifest());
	}
	
	@Override
	public final Type getType() {
		return Type.READ_ONLY;
	}
	
	@Override
	public final ReadOnlyTask asReadOnlyTask() {
		return this;
	}

}
