package org.daisy.streamline.api.config;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class InventoryTest {
	
	@Test
	public void testSerialization() throws IOException {
		Inventory c = new Inventory();
		c.nextIdentifier(); c.nextIdentifier(); c.nextIdentifier();
		File f = File.createTempFile("test", ".tmp");
		f.deleteOnExit();
		c.write("test", f);

		Inventory c2 = Inventory.read(f);
		// Manually check equality
		assertEquals(c.getIndex(), c2.getIndex());
		assertEquals(3l, c2.getIndex());
	}

}