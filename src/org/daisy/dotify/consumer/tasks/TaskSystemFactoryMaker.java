package org.daisy.dotify.consumer.tasks;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import org.daisy.dotify.api.tasks.TaskSystem;
import org.daisy.dotify.api.tasks.TaskSystemFactory;
import org.daisy.dotify.api.tasks.TaskSystemFactoryException;
import org.daisy.dotify.api.tasks.TaskSystemFactoryMakerService;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;


/**
 * Entry point for retrieving a TaskSystem implementation. This class will
 * locate all TaskSystemFactory implementations available to the java
 * services API.
 * 
 * @author Joel Håkansson
 *
 */
@Component
public class TaskSystemFactoryMaker implements TaskSystemFactoryMakerService {
	private final List<TaskSystemFactory> filters;
	private final Map<String, TaskSystemFactory> map;
	private final Logger logger;

	/**
	 * Creates a new task system factory maker.
	 */
	public TaskSystemFactoryMaker() {
		logger = Logger.getLogger(TaskSystemFactoryMaker.class.getCanonicalName());
		filters = new CopyOnWriteArrayList<>();
		this.map = Collections.synchronizedMap(new HashMap<String, TaskSystemFactory>());
	}

	/**
	 * <p>
	 * Creates a new TaskSystemFactoryMaker and populates it using the SPI (java
	 * service provider interface).
	 * </p>
	 * 
	 * <p>
	 * In an OSGi context, an instance should be retrieved using the service
	 * registry. It will be registered under the TaskSystemFactoryMakerService
	 * interface.
	 * </p>
	 * 
	 * @return returns a new TaskSystemFactoryMaker
	 */
	public static TaskSystemFactoryMaker newInstance() {
		TaskSystemFactoryMaker ret = new TaskSystemFactoryMaker();
		{
			for (TaskSystemFactory factory : ServiceLoader.load(TaskSystemFactory.class)) {
				factory.setCreatedWithSPI();
				ret.addFactory(factory);
			}
		}
		return ret;
	}
	
	/**
	 * Adds a factory (intended for use by the OSGi framework)
	 * @param factory the factory to add
	 */
	@Reference(type = '*')
	public void addFactory(TaskSystemFactory factory) {
		logger.finer("Adding factory: " + factory);
		filters.add(factory);
	}

	/**
	 * Removes a factory (intended for use by the OSGi framework)
	 * @param factory the factory to remove
	 */
	// Unbind reference added automatically from addFactory annotation
	public void removeFactory(TaskSystemFactory factory) {
		logger.finer("Removing factory: " + factory);
		// this is to avoid adding items to the cache that were removed while
		// iterating
		synchronized (map) {
			filters.remove(factory);
			map.clear();
		}
	}
	
	private static String toKey(String inputFormat, String outputFormat, String context) {
		return context + "(" + inputFormat + "->" + outputFormat + ")";
	}

	@Override
	public TaskSystemFactory getFactory(String inputFormat, String outputFormat, String locale)
			throws TaskSystemFactoryException {
		String key = toKey(inputFormat, outputFormat, locale);
		TaskSystemFactory template = map.get(key);
		Integer matchedPriority = null;
		if (template==null) {
			for (TaskSystemFactory h : filters) {
				if (h.supportsSpecification(inputFormat, outputFormat, locale)) {
					int currentPriority = h.getPriority();
					if (matchedPriority==null || matchedPriority<currentPriority) {
						matchedPriority = currentPriority;
						logger.fine("Found a factory for " + locale + " (" + h.getClass() + ")");
						map.put(key, h);
						template = h;
					}
				}
			}
		}
		if (template==null) {
			throw new TaskSystemFactoryException("Cannot locate a TaskSystemFactory for " + key);
		}
		return template;
	}

	@Override
	public TaskSystem newTaskSystem(String inputFormat, String outputFormat, String locale)
			throws TaskSystemFactoryException {
		return getFactory(inputFormat, outputFormat, locale).newTaskSystem(inputFormat, outputFormat, locale);
	}
}