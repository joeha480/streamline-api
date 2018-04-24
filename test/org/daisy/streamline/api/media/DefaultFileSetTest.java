package org.daisy.streamline.api.media;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class DefaultFileSetTest {

	@Test
	public void testRelative() {
		Path a = Paths.get("/a/b/c");
		Path b = Paths.get("/a/c/d");
		Path c = Paths.get("/a/c/d/e");	
		assertFalse(DefaultFileSet.isDescendant(a, b));
		assertFalse(DefaultFileSet.isDescendant(b, a));
		assertTrue(DefaultFileSet.isDescendant(b, c));
		assertFalse(DefaultFileSet.isDescendant(c, b));
		
		Path d = Paths.get("/a/b/c");
		Path e = Paths.get("/a/c/../b/c/d");
		assertTrue(DefaultFileSet.isDescendant(d, e));
	}
}
