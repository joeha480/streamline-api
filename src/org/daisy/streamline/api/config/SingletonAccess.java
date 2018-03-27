package org.daisy.streamline.api.config;

import java.io.IOException;

public interface SingletonAccess {

	/**
	 * Acquire a lock on the file.
	 * @return returns true if a lock was acquired, false otherwise
	 * @throws IOException if an I/O error occurs
	 */
	public boolean acquireLock() throws IOException;
	
	/**
	 * Releases a previously acquired lock.
	 */
	public void releaseLock();
}
