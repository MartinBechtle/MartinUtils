package martinutils.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import martinutils.runtime.ExecUtil;
import martinutils.text.StringUtil;

/**
 * Divide un file di testo (diviso in righe) in tanti file da meno di 1mb
 * @author Martin
 */
public class FileSplitter
{
	public static final int DEFAULT_SPLIT_SIZE = 900000; 
	
	/**
	 * Divide un file di testo in tanti file da meno di 1mb, nella stessa sua directory
	 * @param args il file da splittare
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		int len = args.length;
		if (len < 1 || len > 2)
			ExecUtil.die("Usage: FileSplitter file_path [size]");
		
		FileSplitter splitter = new FileSplitter();
		
		String filePath = args[0];
		if (len == 2)
		{
			String size = args[1];
			Integer _size = StringUtil.tryParseInt(size);
			if (_size != null)
				splitter.setSplitSize(_size);
		}
		
		splitter.split(filePath);
		System.out.println("Done");
	}
	
	public FileSplitter()
	{
		splitSize = DEFAULT_SPLIT_SIZE;
	}
	
	public FileSplitter(int splitSizeBytes)
	{
		setSplitSize(splitSizeBytes);
	}
	
	public FileSplitter(String splitSizeBytes)
	{
		Integer _size = StringUtil.tryParseInt(splitSizeBytes);
		if (_size == null)
			throw new IllegalArgumentException("splitsize is not a valid int");
		
		setSplitSize(_size);
	}
	
	public void setSplitSize(int splitSizeBytes)
	{
		if (splitSizeBytes < 100000)
			throw new RuntimeException("Are you really sure you want to split in less than 100kb files?");
		
		splitSize = splitSizeBytes;
	}
	
	public void split(String filePath) throws IOException
	{
		split( new File(filePath) );
	}
	
	@SuppressWarnings("resource")
	public void split(File file) throws IOException
	{
		BufferedReader reader = FileUtil.getUTF8Reader(file);
		
		String fileName = file.getName();
		int pos = fileName.lastIndexOf('.');
		fileExt = fileName.substring(pos);
		fileNameNoExt = fileName.substring(0, pos);
		fileRootDir = file.getParent();
		
		BufferedWriter writer = getNewWriter(null);
		
		int bytes = 0;
		String line = null;
		while ((line = reader.readLine()) != null)
		{
			int len = line.length();
			bytes += len;
			writer.append(line);
			writer.append("\n");
			
			if (bytes > splitSize) {
				writer = getNewWriter(writer); // qui ci sarebbe un resource leak se dentro il metodo il vecchio writer non venisse chiuso
				bytes = 0;
			}
		}
		
		writer.close();
	}
	
	private int splitSize;
	public static int fileCounter = 0;
	public static String fileNameNoExt;
	public static String fileExt;
	public static String fileRootDir;
	
	private static BufferedWriter getNewWriter(BufferedWriter oldWriter) throws IOException
	{
		if (oldWriter != null)
			oldWriter.close();
		
		fileCounter++;
		String _fileCounter = fileCounter > 9 ? "" + fileCounter : "0" + fileCounter;
		String newChunkPath = fileRootDir + File.separator + fileNameNoExt + _fileCounter + fileExt;
		File newFile = new File(newChunkPath);
		return FileUtil.getUTF8Writer(newFile);
	}

}
