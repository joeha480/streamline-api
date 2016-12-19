package org.daisy.dotify.consumer.identify;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.daisy.dotify.api.identity.IdentityProviderService;
import org.daisy.dotify.api.tasks.AnnotatedFile;
import org.daisy.dotify.consumer.identity.IdentityProvider;
import org.junit.Test;
@SuppressWarnings("javadoc")
public class IdentityProviderTest {
	
	@Test
	public void testXML_01() {
		IdentityProviderService id = IdentityProvider.newInstance();
		AnnotatedFile f = id.identify(new File("test/resource-files/test.xml"));
		assertEquals("text/xml", f.getMediaType());
	}
	
	@Test
	public void testXML_02() {
		IdentityProviderService id = IdentityProvider.newInstance();
		AnnotatedFile f = id.identify(new File("test/resource-files/test.qqq"));
		// the default implementation returns null here, so we know it's not 
		// reading the file to determine the type. If this changes, then maybe
		// have a look at the IdentityProvider implementation
		assertEquals(null, f.getMediaType());
	}
	
	@Test
	public void testUnknown() {
		//Tests an unknown file type (unknown to the default java implementation)
		IdentityProviderService id = IdentityProvider.newInstance();
		AnnotatedFile f = id.identify(new File("test/resource-files/unknown.unk"));
		assertEquals("application/test", f.getMediaType());
	}

}
