package test.impl;

import org.daisy.dotify.api.identity.IdentificationFailedException;
import org.daisy.dotify.api.identity.Identifier;
import org.daisy.dotify.api.identity.IdentifierFactory;
import org.daisy.dotify.api.tasks.AnnotatedFile;
import org.daisy.dotify.api.tasks.DefaultAnnotatedFile;
import org.daisy.dotify.api.tasks.FileDetails;

@SuppressWarnings("javadoc")
public class TestFactory implements IdentifierFactory {

	@Override
	public Identifier newIdentifier() {
		return new Identifier() {@Override
		public AnnotatedFile identify(AnnotatedFile f) throws IdentificationFailedException {
			return DefaultAnnotatedFile.with(f).mediaType("application/test").build();
		}};
	}

	@Override
	public boolean accepts(FileDetails type) {
		return type.getExtension().equals("unk");
	}

	@Override
	public void setCreatedWithSPI() {
	}

}
