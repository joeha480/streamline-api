package org.daisy.streamline.api.media;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Optional;

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
	public void testCommonAncestor_01() {
		assertEquals(Paths.get("a"), DefaultFileSet.findCommonAncestor(Arrays.asList(Paths.get("a/a.txt"), Paths.get("a/b.txt"))).get());
	}
	
	@Test
	public void testCommonAncestor_02() {
		assertEquals(Paths.get("a/b"), DefaultFileSet.findCommonAncestor(Arrays.asList(Paths.get("a/b/c/d/x.txt"), Paths.get("a/b/d/y.txt"))).get());
	}
	
	@Test
	public void testCommonAncestor_03() {
		assertEquals(Optional.empty(), DefaultFileSet.findCommonAncestor(Arrays.asList(Paths.get("x.txt"), Paths.get("y.txt"))));
	}
	
	@Test
	public void testCommonAncestor_04() {
		assertEquals(Optional.empty(), DefaultFileSet.findCommonAncestor(Arrays.asList(Paths.get("C:\\a\\x.txt"), Paths.get("D:\\a\\y.txt"))));
	}
	
	@Test
	public void testCommonAncestor_05() {
		assertEquals(Paths.get("a"), DefaultFileSet.findCommonAncestor(Arrays.asList(Paths.get("a/a.txt"))).get());
	}
	
	private static DefaultFileSet buildFileSet() throws URISyntaxException {
		return new DefaultFileSet.Builder(
				DefaultAnnotatedFile.with(Paths.get(DefaultFileSetTest.class.getResource("resource-files/a/manifest.mf").toURI())).build(),
				DefaultAnnotatedFile.with(Paths.get(DefaultFileSetTest.class.getResource("resource-files/b/resource1.txt").toURI())).build(),
				DefaultAnnotatedFile.with(Paths.get(DefaultFileSetTest.class.getResource("resource-files/b/c/resource2.txt").toURI())).build()
			).build();
	}
	
	@Test
	public void testBuildFileSet_01() throws URISyntaxException {
		DefaultFileSet fs = buildFileSet();
		assertTrue(fs.getBaseFolder().getPath().endsWith("resource-files"));
		assertTrue(fs.isManifest("a/b/../manifest.mf"));
	}

	@Test
	public void testCopy() throws URISyntaxException, IOException {
		BaseFolder copy1Folder = BaseFolder.with("build", "test", "copy1");
		BaseFolder copy2Folder = BaseFolder.with("build", "test", "copy2");
		// Copy file set to a second location
		FileSet fs1 = DefaultFileSet.copy(buildFileSet(), copy1Folder);
		// Test the number of external resources (nothing)
		assertEquals(0, fs1.streamExternal().map(v->v.getPath()).count());
		// Test the number of existing resources
		assertEquals(3, fs1.getResourcePaths().stream().map(v->fs1.getResource(v).get()).filter(v->Files.exists(v.getPath())).count());

		DefaultFileSet.Builder fs2Builder = new DefaultFileSet.Builder(copy2Folder, fs1.getManifest(), "a/manifest.mf");
		// Link resources in the first file set without moving them
		for (String path : fs1.getResourcePaths()) {
			fs1.getResource(path).ifPresent(f->fs2Builder.add(f, path));
		}
		// Build the file set
		DefaultFileSet fs2 = fs2Builder.build();
		
		// Test the number of external resources (everything)
		assertEquals(3, fs2.streamExternal().map(v->v.getPath()).count());
		
		// Move resources below the original file sets path "b"
		fs2.internalizeBelow(fs1.getBaseFolder().getPath().resolve("b"));
		
		// Test the number of existing resources in the first file set
		assertEquals(1, fs1.getResourcePaths().stream().map(v->fs1.getResource(v).get()).filter(v->Files.exists(v.getPath())).count());

		// The external resources should now be only a/manifest.mf 
		assertEquals(1, fs2.streamExternal().map(v->v.getPath()).count());
		assertTrue(fs2.streamExternal().findFirst().get().getPath().endsWith("a/manifest.mf"));
		
		// Clean up
		FileVisitor<Path> deleteVisitor =  new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
					throws IOException
			{
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException e)
					throws IOException
			{
				if (e == null) {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				} else {
					// directory iteration failed
					throw e;
				}
			}
		};
		Files.walkFileTree(copy1Folder.getPath(), deleteVisitor);
		Files.walkFileTree(copy2Folder.getPath(), deleteVisitor);
	}

}
