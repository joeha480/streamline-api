package org.daisy.dotify.consumer.identify;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.daisy.dotify.api.identity.IdentityProviderService;
import org.daisy.dotify.api.tasks.AnnotatedFile;
import org.daisy.dotify.consumer.identity.IdentityProvider;
import org.junit.Ignore;
import org.junit.Test;
@SuppressWarnings("javadoc")
public class IdentityProviderTest {
	
	@Test
	@Ignore ("This test is ignored because it behaves differently on windows and linux.")
	public void testXML_01() {
		IdentityProviderService id = IdentityProvider.newInstance();
		AnnotatedFile f = id.identify(new File("test/resource-files/test.xml"));
		// on windows the default implementation returns text/xml
		assertEquals("text/xml", f.getMediaType());
		// on linux the default implementation returns application/xml
		assertEquals("application/xml", f.getMediaType());
	}
	
	@Test
	@Ignore ("This test is ignored because it behaves differently on windows and linux.")
	public void testXML_02() {
		IdentityProviderService id = IdentityProvider.newInstance();
		AnnotatedFile f = id.identify(new File("test/resource-files/test.qqq"));
		// on windows the default implementation returns null
		assertEquals(null, f.getMediaType());
		// on linux the default implementation returns application/xml
		assertEquals("application/xml", f.getMediaType());
	}
	
	@Test
	public void testUnknown() {
		//Tests an unknown file type (unknown to the default java implementation)
		IdentityProviderService id = IdentityProvider.newInstance();
		AnnotatedFile f = id.identify(new File("test/resource-files/unknown.unk"));
		assertEquals("application/test", f.getMediaType());
	}

}
