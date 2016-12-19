/**
 * Provides an API for identifying file formats. This API
 * enhances {@link java.nio.file.Files#probeContentType(java.nio.file.Path)}
 * in two ways:
 * <ol>
 * <li>it supports OSGi runtime context in addition to SPI context</li>
 * <li>it supports sub type identification (commonly used with e.g. XML)</li>
 * </ol>
 * 
 * <h1>Compatibility with {@link java.nio.file.spi.FileTypeDetector}</h1>
 * In an SPI environment, all implementations of {@link java.nio.file.spi.FileTypeDetector}  
 * will be available through this API. In an OSGi environment, the default implementation
 * of {@link java.nio.file.spi.FileTypeDetector} will be available as
 * well as any implementations provided by the package that is using this API.
 * 
 * @author Joel Håkansson
 */
package org.daisy.dotify.api.identity;