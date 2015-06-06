package martinutils.io;
import java.io.*;
import java.util.*;

/**
 * Confronta i file presenti in due cartelle, rimuovendo l'estensione. Utile per confrontare nei progetti Merck se tutti gli EPS sono stati trasformati in GIF, per esempio.
 * @author Martin
 */
public class MissingFiles
{
	public static void main(String[] args) 
	{
		if (args.length != 2) {
			System.err.println("Usage: MissingFiles completeDir incompleteDir");
			System.exit(-1);
		}
		
		String completeDirPath 		= args[0];
		String incompleteDirPath 	= args[1];
		
		File completeDir 	= new File(completeDirPath);
		File incompleteDir	= new File(incompleteDirPath);
		
		File[] completeFiles 	= completeDir.listFiles();
		File[] incompleteFiles 	= incompleteDir.listFiles();
		
		Set<String> completeFileNames 	= new HashSet<>();
		Set<String> incompleteFileNames = new HashSet<>();
		
		for (File file : completeFiles)
			completeFileNames.add( getFileNameNoExt(file) );
		
		for (File file : incompleteFiles)
			incompleteFileNames.add( getFileNameNoExt(file) );
		
		completeFileNames.removeAll(incompleteFileNames);
		
		if (completeFileNames.size() > 0)
			for (String str : completeFileNames)
				System.out.println(str);
		else
			System.out.println("No files missing");
	}
	
	private static String getFileNameNoExt(File file)
	{
		String fileName = file.getName();
		fileName = fileName.replaceAll("\\.[a-zA-Z]+$", "");
		return fileName;
	}

}
