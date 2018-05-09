package org.daisy.streamline.api.tasks;

import java.io.File;
import java.util.List;

import org.daisy.streamline.api.media.AnnotatedFile;
import org.daisy.streamline.api.media.FileSet;

/**
 * Provides an abstract base for expanding tasks. 
 * 
 * @author Joel Håkansson
 *
 */
public abstract class ExpandingTask extends InternalTask { //NOPMD

	/**
	 * Creates a new expanding task with the specified name
	 * @param name the name of the task
	 */
	public ExpandingTask(String name) {
		super(name);
	}

	/**
	 * Resolves the task into other tasks based on the contents of the <code>input</code>.
	 * @param input input file
	 * @return returns a list of internal tasks
	 * @throws InternalTaskException throws InternalTaskException if something goes wrong.
	 * @deprecated use {@link #resolve(AnnotatedFile)}
	 */
	@Deprecated
	public abstract List<InternalTask> resolve(File input) throws InternalTaskException;
	
	/**
	 * Resolves the task into other tasks based on the contents of the <code>input</code>.
	 * @param input annotated input file
	 * @return returns a list of internal tasks
	 * @throws InternalTaskException throws InternalTaskException if something goes wrong.
	 */
	public abstract List<InternalTask> resolve(AnnotatedFile input) throws InternalTaskException;
	
	/**
	 * Resolves the task into other tasks based on the contents of the <code>input</code>.
	 * @param input input file set
	 * @return returns a list of internal tasks
	 * @throws InternalTaskException throws InternalTaskException if something goes wrong.
	 */
	public List<InternalTask> resolve(FileSet input) throws InternalTaskException {
		return resolve(input.getManifest());
	}

	@Override
	public final Type getType() {
		return Type.EXPANDING;
	}
	
	@Override
	public final ExpandingTask asExpandingTask() {
		return this;
	}

}
