package org.daisy.dotify.api.tasks;

import java.util.List;

public interface TaskMaterialDescriptor {
	
	public enum Validity {
		VALID,
		INVALID,
		UNKNOWN;
	}
	
	/**
	 * Gets the entry point
	 * @return
	 */
	public AnnotatedFile getManifest();
	
	/**
	 * Gets all file members, including the manifest. Any file not in this list may be purged by the task runner
	 * when the task has completed.
	 * @return returns all file members
	 */
	public List<AnnotatedFile> getMembers();
	
	/**
	 * One or more resource files, excluding manifest files
	 * @return
	 */
	//public List<File> getResources();
	
	/**
	 * Returns true if the material is known to be correct, false if it is known NOT to be correct and
	 * null if it is undetermined.
	 * @return
	 */
	public Validity getValidity();

	/**
	 * Gets the format identifier, or null if not known
	 * @return
	 */
	public String getFormat();

	/**
	 * Resolves a relative path found in the manifest file to a member file descriptor
	 * @param path
	 * @return
	 */
	public AnnotatedFile resolveResource(String path);
}
