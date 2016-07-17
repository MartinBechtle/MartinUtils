package martinutils.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Legge righe e colonne da file tsv (ovvero separatore colonne tab)
 * @author Martin
 */
public class LineReader implements Iterable<String[]>
{
	private String 			fileName;
	private List<String> 	lines;
	private List<String[]> 	columnList;
	private Set<Integer> 	constraints = null;
	private String 			colSeparatorRegex = "\t";
	
	public LineReader(Path inputFile) throws IOException
	{
		this(inputFile.toFile());
	}
	
	public LineReader(File inputFile) throws IOException
	{
		if (inputFile == null)
			throw new IllegalArgumentException("inputFile cannot be null");
		
		lines 		= FileUtil.readUTF8Lines(inputFile);
		fileName 	= inputFile.getName();
		columnList 	= new ArrayList<>();
	}
	
	public LineReader setColSeparator(String regex)
	{
		this.colSeparatorRegex = regex;
		return this;
	}
	
	public LineReader(Path inputFile, String charset) throws IOException
	{
		this(inputFile.toFile(), charset);
	}
	
	public LineReader(File inputFile, String charset) throws IOException
	{
		if (inputFile == null)
			throw new IllegalArgumentException("inputFile cannot be null");
		if (StringUtils.isEmpty(charset))
			throw new IllegalArgumentException("charset cannot be empty");
		
		lines 		= FileUtils.readLines(inputFile, Charset.forName(charset));
		fileName 	= inputFile.getName();
	}
	
	public LineReader setExpectedCols(int minCols, int maxCols) throws LineFormatException
	{
		if (minCols > maxCols)
			throw new IllegalArgumentException("minCols cannot be > maxCols");
		if (minCols < 1)
			throw new IllegalArgumentException("minCols must be > 0");
		
		constraints = new HashSet<>();
		for (int i = minCols; i <= maxCols; i++)
			constraints.add(i);
		
		return this;
	}
	
	public LineReader setExpectedCols(int cols) throws LineFormatException
	{
		if (cols < 1)
			throw new IllegalArgumentException("cols must be > 0");
		
		constraints = new HashSet<>();
		constraints.add(cols);
		
		return this;
	}
	
	public LineReader setExpectedCols(Integer[] cols) throws LineFormatException
	{
		constraints = new HashSet<>( Arrays.asList(cols) );
		return this;
	}
	
	public LineReader readLines() throws LineFormatException
	{
		columnList = new ArrayList<>();
		int lineNum = 0;
		
		for (String line : lines)
		{
			String[] cols = line.split(colSeparatorRegex);
			int colsLen = cols.length;
			if (line.matches("^.+" + colSeparatorRegex + "$")) { // if line ends with col separator, add a column
				cols = Arrays.copyOf(cols, ++colsLen);
				cols[colsLen - 1] = "";
			}
			if (constraints != null && !constraints.contains(cols.length))
				throw new LineFormatException(lineNum, fileName, colsLen);
			
			columnList.add(cols);
			lineNum++;
		}

		constraints = null;
		return this;
	}

	public List<String[]> getColumns()
	{
		return columnList;
	}
	
	@Override
	public Iterator<String[]> iterator()
	{
		return columnList.iterator();
	}
	
	public int size()
	{
		return columnList.size();
	}
}
