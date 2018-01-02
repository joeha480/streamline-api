package org.daisy.streamline.api.tasks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.daisy.streamline.api.media.AnnotatedFile;
import org.daisy.streamline.api.media.BaseFolder;
import org.daisy.streamline.api.media.DefaultFileSet;
import org.daisy.streamline.api.media.FileSet;
import org.daisy.streamline.api.media.ModifiableFileSet;

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
	 * <p>Apply the task to <code>input</code> and place the result in <code>output</code>.</p>
	 * <p>Note: Resources in the input file set that are not modified, but should be included in
	 * the output file set, should preferably keep their locations in the input file set for
	 * performance reasons. This may not always be possible, depending on the processing 
	 * requirements in the implementation of this method.</p>
	 * 
	 * @param input input file set
	 * @param output output location
	 * @return the output file set
	 * @throws InternalTaskException throws InternalTaskException if something goes wrong.
	 */
	public ModifiableFileSet execute(FileSet input, BaseFolder output)  throws InternalTaskException {
		try {
			AnnotatedFile f = execute(input.getManifest(), Files.createTempFile(output.getPath(), "file", ".tmp").toFile());
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
