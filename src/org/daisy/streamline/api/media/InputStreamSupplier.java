package org.daisy.streamline.api.media;

import java.io.IOException;
import java.io.InputStream;

/**
 * Provides an supplier of input streams for a resource.
 * 
 * @author Joel Håkansson
 */
public interface InputStreamSupplier {

	/**
	 * Creates a new input stream for the resource.
	 * @return the input stream
	 * @throws IOException if an I/O error occurs
	 */
	public InputStream newInputStream() throws IOException;

	/**
	 * <p>Gets the system identifier. The system identifier can be included in error messages and warnings.</p>
	 * <p>If the system identifier is a URL, it can also be used to resolve links to relative resources.
	 * However, one should keep in mind that the content of the resource at the specified URL might not be identical to 
	 * the content supplied by {@link #newInputStream()}. The URL might even point to a non-existing resource.
	 * For these reasons, a consumer should avoid opening a connection to the URL and instead use {@link #newInputStream()}.</p>
	 * <p>If the system identifier is a URL, it must be fully resolved (it may not be a relative URL).</p>
	 * @return the system identifier
	 */
	public String getSystemId();

}
