package org.daisy.streamline.api.media;

import java.nio.file.Path;

/**
 * Provides a modifiable file set.
 * 
 * @author Joel Håkansson
 */
public interface ModifiableFileSet extends FileSet {

	/**
	 * Copies all external resources into this file set. The resources in this
	 * file set will be updated with their new locations.
	 * 
	 * For the copy to succeed, the path to the new location must be a descendant
	 * of {@link #getBaseFolder()}. For example, if the resource path starts with
	 * <code>..</code>, it will not be processed.
	 * 
	 * See also, {@link #internalizeBelow(Path)}.
	 */
	public void internalizeAllCopy();
	
	/**
	 * Moves all resources located in the specified directory into this file set's base
	 * location. Resources in this file set will be update with the new locations.
	 * 
	 * For the move to succeed, the path to the new location must be a descendant
	 * of {@link #getBaseFolder()}. For example, if the resource path starts with
	 * <code>..</code>, it will not be processed.
	 * 
	 * See also, {@link #internalizeAllCopy()}.
	 * @param base the directory
	 */
	public void internalizeBelow(Path base);
	
	/**
	 * Internalizes the specified file set path by copying the original resource
	 * into the file set.
	 * 
	 * For the copy to succeed, the path to the new location must be a descendant
	 * of {@link #getBaseFolder()}. For example, if the resource path starts with
	 * <code>..</code>, it will not be processed.
	 * 
	 * See also, {@link #internalize(String)}.
	 * @param path the path
	 * @return true if the file was successfully internalized, false otherwise
	 */
	public boolean internalizeCopy(String path);
	
	/**
	 * Internalizes the specified file set path by moving the resource from its
	 * current location into the file set.
	 * 
	 * For the move to succeed, the path to the new location must be a descendant
	 * of {@link #getBaseFolder()}. For example, if the resource path starts with
	 * <code>..</code>, it will not be processed.
	 * 
	 * See also, {@link #internalizeCopy(String)}. 
	 * @param path the path
	 * @return true if the file was successfully internalized, false otherwise
	 */
	public boolean internalize(String path);

}
