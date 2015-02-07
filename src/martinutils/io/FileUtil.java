package martinutils.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class FileUtil
{
	public static String readUTF8File(File file) throws IOException
	{
		return readUTF8File(file, true);
	}
	
	public static String readUTF8File(File file, boolean removeBOM) throws IOException
	{
		String fileContent = FileUtils.readFileToString( file, Charset.forName("UTF-8"));
		
		if (removeBOM && fileContent.length() > 0 && fileContent.charAt(0) == BOM)
			fileContent = fileContent.substring(1);
		
		return fileContent;
	}
	
	public static String readTextFile(File file, String charset) throws IOException
	{
		return FileUtils.readFileToString(file, Charset.forName(charset));
	}
	
	public static String readTextFile(File file) throws IOException
	{
		return FileUtils.readFileToString(file);
	}
	
	public static void saveTextFile(File file, String fileContent) throws IOException
	{
		FileUtils.writeStringToFile(file, fileContent);
	}
	
	public static void saveUtf8File(File file, String fileContent, boolean withBOM) throws IOException
	{
		if (withBOM && !fileContent.startsWith(BOM_STR) )
			FileUtils.writeStringToFile(file, BOM_STR + fileContent, Charset.forName("UTF-8"));
		else
			FileUtils.writeStringToFile(file, fileContent, Charset.forName("UTF-8"));
	}
	
	public static void saveUtf8File(File file, String fileContent) throws IOException
	{
		saveUtf8File(file, fileContent, false);
	}
	
	public static void saveTextFile(File file, String fileContent, String charset) throws IOException
	{
		FileUtils.writeStringToFile(file, fileContent, Charset.forName(charset));
	}
	
	public static BufferedWriter getUTF8Writer(File file) throws IOException
	{
		String filePath = file.getAbsolutePath();
		Path path = FileSystems.getDefault().getPath(filePath);
		
		if (file.exists())
			file.delete();
		
		return java.nio.file.Files.newBufferedWriter( path, Charset.forName("UTF-8"), StandardOpenOption.CREATE_NEW);
	}
	
	public static BufferedReader getUTF8Reader(File file) throws IOException
	{
		String filePath = file.getAbsolutePath();
		Path path = FileSystems.getDefault().getPath(filePath);
		return java.nio.file.Files.newBufferedReader( path, Charset.forName("UTF-8"));
	}
	
	public static String removeBOM(String str)
	{
		if (str.length() > 0 && str.charAt(0) == BOM)
			return str.substring(1);
		return str;
	}
	
	/**
	 * UTF-8 byte order mark, which appears at the start of some utf8 files and the java library does not remove
	 */
	public static final int BOM = 65279;
	
	/**
	 * UTF-8 byte order mark, which appears at the start of some utf8 files and the java library does not remove
	 */
	public static final char BOM_CHAR = 65279;
	
	/**
	 * UTF-8 byte order mark, which appears at the start of some utf8 files and the java library does not remove
	 */
	public static final String BOM_STR = BOM_CHAR + "";
	
	/**
	 * Read all lines from a UTF8 files using Guava library and removing the UTF-8 byte order mark character
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public static List<String> readUTF8Lines(File file) throws IOException
	{
		List<String> lines = FileUtils.readLines(file, Charset.forName("UTF-8"));
		if (lines.size() > 0)
		{
			String str = lines.get(0);
			if (str.charAt(0) == BOM)
				lines.set(0, str.substring(1));
		}
		return lines;
	}
	
	/**
	 * Adds the File.separator char (\ in windows and / in linux) to a string
	 * and removes enclosing quotes
	 */
	public static String getDir(String arg)
	{
		if (arg.startsWith("\"") && arg.endsWith("\"") && arg.length() > 2)
			arg = arg.substring(1, arg.length() - 1);
		
		if (arg.endsWith(File.separator))
			return arg;
		
		return arg + File.separator;
	}
	
	/**
	 * Removes enclosing quotes from a file name (useful in case it was passed as argument)
	 */
	public static String getFile(String arg)
	{
		if (arg.startsWith("\"") && arg.endsWith("\"") && arg.length() > 2)
			arg = arg.substring(1, arg.length() - 1);
		
		return arg;
	}
	
	/**
	 * Restituisce true se l'array di file passati (tipicamente figli di una directory) � vuoto (nullo)
	 * @param files
	 * @return
	 */
	public static boolean isEmptyDir(File[] files)
	{
		return files == null || files.length == 0;
	}
	
	/**
	 * Restituisce il nome di un file senza l'estensione
	 * @param file
	 * @return
	 */
	public static String getFileNameNoExt(File file)
	{
		if (file == null)
			throw new IllegalArgumentException("file cannot be null");
		
		String fileName = file.getName();
		return getFileNameNoExt(fileName);
	}
	
	/**
	 * Rimuove l'estensione dal nome di un file
	 * @param file
	 * @return
	 */
	public static String getFileNameNoExt(String fileName)
	{
		return fileName.replaceAll("\\.[a-zA-Z]+$", "");
	}
	

	
	/**
	 * Check if given file exists, is readable and is not a directory. Throws IOException if conditions are not met
	 * @param file
	 * @throws IOException 
	 */
	public static void checkFileExistReadable(File file) throws IOException
	{
		if (!file.exists())
			throw new FileNotFoundException("File does not exist: " + file);
		if (!file.canRead())
			throw new IOException("File not readable: " + file);
		if (file.isDirectory())
			throw new IOException("File cannot be a directory: " + file);
	}
	
	/**
	 * Check if the given file is a directory, which also is accessible and writable, else throw IOException
	 * @param dir
	 * @throws IOException
	 */
	public static void checkDirExistWritable(File dir) throws IOException
	{
		if (!dir.exists())
			throw new FileNotFoundException("Directory does not exist: " + dir);
		if (!dir.canRead())
			throw new IOException("Dir not accessible: " + dir);
		if (!dir.isDirectory())
			throw new IOException("Not a valid directory: " + dir);
		if (!dir.canWrite())
			throw new IOException("Cannot write in directory: " + dir);
	}
	
	/**
	 * Check if the given file is an accessible directory
	 * @param dir
	 * @throws IOException
	 */
	public static void checkDirExistReadable(File dir) throws IOException
	{
		if (!dir.exists())
			throw new FileNotFoundException("Directory does not exist: " + dir);
		if (!dir.canRead())
			throw new IOException("Dir not accessible: " + dir);
		if (!dir.isDirectory())
			throw new IOException("Not a valid directory: " + dir);
	}
	
	/**
	 * Fa esattamente ciò che fa la funzione File.listFiles, ma restituisce una lista non nulla e filtra solo i file 
	 * @param directory la directory da cui prendere la lista dei file
	 * @param un filtro opzionale
	 * @return
	 */
	private static List<File> listFiles(File directory, FilenameFilter filter)
	{
		if (directory == null)
			throw new IllegalArgumentException("directory cannot be null");
		
		File[] files = filter != null ? directory.listFiles(filter) : directory.listFiles();
		if (files == null)
			files = new File[0];
		
		return Arrays.asList(files);
	}
	
	/**
	 * Fa esattamente ciò che fa la funzione File.listFiles, ma restituisce una lista non nulla
	 * @param directory la directory da cui prendere la lista dei file
	 * @return
	 */
	public static List<File> listFiles(File directory)
	{
		return listFiles(directory, null);
	}
	
	/**
	 * Fa esattamente ciò che fa la funzione File.listFiles, ma restituisce una lista non nulla e prende solo i file XML
	 * @param directory la directory da cui prendere la lista dei file
	 * @return
	 */
	public static List<File> listXmlFiles(File directory)
	{
		return listFiles(directory, new XmlFileFilter());
	}
	
	/**
	 * Fa esattamente ciò che fa la funzione File.listFiles, ma restituisce una lista non nulla e prende solo i file con una determinata estensione
	 * @param directory la directory da cui prendere la lista dei file
	 * @param ext l'estensione (con o senza punto)
	 * @return
	 */
	public static List<File> listFilesByExt(File directory, String ext)
	{
		if (StringUtils.isEmpty(ext))
			throw new IllegalArgumentException("ext cannot be null");
		if (ext.startsWith("."))
			ext = ext.substring(1);
		
		return listFiles(directory, new FileNameFilterByExt(ext));
	}
}
