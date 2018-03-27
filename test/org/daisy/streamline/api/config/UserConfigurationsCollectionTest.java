package org.daisy.streamline.api.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class UserConfigurationsCollectionTest {

	@Test
	public void test() throws IOException {
		UserConfigurationsCollection c = new UserConfigurationsCollection(new File(new File("build"), this.getClass().getName()), new SingletonAccess(){
			@Override
			public boolean acquireLock() throws IOException {
				return true;
			}

			@Override
			public void releaseLock() {
			}});
		System.out.println("-- BEFORE --");
		c.getConfigurationDetails().stream().map(v->v.getKey()).forEach(System.out::println);
		Map<String, Object> t = new HashMap<>();
		t.put("k1", "v1");
		t.put("k2", "v2");
		String id = c.addConfiguration("test1", "desc", t).orElseThrow(RuntimeException::new);
		System.out.println("-- AFTER --");
		c.getConfigurationDetails().stream().map(v->v.getKey()).forEach(System.out::println);
		c.removeConfiguration(id);	
	}
}
