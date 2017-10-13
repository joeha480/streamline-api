package org.daisy.streamline.api.identify;

import org.daisy.streamline.api.identity.IdentificationFailedException;
import org.daisy.streamline.api.identity.Identifier;
import org.daisy.streamline.api.identity.IdentifierFactory;
import org.daisy.streamline.api.tasks.AnnotatedFile;
import org.daisy.streamline.api.tasks.DefaultAnnotatedFile;
import org.daisy.streamline.api.tasks.FileDetails;

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
