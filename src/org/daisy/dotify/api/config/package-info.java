/**
 * <p>
 * Provides means for adding configuration catalogs. A configuration catalog
 * is a map with a title and a description. The title and description
 * are used for selecting a configuration and the map contains
 * key/value combinations for that configuration.
 * </p>
 * <p>
 * A catalog can be added by creating a new java project, implementing
 * <code>ConfigurationsProvider</code> and adding the file
 * <code>org.daisy.dotify.api.config.ConfigurationsProvider</code> to the
 * META-INF/services folder. This file should contain a qualified reference to
 * the new implementation. Package the result as a jar-file and included in the
 * classpath.
 * </p>
 * @author Joel Håkansson
 */
package org.daisy.dotify.api.config;