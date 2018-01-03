/**
 * <p>Provides an API for tasks. This package defines components necessary
 * to assemble a file format conversion dynamically at runtime.</p>
 * 
 * <p><code>TaskGroup</code> is used to implement a task. This can in turn
 * be used by a <code>TaskSystem</code> to assemble a file format conversion.
 * Both these interfaces return a list of <code>InternalTask</code>s. An
 * <code>InternalTask</code> represents a file format conversion unit, which
 * can be one of several types:</p>
 * <ul>
 * <li>Read Only - this type is typically used for validation</li>
 * <li>Read/Write - this type can be a conversion or enhancement</li>
 * <li>An "expanding" task - this is not a task in it self, but a task 
 * that expands into other tasks at the time of execution. The purpose of this
 * type is that it allows the actual input file to influence which tasks
 * are run. This makes it possible to, for example, provide a generic task for
 * XML which performs different tasks based on the XML namespace of the 
 * root element</li>
 * </ul>
 * <p>Note that a <code>TaskSystem</code> resolving the tasks and performing the
 * actions needs to use reflexion to determine what action to take since the
 * <code>InternalTask</code> interface doesn't provide a way to execute the
 * task.</p>
 * 
 * @author Joel Håkansson
 */
package org.daisy.streamline.api.tasks;