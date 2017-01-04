package org.daisy.dotify.consumer.tasks;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.daisy.dotify.api.tasks.TaskGroup;
import org.daisy.dotify.api.tasks.TaskGroupFactory;
import org.daisy.dotify.api.tasks.TaskGroupFactoryMakerService;
import org.daisy.dotify.api.tasks.TaskGroupInformation;
import org.daisy.dotify.api.tasks.TaskGroupSpecification;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

/**
 * Provides a factory maker for input manager factories, that is to say a collection of
 *  
 * @author Joel Håkansson
 */
@Component
public class TaskGroupFactoryMaker implements TaskGroupFactoryMakerService {
	private final List<TaskGroupFactory> filters;
	private final Map<TaskGroupInformation, TaskGroupFactory> map;
	private final Logger logger;

	/**
	 * Creates a new task group factory maker.
	 */
	public TaskGroupFactoryMaker() {
		logger = Logger.getLogger(TaskGroupFactoryMaker.class.getCanonicalName());
		filters = new CopyOnWriteArrayList<>();
		this.map = Collections.synchronizedMap(new HashMap<TaskGroupInformation, TaskGroupFactory>());
	}

	/**
	 * <p>
	 * Creates a new TaskGroupFactoryMaker and populates it using the SPI (java
	 * service provider interface).
	 * </p>
	 * 
	 * <p>
	 * In an OSGi context, an instance should be retrieved using the service
	 * registry. It will be registered under the TaskGroupFactoryMakerService
	 * interface.
	 * </p>
	 * 
	 * @return returns a new TaskGroupFactoryMaker
	 */
	public static final TaskGroupFactoryMaker newInstance() {
		TaskGroupFactoryMaker ret = new TaskGroupFactoryMaker();
		{
			Iterator<TaskGroupFactory> i = ServiceLoader.load(TaskGroupFactory.class).iterator();
			while (i.hasNext()) {
				TaskGroupFactory factory = i.next();
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
	public void addFactory(TaskGroupFactory factory) {
		logger.finer("Adding factory: " + factory);
		filters.add(factory);
	}

	/**
	 * Removes a factory (intended for use by the OSGi framework)
	 * @param factory the factory to remove
	 */
	// Unbind reference added automatically from addFactory annotation
	public void removeFactory(TaskGroupFactory factory) {
		logger.finer("Removing factory: " + factory);
		// this is to avoid adding items to the cache that were removed while
		// iterating
		synchronized (map) {
			filters.remove(factory);
			map.clear();
		}
	}

	private static String toKey(TaskGroupSpecification spec) {
		return new StringBuilder().
				append(spec.getLocale()).
				append(" (").
				append(spec.getInputFormat()).
				append(" -> ").
				append(spec.getOutputFormat()).
				append(")").toString();
	}
	
	@Override
	public TaskGroupFactory getFactory(TaskGroupInformation spec) {
		TaskGroupFactory template = map.get(spec);
		if (template==null) {
			// this is to avoid adding items to the cache that were removed
			// while iterating
			synchronized (map) {
				for (TaskGroupFactory h : filters) {
					if (h.supportsSpecification(spec)) {
						logger.fine("Found a factory for " + spec.toString() + " (" + h.getClass() + ")");
						map.put(spec, h);
						template = h;
						break;
					}
				}
			}
		}
		if (template==null) {
			throw new IllegalArgumentException("Cannot locate an TaskGroup for " + spec.toString());
		}
		return template;
	}
	
	@Override
	public TaskGroup newTaskGroup(TaskGroupSpecification spec) {
		logger.fine("Attempt to locate a task group for " + toKey(spec));
		for (TaskGroupInformation i : listAll()) {
			if (spec.matches(i)) {
				return getFactory(i).newTaskGroup(spec);
			}
		}
		throw new IllegalArgumentException("Cannot find an TaskGroup for " + spec.toString());
	}
	
	@Override
	public TaskGroup newTaskGroup(TaskGroupInformation spec, String locale) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Attempt to locate a task group for " + spec.toString());
		}
		return getFactory(spec).newTaskGroup(spec.toSpecificationBuilder(locale).build());
	}

	@Override
	public Set<TaskGroupInformation> listAll() {
		HashSet<TaskGroupInformation> ret = new HashSet<>();
		for (TaskGroupFactory h : filters) {
			ret.addAll(h.listAll());
		}
		return ret;
	}

	@Override
	public Set<TaskGroupInformation> list(String locale) {
		HashSet<TaskGroupInformation> ret = new HashSet<>();
		for (TaskGroupFactory h : filters) {
			ret.addAll(h.list(locale));
		}
		return ret;
	}

}