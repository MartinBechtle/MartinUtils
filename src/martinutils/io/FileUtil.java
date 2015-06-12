package martinutils.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.mozilla.universalchardet.UniversalDetector;

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
	 * Restituisce true se l'array di file passati (tipicamente figli di una directory) è vuoto (nullo)
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
	 * Cambia l'estensione al nome di un file
	 * @param fileName il nome del file
	 * @param la nuova estensione, con o senza il punto
	 * @return il nome del file con l'estensione cambiata
	 */
	public static String changeExtension(String fileName, String newExt)
	{
		String fileNameNoExt = getFileNameNoExt(fileName);
		if (!newExt.startsWith("."))
			newExt = "." + newExt;
		
		return fileNameNoExt + newExt;
	}
	
	/**
	 * Restituisce l'estensione di un file, controllandone il nome
	 * @param file
	 * @return stringa vuota se non ha alcuna estensione
	 */
	public static String getFileExt(File file)
	{
		if (file == null)
			throw new IllegalArgumentException("file cannot be null");
		
		return getFileExt(file.getName());
	}
	
	/**
	 * Restituisce l'estensione di un file
	 * @param fileName il nome del file
	 * @return stringa vuota se non ha alcuna estensione
	 */
	public static String getFileExt(String fileName)
	{
		if (StringUtils.isEmpty(fileName))
			return "";

		int i = fileName.lastIndexOf(".");
		int lastIndex = fileName.length() - 1;
		
		if (i < 0)
			return "";
		if (i >= lastIndex)
			return "";
		
		++i;
		return fileName.substring(i);
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
	 * Check if given file exists, is readable and is not a directory. 
	 * @param file
	 * @return an error message if an error occurs, else null
	 */
	public static String checkFileExistsReadableSoft(File file)
	{
		if (!file.exists())
			return "File does not exist: " + file;
		if (!file.canRead())
			return "File not readable: " + file;
		if (file.isDirectory())
			return "File cannot be a directory: " + file;
		
		return null;
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
	
	/**
	 * Inserisce in una mappa tutti i file trovati nella directory
	 * @param directory la directory da cui prendere i file
	 * @return una hashmap la cui chiave è il nome del file (stringa) e il valore l'oggetto file
	 */
	public static Map<String, File> mapFiles(File directory)
	{
		return mapXmlFiles(directory, null);
	}
	
	/**
	 * Inserisce in una mappa tutti i file XML trovati nella directory
	 * @param directory la directory da cui prendere i file
	 * @return una hashmap la cui chiave è il nome del file (stringa) e il valore l'oggetto file
	 */
	public static Map<String, File> mapXmlFiles(File directory)
	{
		return mapXmlFiles(directory, new XmlFileFilter());
	}
	
	/**
	 * Inserisce in una mappa tutti i file di un certo tipo trovati nella directory
	 * @param directory la directory da cui prendere i file
	 * @param ext l'estensione dei file da prendere
	 * @return una hashmap la cui chiave è il nome del file (stringa) e il valore l'oggetto file
	 */
	public static Map<String, File> mapFilesByExt(File directory, String ext)
	{
		if (StringUtils.isEmpty(ext))
			throw new IllegalArgumentException("ext cannot be null");
		if (ext.startsWith("."))
			ext = ext.substring(1);
		
		return mapXmlFiles(directory, new FileNameFilterByExt(ext));
	}
	
	/**
	 * Inserisce in una mappa tutti i file trovati nella directory
	 * @param directory la directory da cui prendere i file
	 * @param filter un filtro opzionale
	 * @return una hashmap la cui chiave è il nome del file (stringa) e il valore l'oggetto file
	 */
	private static Map<String, File> mapXmlFiles(File directory, FilenameFilter filter)
	{
		List<File> files = FileUtil.listFiles(directory, filter);
		Map<String, File> filesMap = new HashMap<>(); // mappa <nomefile, file> di tutti i file nella inputDir
		
		for (File file : files)
			filesMap.put(file.getName(), file);
		
		return filesMap;
	}
	
	/**
	 * Prova a indovinare l'encoding di un file
	 * @param fileName il path del file da analizzare
	 * @return il Charset detectato oppure quello default di sistema se non detectato
	 */
	public static Charset tryDetectEncoding(String fileName)
	{
		byte[] buf = new byte[4096];
	    
		UniversalDetector detector = null;
		try (FileInputStream fis = new FileInputStream(fileName))
		{
			detector = new UniversalDetector(null);

		    int nread;
		    while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
		      detector.handleData(buf, 0, nread);
		    }
		}
		catch (IOException e) {
			// ignore
		}
		finally {
			if (detector != null) detector.dataEnd();
		}
		
		if (detector != null)
		{
			String encoding = detector.getDetectedCharset();
		    detector.reset();
		    
		    if ( !StringUtils.isEmpty(encoding))
		    	return Charset.forName(encoding);
		}
	    
	    return Charset.defaultCharset();
	}
	
	/**
	 * Crea un BufferedReader con l'autodetect del charset. Se il charset non è riconoscibile viene usato quello di default del sistema
	 * @param fileName
	 * @return
	 * @throws IOException 
	 */
	public static BufferedReader newBufferedReader(File file) throws IOException
	{
		String filePath = file.getAbsolutePath();
		Path path = FileSystems.getDefault().getPath(filePath);
		Charset cs = tryDetectEncoding(filePath);
		return java.nio.file.Files.newBufferedReader(path, cs);
	}
}
