package org.daisy.streamline.api.media;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Provides a file set.
 * @author Joel Håkansson
 */
public interface FileSet {
	
	/**
	 * The base folder for this file set.
	 * @return the base folder
	 */
	public BaseFolder getBaseFolder();
	
	/**
	 * Gets the manifest for this file set. This file 
	 * must have {@link #getBaseFolder()} as an ancestor.
	 * 
	 * @return the entry point
	 */
	public AnnotatedFile getManifest();
	
	/**
	 * Returns true if the specified path is the manifest, false otherwise.
	 * @param path the path
	 * @return true if the path is the manifest, false otherwise
	 */
	public boolean isManifest(String path);
	
	/**
	 * Gets the format identifier for the file set.
	 * @return returns the identifier
	 */
	public Optional<FormatIdentifier> getFormatIdentifier();

	/**
	 * Gets all registered resources.
	 * @return the resources
	 */
	public Map<String, AnnotatedFile> getResources();
	
	/**
	 * Gets all resource paths in the file set.
	 * @return a set of resource paths
	 */
	public Set<String> getResourcePaths();
	
	/**
	 * Gets the resource at the specified path.
	 * @param path the path
	 * @return returns the resource
	 */
	public Optional<AnnotatedFile> getResource(String path);
	
	/**
	 * Opens a stream to external resources.
	 * @return a stream of external resources
	 */
	public Stream<AnnotatedFile> streamExternal();
	
	/**
	 * Copies all external resources into this file set. The resources in this
	 * file set will be updated with their new locations.
	 * 
	 * For the copy to succeed, the path to the new location must be a descendant
	 * of {@link #getBaseFolder()}. For example, if the resource path starts with
	 * <code>..</code>, it will not be processed.
	 */
	public void copyExternal();
	
	/**
	 * Moves all resources located in the specified directory into this file set's base
	 * location. Resources in this file set will be update with the new locations.
	 * @param base the directory
	 */
	public void moveExternal(Path base);
	
	/**
	 * Internalizes the specified file set path by copying the original resource
	 * into the file set. 
	 * @param path the path
	 */
	public void internalizeCopy(String path);
	
	/**
	 * Internalizes the specified file set path by moving the resource from its
	 * current location into the file set. See also, {@link #internalizeCopy(String)}. 
	 * @param path the path
	 */
	public void internalize(String path);
}
