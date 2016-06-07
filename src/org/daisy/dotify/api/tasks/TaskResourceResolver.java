package org.daisy.dotify.api.tasks;

import java.io.File;

public interface TaskResourceResolver {

	public File resolve(String path);
}
