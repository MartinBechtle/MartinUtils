package martinutils.io;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Implementation of {@link java.io.FilenameFilter} for .xml filename filtering
 */
public class XmlFileFilter implements FilenameFilter
{
	/**
	 * Filter out all .xml files
	 */
	public boolean accept(File dir, String name)
	{
		return name.matches(".+\\.xml$");
	}
}

