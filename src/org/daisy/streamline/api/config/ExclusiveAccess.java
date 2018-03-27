package org.daisy.streamline.api.config;

/**
 * Defines inter-process exclusive access signaling. An
 * implementation is required to provide a mechanism that only
 * gives one caller access at any given time, even across multiple
 * JVMs. The lock must be retained until it is explicitly
 * released by the owner, or the owner's JVM exits, whichever
 * comes first.
 * 
 * @author Joel Håkansson
 */
public interface ExclusiveAccess {

	/**
	 * Tries to acquire exclusive access.
	 * 
	 * @return returns true if access was acquired, false if access
	 * 			could not be acquired because someone else already 
	 * 			has it.
	 * @throws ExclusiveAccessException if access could not be acquired for reasons 
	 * 			other than that the access is already owned by another instance.
	 */
	public boolean acquire() throws ExclusiveAccessException;
	
	/**
	 * Releases previously acquired access.
	 */
	public void release();
}
