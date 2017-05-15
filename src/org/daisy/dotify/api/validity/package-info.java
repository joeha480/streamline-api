/**
 * <p>Provides interfaces and classes needed to implement validators.</p>
 * <p>
 * A validator can be added by creating a new java project, implementing
 * <code>ValidatorFactory</code> and adding the file
 * <code>org.daisy.dotify.api.validity.ValidatorFactory</code> to the
 * META-INF/services folder. This file should contain a qualified reference to
 * the new implementation. Package the result as a jar-file and included in the
 * classpath.
 * </p>
 * @author Joel Håkansson
 */
package org.daisy.dotify.api.validity;