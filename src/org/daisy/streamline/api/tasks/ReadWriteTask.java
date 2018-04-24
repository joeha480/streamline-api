package org.daisy.streamline.api.tasks;

import java.io.File;
import java.io.IOException;

import org.daisy.streamline.api.media.AnnotatedFile;
import org.daisy.streamline.api.media.BaseFolder;
import org.daisy.streamline.api.media.DefaultFileSet;
import org.daisy.streamline.api.media.FileSet;

/**
 * Provides an abstract base for read/write tasks. 
 * 
 * @author Joel Håkansson
 *
 */
public abstract class ReadWriteTask extends InternalTask { //NOPMD

	/**
	 * Creates a new read/write task with the specified name
	 * @param name the name of the task
	 */
	public ReadWriteTask(String name) {
		super(name);
	}

	/**
	 * Apply the task to <code>input</code> and place the result in <code>output</code>.
	 * @param input input file
	 * @param output output file
	 * @throws InternalTaskException throws InternalTaskException if something goes wrong.
	 */
	public abstract void execute(File input, File output) throws InternalTaskException;
	
	/**
	 * Apply the task to <code>input</code> and place the result in <code>output</code>.
	 * @param input input file
	 * @param output output file
	 * @return returns the annotated output file
	 * @throws InternalTaskException throws InternalTaskException if something goes wrong.
	 */
	public abstract AnnotatedFile execute(AnnotatedFile input, File output) throws InternalTaskException;
	
	/**
	 * Apply the task to <code>input</code> and place the result in <code>output</code>.
	 * @param input input file set
	 * @param output output location
	 * @return the output file set
	 * @throws InternalTaskException throws InternalTaskException if something goes wrong.
	 */
	public FileSet execute(FileSet input, BaseFolder output)  throws InternalTaskException {
		try {
			AnnotatedFile f = execute(input.getManifest(), File.createTempFile("file", ".tmp", output.getLocation()));
			return DefaultFileSet.with(output, f).build();
		} catch (IOException e) {
			throw new InternalTaskException(e);
		}
	}

	@Override
	public final Type getType() {
		return Type.READ_WRITE;
	}
	
	@Override
	public final ReadWriteTask asReadWriteTask() {
		return this;
	}
}
