package org.daisy.streamline.api.media;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.daisy.streamline.api.tasks.AnnotatedFile;
import org.daisy.streamline.api.tasks.DefaultAnnotatedFile;

public final class FileSet {
	private final File baseFolder;
	private final Map<FilePath, FileLocation> paths;
	private final Map<FileLocation, AnnotatedFile> resources;
	private AnnotatedFile manifest;

	public FileSet(File baseFolder) {
		this.baseFolder = baseFolder;
		this.paths = new HashMap<>();
		this.resources = new HashMap<>();
	}

	/**
	 * Sets the manifest of this file set.
	 * @param manifest the manifest
	 */
	public void setManifest(AnnotatedFile manifest) {
		this.manifest = manifest;
	}

	/**
	 * Adds the annotated file to the file set.
	 * @param f the file
	 * @throws IllegalArgumentException if the file is not a child of //TODO
	 */
	public void add(AnnotatedFile f) {
		FileLocation loc = new FileLocation(f.getFile());
		paths.put(toPath(f.getFile()), loc);
		resources.put(loc, f);
	}

	public void add(File f) {
		//TODO: require existing file?
		add(DefaultAnnotatedFile.create(f));
	}
	
	public void remove(AnnotatedFile f) {
		remove(f.getFile());
	}

	public void remove(File f) {
		resources.remove(new FileLocation(f));
	}

	public Collection<AnnotatedFile> getResources() {
		return resources.values();
	}
	
	public Optional<AnnotatedFile> getResource(String path) {
		return Optional.empty();
	}

	public AnnotatedFile getManifest() {
		return manifest;
	}
	
	/**
	 * Copies all external resources into this file set. All external
	 * resources in this file set will be updated with their new locations.
	 */
	public void copyExternal() {
		
	}

	/**
	 * Moves all resources located in the specified directory into this file set's base
	 * location. Resources in this file set will be update with the new locations.
	 * @param base the directory
	 */
	public void moveExternal(File base) {
		
	}
	
	/**
	 * Moves all resources located in the specified file set into this file set's base
	 * location, regardless of their current locations. Resources in this file set 
	 * will be update with the new locations. The resources in other file set will
	 * be removed. 
	 * @param other
	 */
	public void moveExternal(FileSet other) {
		
	}

	private FilePath toPath(File f) {
		try {
			return new FilePath(baseFolder.toPath().relativize(f.toPath()).toString());
		} catch (InvalidPathException e) {
			throw new IllegalArgumentException();
		}
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
