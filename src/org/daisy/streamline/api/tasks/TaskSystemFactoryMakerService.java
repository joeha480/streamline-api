package org.daisy.streamline.api.tasks;

import java.util.Set;

import org.daisy.streamline.api.media.FormatIdentifier;

/**
 * <p>
 * Provides an interface for a TaskSystemFactoryMaker service. The purpose of
 * this interface is to expose an implementation of a TaskSystemFactoryMaker as
 * an OSGi service.
 * </p>
 * 
 * <p>
 * To comply with this interface, an implementation must be thread safe and
 * address both the possibility that only a single instance is created and used
 * throughout and that new instances are created as desired.
 * </p>
 * 
 * @author Joel Håkansson
 * 
 */
public interface TaskSystemFactoryMakerService {
	
	/**
	 * Gets a TaskSystemFactory that supports the specified locale and format.
	 * 
	 * @param inputFormat the input file format
	 * @param outputFormat the output file format
	 * @param locale the target locale
	 * @return returns a task system factory for the specified locale and format
	 * @throws TaskSystemFactoryException if a factory cannot be returned
	 */
	public TaskSystemFactory getFactory(String inputFormat, String outputFormat, String locale) throws TaskSystemFactoryException;
	
	/**
	 *  Gets a task system for the specified output format and context
	 *  @param inputFormat the input file format
	 *  @param outputFormat the output file format
	 *  @param locale the target locale
	 *  @return returns a task system for the specified locale and format
	 *  @throws TaskSystemFactoryException if a task system cannot be returned
	 */
	public TaskSystem newTaskSystem(String inputFormat, String outputFormat, String locale) throws TaskSystemFactoryException;
	
	/**
	 * Lists available input formats.
	 * @return a list of available input formats
	 */
	public Set<FormatIdentifier> listInputs();
	
	/**
	 * Lists available output formats.
	 * @return a list of available output formats
	 */
	public Set<FormatIdentifier> listOutputs();

	/**
	 * Gets a set with information about supported task systems for the specified input format and locale.
	 * @param input the input format
	 * @param locale the locale
	 * @return returns a list of information for the specified locale
	 */
	public Set<TaskSystemInformation> listForInput(FormatIdentifier input, String locale);
	
	/**
	 * Gets a set with information about supported task systems for the specified output format and locale.
	 * @param output the output format
	 * @param locale the locale
	 * @return returns a list of information for the specified locale
	 */
	public Set<TaskSystemInformation> listForOutput(FormatIdentifier output, String locale);
	
}
