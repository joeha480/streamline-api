package org.daisy.streamline.api.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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

}
