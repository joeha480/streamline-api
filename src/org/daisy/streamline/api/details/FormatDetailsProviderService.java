package org.daisy.streamline.api.details;

import java.util.List;
import java.util.Optional;

import org.daisy.streamline.api.media.FormatIdentifier;

/**
 * <p>
 * Provides an interface for a FormatDetailsProvider service. The purpose of
 * this interface is to expose an implementation of a FormatDetailsProvider as
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
public interface FormatDetailsProviderService {

	/**
	 * <p>Gets format details for the specified identifier.</p>
	 * 
	 * <p>Note that more than one definition may be available for any given
	 * identifier. The behavior in this case is explicitly nondeterministic.
	 * An implementation is free to select any available definition or even
	 * to merge details from several definitions.</p>
	 * 
	 * @param identifier the format identifier
	 * @return an optional with the format details, or an empty optional if no
	 * 	details could be provided.
	 */
	public Optional<FormatDetails> getDetails(FormatIdentifier identifier);
	
	/**
	 * <p>Gets format details matching the specified file name extension.</p>
	 * 
	 * <p>Note that the returned list may include several {@link FormatDetails}
	 * having the same identifier.</p>
	 * @param ext the file name extension
	 * @return a list of matching format details
	 */
	public List<FormatDetails> findByExtension(String ext);
}
