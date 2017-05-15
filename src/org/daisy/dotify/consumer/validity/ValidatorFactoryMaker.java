package org.daisy.dotify.consumer.validity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.daisy.dotify.api.validity.FormatDetails;
import org.daisy.dotify.api.validity.Validator;
import org.daisy.dotify.api.validity.ValidatorFactory;
import org.daisy.dotify.api.validity.ValidatorFactoryException;
import org.daisy.dotify.api.validity.ValidatorFactoryMakerService;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

/**
 * Simple factory for instantiating a Validator based on its identifier 
 * @author Joel Håkansson
 */
@Component
public class ValidatorFactoryMaker implements ValidatorFactoryMakerService {
	private final List<ValidatorFactory> providers;
	private final Map<String, ValidatorFactory> map;
	
	/**
	 * Creates a new empty instance. This method is public because it is required by OSGi.
	 * In an SPI context, use newInstance()
	 */
	public ValidatorFactoryMaker() {
		providers = new CopyOnWriteArrayList<>();
		map = Collections.synchronizedMap(new HashMap<String, ValidatorFactory>());
	}

	/**
	 * <p>
	 * Creates a new ValidatorFactoryMaker and populates it using the SPI
	 * (java service provider interface).
	 * </p>
	 * 
	 * <p>
	 * In an OSGi context, an instance should be retrieved using the service
	 * registry. It will be registered under the ValidatorFactoryMakerService
	 * interface.
	 * </p>
	 * 
	 * @return returns a new ValidatorFactoryMaker
	 */
	public static ValidatorFactoryMaker newInstance() {
		ValidatorFactoryMaker ret = new ValidatorFactoryMaker();
		Iterator<ValidatorFactory> i = ServiceLoader.load(ValidatorFactory.class).iterator();
		while (i.hasNext()) {
			ret.addFactory(i.next());
		}
		return ret;
	}
	
	private void addToMap(ValidatorFactory factory) {
		for (FormatDetails d : factory.list()) {
			map.put(toKey(d), factory);
		}
	}
	
	@SuppressWarnings("javadoc")
	@Reference(type = '*')
	public void addFactory(ValidatorFactory factory) {
		providers.add(factory);
		synchronized(map) {
			addToMap(factory);
		}
	}

	// Unbind reference added automatically from addFactory annotation
	@SuppressWarnings("javadoc")
	public void removeFactory(ValidatorFactory factory) {
		// this is to avoid adding items to the cache that were removed while
		// iterating
		synchronized (map) {
			providers.remove(factory);
			map.clear();
			for (ValidatorFactory f : providers) {
				addToMap(f);
			}
		}
	}

	private String toKey(FormatDetails details) {
		List<String> ret = new ArrayList<>();
		if (details.getFormatName()!=null) {
			ret.add(details.getFormatName());
		}
		if (details.getFormatVersion()!=null) {
			ret.add(details.getFormatVersion());
		}
		if (details.getExtension()!=null) {
			ret.add(details.getExtension());
		}
		if (details.getMediaType()!=null) {
			ret.add(details.getMediaType());
		}
		return ret.stream().collect(Collectors.joining(" : "));
	}
	
	

	/**
	 * Obtains a new instance of a Validator with the given identifier
	 * @param details a string that identifies the desired implementation
	 * @return returns a Validator for the given identifier, or null if none is found
	 */
        @Override
	public Validator newValidator(FormatDetails details) {
		Objects.requireNonNull(details);
		// Using string as key, because FormatDetails is an interface (and hash codes for 
		// implementations may include fields that aren't relevant to this interface,
		// potentially causing false negatives)
		String key = toKey(details);
		synchronized (map) {
			ValidatorFactory template = map.get(key);
			if (template!=null) {
				try {
					return template.newValidator(details);
				} catch (ValidatorFactoryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}