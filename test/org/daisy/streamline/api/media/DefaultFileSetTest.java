package org.daisy.streamline.api.media;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

@SuppressWarnings("javadoc")
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
	
	@Test
	public void testCommonDirectory_01() {
		DefaultFileSet f = new DefaultFileSet.Builder(BaseFolder.with(new File("a")), DefaultAnnotatedFile.with(new File("a/a.txt")).build())
				.add(new File("a/b.txt"))
				.build();
		assertEquals(Paths.get("a"), f.findCommonDirectory().get());
	}
	
	@Test
	public void testCommonDirectory_02() {
		DefaultFileSet f = new DefaultFileSet.Builder(BaseFolder.with(new File("a/b/c")), DefaultAnnotatedFile.with(new File("a/b/c/d/x.txt")).build())
				.add(new File("a/b/d/y.txt"), "a/b/d/y.txt")
				.build();
		assertEquals(Paths.get("a/b"), f.findCommonDirectory().get());
	}
}
