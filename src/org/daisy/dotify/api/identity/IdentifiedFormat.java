package org.daisy.dotify.api.identity;

import java.util.Collection;
import java.util.List;

public interface IdentifiedFormat {

	/**
	 * Gets a list of format types. For each item in the list, the type is more
	 * specialized than the previous. Every item in the list except the first 
	 * item must conform to the rules associated with the preceding item.
	 * @return returns a list of format types
	 */
	public List<FormatType> getFormatTypes();
	
	/**
	 * Gets the principal file type for this format. This
	 * file is sometimes referred to as a manifest, as
	 * it often provides a list of references to supplementary
	 * files in the format.
	 * 
	 * @return returns the principal file type
	 */
	public FileType getPrincipalFile();
	
	/**
	 * Gets a collection of supplementary files associated 
	 * with the principal file.
	 * @return returns a collection of additional files
	 */
	public Collection<FileType> getSupplementaryFiles();

}
