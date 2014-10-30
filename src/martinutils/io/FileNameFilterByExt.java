package martinutils.io;

import java.io.*;

/**
 * Implements a Filename filter which filters file by extension
 * @author Martin
 *
 */
public class FileNameFilterByExt implements FilenameFilter
{
	protected String extension = "";
	
	/**
	 * Pass extension without dot
	 * @param ext
	 */
	public FileNameFilterByExt(String ext)
	{
		if (ext == null)
			throw new IllegalArgumentException("extension cannot be null");
		if (ext.startsWith("."))
			ext = ext.substring(1);
		if (ext.isEmpty())
			throw new IllegalArgumentException("extension invalid");
		extension = ext;
	}
	
	@Override
	public boolean accept(File dir, String name)
	{
		String regex = String.format(".+\\.%s$", extension);
		return name.matches(regex);
	}
	
}
