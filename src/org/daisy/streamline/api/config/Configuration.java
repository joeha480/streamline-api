package org.daisy.streamline.api.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Provides a configuration that can be written to file and subsequently restored.
 * @author Joel Håkansson
 */
class Configuration implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 954333206661166742L;
	private final ConfigurationDetails details;
	private final Map<String, Object> config;
	Configuration(ConfigurationDetails details, Map<String, Object> config) {
		this.details = details;
		this.config = config;
	}
	
	/**
	 * Gets the configuration data.
	 * @return the configuration data
	 */
	Map<String, Object> getMap() {
		return config;
	}
	
	/**
	 * Gets the configuration details.
	 * @return the configuration details
	 */
	ConfigurationDetails getDetails()  {
		return details;
	}
	
	/**
	 * Creates a configuration with the same content as this instance, but
	 * with the specified identifier.
	 * @param identifier the new identifier
	 * @return a new configuration
	 */
	Configuration copyWithIdentifier(String identifier) {
		return new Configuration(details.builder().identifier(identifier).build(), new HashMap<>(config));
	}
	
	/**
	 * Reads a file that was previously written with {@link #write(File)}.
	 * @param f the file
	 * @return returns a new configuration instance based on the contents of the file
	 * @throws FileNotFoundException if the file does not exist, is a directory rather
	 * 			 than a regular file, or for some other reason cannot be opened for reading. 
	 * @throws IOException if an I/O problem occurs.
	 */
	static Configuration read(File f) throws FileNotFoundException, IOException {
		try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(f))) {
			return (Configuration)is.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException("Failed to deserialize.", e);
		}
	}
	
	/**
	 * Writes the configuration to file.
	 * @param f the file
	 * @throws FileNotFoundException if the file does not exist, is a directory rather
	 * 			 than a regular file, or for some other reason cannot be opened for reading. 
	 * @throws IOException if an I/O problem occurs.
	 */
	void write(File f) throws FileNotFoundException, IOException {
		try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(f))) {
			os.writeObject(this);
		}
	}
	
// XML serialization
	
	private static final String NS = "http://brailleapps.github.io/ns/config";
	private static final QName ROOT_ELEMENT = new QName(NS, "configuration");
	private static final QName DESCRIPTION_ELEMENT = new QName(NS, "description");
	private static final QName ENTRIES_ELEMENT = new QName(NS, "entries");
	private static final QName ENTRY_ELEMENT = new QName(NS, "entry");
	private static final QName ATTR_KEY = new QName("key");
	private static final QName ATTR_VALUE = new QName("value");
	private static final QName ATTR_NICE_NAME = new QName("name");
	private static final String XML_ENCODING = "utf-8";
	
	static Configuration read(File f, XMLStreamFactoriesProvider provider) throws XMLStreamException, IOException {
		try (InputStream is = new FileInputStream(f)) {
			XMLEventReader reader = provider.newXMLInputFactory().createXMLEventReader(is, XML_ENCODING);
			XMLEvent event;
			Optional<ConfigurationDetails.Builder> details = Optional.empty();
			Map<String, Object> config = new HashMap<>();
			while (reader.hasNext()) {
				event = reader.nextEvent();
				if (event.isStartElement() && event.asStartElement().getName().equals(ROOT_ELEMENT)) {
					StartElement el = event.asStartElement();
					details = Optional.ofNullable(el.getAttributeByName(ATTR_KEY))
						.map(key->new ConfigurationDetails.Builder(key.getValue())
								.niceName(
									Optional.ofNullable(el.getAttributeByName(ATTR_NICE_NAME))
										.map(v->v.getValue())
										.orElse(""))
						);
				} else if (event.isStartElement() && event.asStartElement().getName().equals(DESCRIPTION_ELEMENT)) {
					// The description has to be parsed, even if there isn't a details object
					String desc = readDescription(event, reader);
					details.ifPresent(v->v.description(desc));
				} else if (event.isStartElement() && event.asStartElement().getName().equals(ENTRIES_ELEMENT)) {
					while (reader.hasNext()) {
						event = reader.nextEvent();
						if (event.isStartElement() && event.asStartElement().getName().equals(ENTRY_ELEMENT)) {
							StartElement el = event.asStartElement();
							Optional.ofNullable(el.getAttributeByName(ATTR_KEY))
								.map(v->v.getValue())
								.ifPresent(key->{
									config.put(key, Optional.ofNullable(el.getAttributeByName(ATTR_VALUE))
											.map(v->v.getValue())
											.orElse(""));
							});
							scanEmptyElement(event, reader);
						} else if (event.isEndElement() && event.asEndElement().getName().equals(ENTRIES_ELEMENT)) {
							break;
						} else {
							ignoreOrThrowValidationError(event);
						}
					}
				} else if (event.isEndElement() && event.asEndElement().getName().equals(ROOT_ELEMENT)) {
					break;
				} else {
					ignoreOrThrowValidationError(event);
				}
			}
			if (details.isPresent()) {
				return new Configuration(details.get().build(), config);
			} else {
				throw new IOException("Failed to parse");
			}
		}
	}
	
	private static String readDescription(XMLEvent event, XMLEventReader reader) throws XMLStreamException, IOException {
		StringBuilder sb = new StringBuilder();
		while (reader.hasNext()) {
			event = reader.nextEvent();
			if (event.isCharacters()) {
				sb.append(event.asCharacters().getData());
			} else if (event.isEndElement() && event.asEndElement().getName().equals(DESCRIPTION_ELEMENT)) {
				break;
			} else {
				ignoreOrThrowValidationError(event);
			}
		}
		return sb.toString().trim();
	}
	
	private static void scanEmptyElement(XMLEvent event, XMLEventReader reader) throws XMLStreamException, IOException {
		while (reader.hasNext()) {
			// Process empty element
			event = reader.nextEvent();
			if (event.isEndElement() && event.asEndElement().getName().equals(ENTRY_ELEMENT)) {
				break;
			} else {
				ignoreOrThrowValidationError(event);
			}
		}
	}
	
	private static void ignoreOrThrowValidationError(XMLEvent event) throws IOException {
		if (event.isCharacters() && !"".equals(event.asCharacters().getData().trim()) || event.isStartElement() || event.isEndElement()) {
			throw new IOException("Unexpected event: " + event.toString());
		}
	}
	
	/**
	 * Returns true if this configuration can be safely written as XML, false otherwise.
	 * A configuration can be safely written to XML if a configuration that has been written
	 * with {@link #write(File, XMLStreamFactoriesProvider)} can be restored to an equivalent
	 * state using {@link #read(File, XMLStreamFactoriesProvider)}.
	 * @return true if the configuration can be written safely to XML, false otherwise
	 */
	boolean isSerializableAsXml() {
		return !config.values().stream()
				.filter(value->!(value instanceof String))
				.findAny()
				.isPresent();
	}
	
	void write(File f, XMLStreamFactoriesProvider provider) throws XMLStreamException, IOException {
		try (OutputStream os = new FileOutputStream(f)) {
			XMLEventWriter writer = provider.newXMLOutputFactory().createXMLEventWriter(os, XML_ENCODING);
			XMLEventFactory eventFactory = provider.newXMLEventFactory();
			writer.add(eventFactory.createStartDocument(XML_ENCODING));
			writer.add(eventFactory.createCharacters("\n"));
			List<Namespace> rootNsList = Arrays.asList(eventFactory.createNamespace(NS));
			writer.add(eventFactory.createStartElement(ROOT_ELEMENT, null, rootNsList.iterator()));
			writer.add(eventFactory.createAttribute(ATTR_KEY, details.getKey()));
			writer.add(eventFactory.createAttribute(ATTR_NICE_NAME, details.getNiceName()));
			writer.add(eventFactory.createCharacters("\n"));
			writer.add(eventFactory.createStartElement(DESCRIPTION_ELEMENT, null, null));
			writer.add(eventFactory.createCharacters(details.getDescription()));
			writer.add(eventFactory.createEndElement(DESCRIPTION_ELEMENT, null));
			writer.add(eventFactory.createCharacters("\n"));
			writer.add(eventFactory.createStartElement(ENTRIES_ELEMENT, null, null));
			writer.add(eventFactory.createCharacters("\n"));
			for (Entry<String, Object> entry : config.entrySet()) {
				writer.add(eventFactory.createStartElement(ENTRY_ELEMENT, null, null));
				writer.add(eventFactory.createAttribute(ATTR_KEY, entry.getKey()));
				writer.add(eventFactory.createAttribute(ATTR_VALUE, entry.getValue().toString()));
				writer.add(eventFactory.createEndElement(ENTRY_ELEMENT, null));
				writer.add(eventFactory.createCharacters("\n"));
			}
			writer.add(eventFactory.createEndElement(ENTRIES_ELEMENT, null));
			writer.add(eventFactory.createCharacters("\n"));
			writer.add(eventFactory.createEndElement(ROOT_ELEMENT, rootNsList.iterator()));
			writer.add(eventFactory.createEndDocument());
			writer.close();
		}
	}

}
