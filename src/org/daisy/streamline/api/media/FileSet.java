package org.daisy.streamline.api.media;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class FileSet {
	private final File baseFolder;
	private final Map<FilePath, FileLocation> paths;
	private final Map<File, AnnotatedFile> resources;
	private FormatIdentifier formatIdentifier;
	private AnnotatedFile manifest;

	/**
	 * Creates a new file set in the specified folder.
	 * @param baseFolder the folder
	 */
	public FileSet(File baseFolder) {
		this.baseFolder = baseFolder;
		this.paths = new HashMap<>();
		this.resources = new HashMap<>();
		this.manifest = null;
		this.formatIdentifier = null;
	}

	/**
	 * Gets the manifest of this file set.
	 * @return the manifest
	 */
	public Optional<AnnotatedFile> getManifest() {
		return Optional.ofNullable(manifest);
	}

	/**
	 * Sets the manifest of this file set.
	 * @param manifest the manifest
	 */
	public void setManifest(AnnotatedFile manifest) {
		this.manifest = manifest;
	}
	
	/**
	 * Gets the format identifier for this file set.
	 * @return the format identifier
	 */
	public Optional<FormatIdentifier> getFormatIdentifier() {
		return Optional.ofNullable(formatIdentifier);
	}
	
	/**
	 * Sets the format identifier.
	 * @param value the format identifier
	 */
	public void setFormatIdentifier(FormatIdentifier value) {
		this.formatIdentifier = value;
	}
	
	public FileSet copyTo(File copyPath) {
		return null;
		//Files.copy(input.toPath(), this.t1.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	/**
	 * Adds the annotated file to the file set.
	 * @param f the file
	 * @throws IllegalArgumentException if the file is not a child of //TODO
	 */

/*	
	public void add(AnnotatedFile f) {
		FileLocation loc = FileLocation.with(f.getFile());
		paths.put(FilePath.with(baseFolder.toPath(), f.getFile().toPath()), loc);
		resources.put(f.getFile(), f);
	}

	public void add(File f, String path) throws IOException {
		File target = new File(baseFolder, path);
		Files.copy(f.toPath(), target.toPath());
		add(DefaultAnnotatedFile.create(f));
	}

	public void remove(AnnotatedFile f) {
		remove(f.getFile());
	}

	public void remove(File f) {
		resources.remove(FileLocation.with(f));
	}*/

	/**
	 * Gets all resources
	 * @return the resources
	 */
	public Collection<AnnotatedFile> getResources() {
		return resources.values();
	}
	
	/**
	 * Gets the resource at a relative path.
	 * @param path the path
	 * @return returns the resource
	 */
	public Optional<AnnotatedFile> getResource(String path) {
		return Optional.ofNullable(resources.get(new File(baseFolder, path)));
	}
	
	/**
	 * Copies all external resources into this file set. All external
	 * resources in this file set will be updated with their new locations.
	 */
	void copyExternal() {
		
	}

	/**
	 * Moves all resources located in the specified directory into this file set's base
	 * location. Resources in this file set will be update with the new locations.
	 * @param base the directory
	 */
	void moveExternal(File base) {
		
	}
	
	/**
	 * Moves all resources located in the specified file set into this file set's base
	 * location, regardless of their current locations. Resources in this file set 
	 * will be updated with the new locations. The resources in other file set will
	 * be removed. 
	 * @param other
	 */
	void moveExternal(FileSet other) {
		
	}

/*
 * FileSet1
 * base path 		/tmp/123
 * manifest 		/tmp/123/META-INF/manifest.mf
 * manifest path	META-INF/manifest.mf
 * res1				/tmp/123/res/res1.jpg
 * res1 path		res/res1.jpg
 * 
 * FileSet2
 * base path		/tmp/321
 * manifest			/tmp/321/main.xml
 * manifest path	main.xml
 * res1				/tmp/123/res/res1.jpg
 * res1 path		res1.jpg
 *
 * 
 */
}
