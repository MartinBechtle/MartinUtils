package martinutils.io;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import martinutils.runtime.ExecUtil;

/**
 * Confronta una directory "complete" con una "incomplete" per determinare quali file mancano nella incomplete per essere identica alla complete. 
 * Dopodichè i file mancanti vengono copiati dalla complete nella directory incomplete/missing/
 * @author Martin
 */
public class MissingFilesRecovery
{
	public static void main(String[] args) throws IOException 
	{
		if (args.length != 2)
			ExecUtil.die("Usage: MissingFilesRecovery completeDir incompleteDir");
		
		String   completeDirPath 	= FileUtil.getDir( args[0] );
		String incompleteDirPath 	= FileUtil.getDir( args[1] );
		
		File   completeDir 	= new File(completeDirPath);
		File incompleteDir	= new File(incompleteDirPath);
		
		File[]   completeFiles 	=   completeDir.listFiles();
		File[] incompleteFiles 	= incompleteDir.listFiles();
		
		Set<String>   completeFileNames = new HashSet<>();
		Set<String> incompleteFileNames = new HashSet<>();
		
		for (File file : completeFiles)
			completeFileNames.add( file.getName() );
		
		for (File file : incompleteFiles)
			incompleteFileNames.add( file.getName() );
		
		completeFileNames.removeAll(incompleteFileNames);
		
		if (completeFileNames.size() > 0)
		{
			String outputDirPath 	= incompleteDirPath + "missing" + File.separator;
			File outputDir 			= new File(outputDirPath);
			
			if (!outputDir.exists())
				outputDir.mkdir();
			
			for (String fileName : completeFileNames)
			{
				File file 			= new File( completeDirPath + fileName);
				File destination 	= new File(   outputDirPath + fileName);
				
				if ( !file.exists( ))
					ExecUtil.die("Unexpected bug: file to copy does not exist");
				
				if ( destination.exists() )
					ExecUtil.die("Unexpected bug: destination already exists");
				
				System.out.println("Copying " + fileName);
				FileUtils.copyFileToDirectory(file, new File(outputDirPath));
			}
		}
		else
			System.out.println("No files missing");
	}
}
