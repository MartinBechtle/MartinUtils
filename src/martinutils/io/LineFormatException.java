package martinutils.io;

import java.io.IOException;

public class LineFormatException extends IOException
{
	private static final long serialVersionUID = 1L;

	public LineFormatException(int lineNum, String fileName, int colsLen)
	{
		this( String.format("Line %d on file %s has an unsupported number of columns: %d", lineNum, fileName, colsLen) );
	}
	
	public LineFormatException(String str)
	{
		super(str);
	}
}
