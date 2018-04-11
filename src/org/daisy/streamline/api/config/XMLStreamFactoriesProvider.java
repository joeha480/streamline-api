package org.daisy.streamline.api.config;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

interface XMLStreamFactoriesProvider {
	
	public XMLInputFactory newXMLInputFactory();

	public XMLOutputFactory newXMLOutputFactory();
	
	public XMLEventFactory newXMLEventFactory();
}
