package org.daisy.streamline.api.media;

import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * <p>Provides a file set. In its simplest form, a file set is a set of resources located in a hierarchy
 * beneath a root location. Ideally, the hierarchy only contains resources that are part of the file set.</p>
 * 
 * <h2>Paths</h2>
 * <p>Each resource has a path string associated with it. The path string is relative to the
 * base folder. Ideally, the resolved path represents the actual location of the resource. However, 
 * sometimes the path string doesn't match the actual location of the resource.</p>
 * 
 * <h2>The manifest</h2>
 * <p>The manifest is the primary file of a file set, typically the one a user would
 * select as the source of a conversion. Sometimes this file also contains the primary content, such as in
 * HTML, but other times it really is just a manifest, such as in ePub 3.</p>
 * <p>The manifest doesn't have to be directly inside the base folder, it might be in another location.</p>
 * 
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
	 * Gets the path to the manifest within the file set.
	 * @return the path to the manifest
	 */
	public String getManifestPath();
	
	/**
	 * Returns true if the specified path is the manifest, false otherwise.
	 * @param path the path
	 * @return true if the path is the manifest, false otherwise
	 */
	public boolean isManifest(String path);
	
	/**
	 * Gets the format identifier for the file set.
	 * @return the identifier
	 */
	public Optional<FormatIdentifier> getFormatIdentifier();
	
	/**
	 * Gets all resource paths in the file set.
	 * @return a set of resource paths
	 */
	public Set<String> getResourcePaths();
	
	/**
	 * Gets the resource for the specified key. This method is intended
	 * to be used together with {@link #getResourcePaths()}.
	 * @param key the resource key
	 * @return the resource
	 */
	public Optional<AnnotatedFile> getResourceForKey(String key);
	
	/**
	 * Gets the resource at the specified relative path. Unlike {@link #getResourceForKey(String)},
	 * this method may perform some normalization on the provided path in order
	 * to find the resource.
	 * @param path the path
	 * @return the resource
	 */
	public Optional<AnnotatedFile> getResource(String path);
	
	/**
	 * Gets the resource at the specified path.
	 * @param path the path to the resource
	 * @return the resource
	 * @throws IllegalArgumentException if the path cannot be relativized against the base folder
	 */
	public Optional<AnnotatedFile> getResource(Path path);
	
	/**
	 * Opens a stream to external resources.
	 * @return a stream of external resources
	 */
	public Stream<AnnotatedFile> streamExternal();

}
