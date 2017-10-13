package org.daisy.dotify.api.tasks;


/**
 * Provides an interface for task system factories. A
 * task system factory implementation can provide
 * task system instances for any number of specifications.
 * 
 * @author Joel Håkansson
 */
public interface TaskSystemFactory {
	
	/**
	 * Returns true if this factory can create instances with the desired properties.
	 * @param inputFormat the desired input format
	 * @param outputFormat the desired output format
	 * @param locale the desired locale
	 * @return returns true if this factory can create instances with the desired properties, false otherwise
	 */
	public boolean supportsSpecification(String inputFormat, String outputFormat, String locale);
	
	/**
	 * Creates a new task system with the given properties.
	 * @param inputFormat the desired input format
	 * @param outputFormat the desired output format
	 * @param locale the desired locale
	 * @return returns a new task system
	 * @throws TaskSystemFactoryException if a task system with these properties cannot be created
	 */
	public TaskSystem newTaskSystem(String inputFormat, String outputFormat, String locale) throws TaskSystemFactoryException;
	
	/**
	 * Returns the priority when choosing between several matching task systems.
	 * @return returns the priority.
	 */
	public default int getPriority() {
		return 0;
	}

	/**
	 * <p>Informs the implementation that it was discovered and instantiated using
	 * information collected from a file within the <tt>META-INF/services</tt> directory.
	 * In other words, it was created using SPI (service provider interfaces).</p>
	 * 
	 * <p>This information, in turn, enables the implementation to use the same mechanism
	 * to set dependencies as needed.</p>
	 * 
	 * <p>If this information is <strong>not</strong> given, an implementation
	 * should avoid using SPIs and instead use
	 * <a href="http://wiki.osgi.org/wiki/Declarative_Services">declarative services</a>
	 * for dependency injection as specified by OSGi. Note that this also applies to
	 * several newInstance() methods in the Java API.</p>
	 * 
	 * <p>The class that created an instance with SPI must call this method before
	 * putting it to use.</p>
	 */
	public default void setCreatedWithSPI() {}

}
