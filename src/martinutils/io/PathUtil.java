package martinutils.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class PathUtil
{
	/**
	 * Reads a text file with UTF8 encoding, removing the BOM at the start, if any
	 * @param file the file to read
	 * @return a string with the contents of the file
	 * @throws IOException
	 */
	public static String readUTF8File(Path file) throws IOException	{
		return readUTF8File(file, true);
	}
	
	/**
	 * Reads a text file with UTF8 encoding
	 * @param file file the file to read
	 * @param removeBOM should the BOM character at the start of the file be removed, if present?
	 * @return a string with the contents of the file
	 * @throws IOException
	 */
	public static String readUTF8File(Path file, boolean removeBOM) throws IOException 	{
		String fileContent = readTextFile(file, "UTF-8");
		
		if (removeBOM && fileContent.length() > 0 && fileContent.charAt(0) == BOM)
			fileContent = fileContent.substring(1);
		
		return fileContent;
	}
	
	//public static String readTextFile(Path file) throws IOException
	//{
	//	return FileUtils.readFileToString(file); // try to guess charset?
	//}
	
	/**
	 * Read a text file by specifying the encoding
	 * @param file the file to read
	 * @param charset the charset name (eg: UTF-8, iso-8859-1 etc...) or null to use platform default
	 * @return
	 * @throws IOException
	 */
	public static String readTextFile(Path file, String charset) throws IOException {
		
		byte[] encoded = Files.readAllBytes(file);
		return charset != null ? new String(encoded, Charset.forName(charset)) : new String(encoded);
	}
	
	/**
	 * Read a text file trying to guess encoding (no guarantees)
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public static String readTextFileAutodetectEncoding(Path file) throws IOException {
		
		return FileUtil.readTextFileAutoDetectEncoding(file.toFile());
	}
	
	//public static void saveTextFile(Path file, String fileContent) throws IOException
	//{
	//	Files.write(file, fileContent.getBytes()); // what charset?
	//}
	
	/**
	 * Save a string as a UTF-8 text file
	 * @param file the path in which to save the file
	 * @param fileContent the file content
	 * @throws IOException
	 */
	public static void saveUtf8File(Path file, String fileContent) throws IOException {
		saveTextFile(file, fileContent, "UTF-8");
	}
	
	/**
	 * Save a string in a text file by specifying the charset
	 * @param file the path in which to save the file
	 * @param fileContent the file content
	 * @param charset the charset name (eg: UTF-8, iso-8859-1 etc...)
	 * @throws IOException
	 */
	public static void saveTextFile(Path file, String fileContent, String charset) throws IOException {
		Files.write(file, fileContent.getBytes(charset)); 
	}
	
	/**
	 * Open a BufferedWriter to write a UTF-8 text file
	 * @param file the file to be created (if it already exists, it will be deleted and created from scratch)
	 * @return a BufferedWriter
	 * @throws IOException
	 */
	public static BufferedWriter getUTF8Writer(Path file) throws IOException {
		if (Files.exists(file)) {
			Files.delete(file);
		}
		return Files.newBufferedWriter( file, Charset.forName("UTF-8"), StandardOpenOption.CREATE_NEW);
	}
	
	/**
	 * Open a BufferedReader to read from a UTF-8 text file
	 * @param file the file to read
	 * @return a BufferedReader
	 * @throws IOException
	 */
	public static BufferedReader getUTF8Reader(Path file) throws IOException {
		return Files.newBufferedReader( file, Charset.forName("UTF-8"));
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
	 * Read all lines from a UTF-8 file and removing the UTF-8 byte order mark character
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public static List<String> readUTF8Lines(Path file) throws IOException {
		List<String> lines = Files.readAllLines(file, Charset.forName("UTF-8"));
		if (lines.size() > 0) {
			String str = lines.get(0);
			if (str.charAt(0) == BOM) { 
				lines.set(0, str.substring(1));
			}
		}
		return lines;
	}
	
	/**
	 * Crea un BufferedReader con l'autodetect del charset. Se il charset non è riconoscibile viene usato quello di default del sistema
	 * @param fileName
	 * @return
	 * @throws IOException 
	 */
	public static BufferedReader newBufferedReader(Path file) throws IOException {
		Charset cs = FileUtil.tryDetectEncoding(file.toFile().getAbsolutePath());
		return java.nio.file.Files.newBufferedReader(file, cs);
	}
	
	/**
	 * Utility filter for streaming XML files from a directory
	 */
	public static final Filter<Path> xmlFileFilter = new Filter<Path>() {
		@Override public boolean accept(Path entry) throws IOException {
			return entry.getFileName().toString().endsWith(".xml");
		}
	};
}
