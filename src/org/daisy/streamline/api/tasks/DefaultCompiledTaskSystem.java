package org.daisy.streamline.api.tasks;

import java.util.ArrayList;
import java.util.List;

import org.daisy.streamline.api.option.UserOption;

/**
 * Provides a default implementation of a compiled task system
 * @author Joel Håkansson
 *
 */
public class DefaultCompiledTaskSystem extends ArrayList<InternalTask> implements CompiledTaskSystem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5627095028137227428L;
	private List<UserOption> options;
	private final String name;
	
	/**
	 * Creates a new compiled task system with the specified name
	 * @param name the name of the task system
	 */
	public DefaultCompiledTaskSystem(String name) {
		this(name, new ArrayList<UserOption>());
	}

	/**
	 * Creates a new compiled task system with the specified name and options
	 * @param name the name of the task system
	 * @param options the options
	 */
	public DefaultCompiledTaskSystem(String name, List<UserOption> options) {
		super();
		this.options = new ArrayList<>(options);
		this.name = name;
	}
	
	/**
	 * Adds an option to the list of options
	 * @param option the option to add
	 */
	public void addOption(UserOption option) {
		options.add(option);
	}

	@Override
	public List<UserOption> getOptions() {
		return options;
	}

	@Override
	public String getName() {
		return name;
	}

}
