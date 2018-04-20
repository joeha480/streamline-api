package org.daisy.streamline.api.config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;
@SuppressWarnings("javadoc")
public class UserConfigurationsCollectionTest {

	@Test
	public void test() throws IOException {
		File catalog = new File(new File("build"), this.getClass().getName());
		// delete the catalog file, to make sure it is empty to begin with
		catalog.delete();
		ExclusiveAccess ea = Mockito.mock(ExclusiveAccess.class);
		Mockito.when(ea.acquire()).thenReturn(true);
		UserConfigurationsCollection c = new UserConfigurationsCollection(catalog, ea);
		assertTrue(c.getConfigurationDetails().isEmpty());
		Map<String, Object> t = new HashMap<>();
		t.put("k1", "v1");
		t.put("k2", "v2");
		String id = c.addConfiguration("name", "desc", t).orElseThrow(RuntimeException::new);
		assertFalse(c.getConfigurationDetails().isEmpty());
		assertTrue(c.containsConfiguration(id));
		c.removeConfiguration(id);
		assertTrue(c.getConfigurationDetails().isEmpty());
	}
}
