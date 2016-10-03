package org.daisy.dotify.api.tasks;

import java.io.InputStream;

public interface TaskSource extends Annotated {

	/**
	 * Gets the input stream for the source.
	 * @return the input stream
	 */
	public InputStream newInputStream();

}
