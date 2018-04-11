package org.daisy.streamline.api.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;

public class ConfigurationTest {
	
	@Test
	public void testSerialization() throws IOException {
		Map<String, Object> props = new HashMap<>();
		props.put("key-1", "value-1");
		props.put("key-2", "value-2");
		props.put("key-3", "value-3");
		Configuration c = new Configuration(new ConfigurationDetails.Builder("id-1").description("desc").niceName("ID 1").build(), props);
		File f = File.createTempFile("test", ".tmp");
		f.deleteOnExit();
		c.write(f);

		Configuration c2 = Configuration.read(f);
		assertEqualConfig(c, c2);
	}
	
	@Test
	public void testXMLSerialization() throws FileNotFoundException, XMLStreamException, IOException {
		Map<String, Object> props = new HashMap<>();
		props.put("key-1", "value-1");
		props.put("key-2", "value-2");
		props.put("key-3", "value-3");
		Configuration c = new Configuration(new ConfigurationDetails.Builder("id-1").description("desc").niceName("ID 1").build(), props);
		File f = new File("out.xml");
		XMLStreamFactoriesProvider provider = new XMLStreamFactoriesProvider() {
			
			@Override
			public XMLOutputFactory newXMLOutputFactory() {
				return XMLOutputFactory.newInstance();
			}
			
			@Override
			public XMLEventFactory newXMLEventFactory() {
				return XMLEventFactory.newInstance();
			}

			@Override
			public XMLInputFactory newXMLInputFactory() {
				return XMLInputFactory.newInstance();
			}
		}; 
		assertTrue(c.isSerializableAsXml());
		c.write(f, provider);

		Configuration c2 = Configuration.read(f, provider);
		assertEqualConfig(c, c2);
	}
	
	private static void assertEqualConfig(Configuration c, Configuration c2) {
		// Manually check equality, because equals() in ConfigurationDetails does not check all fields.
		assertEquals(c.getDetails().getKey(), c2.getDetails().getKey());
		assertEquals(c.getDetails().getNiceName(), c2.getDetails().getNiceName());
		assertEquals(c.getDetails().getDescription(), c2.getDetails().getDescription());
		assertEquals(c.getMap(), c2.getMap());
	}

}