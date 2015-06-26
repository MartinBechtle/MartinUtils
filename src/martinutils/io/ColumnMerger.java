package martinutils.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Serve ad effettuare il merging delle colonne di file. I file devono essere UTF-8, le colonne separate da \t e le righe da \n.
 * Si passano argomenti in numero pari, ovvero coppie di filename e colonne da estrarre. Le colonne sono numeri interi positivi
 * separati da ; (nb: la prima colonna è n.1, non 0). L'insieme delle colonne selezionate sarà salvato nella directory del primo
 * file passato come argomento, col nome merged.txt
 * Se si passa un solo file come argomento, chiaramente il programma funziona da estrattore e non da merger.
 * NB: il merge si interrompe al primo file che raggiunge l'ultima riga.
 * 
 * @author Martin
 */
public class ColumnMerger
{
	static File[] files;
	static int[][] columns;
	
	public static void main(String[] args) throws IOException
	{		
		// Gli argomenti sono coppie di <nomefile> <colonne>, quindi devono essere in numero pari
		int argsLen = args.length;
		if (argsLen == 0 || (args.length % 2) == 1)
			usage();
		
		// Il numero di file pertanto è la metà del numero di argomenti
		int filesNum = args.length / 2;
		files = new File[filesNum];
		columns = new int[filesNum][];
		
		// Parsing degli argomenti che contengono i nomi file e le colonne
		parseFiles(args, filesNum);
		parseColumns(args, filesNum);
		
		System.out.println("Arguments ok. Merging files...");
		
		// Merge dei file e salvataggio nella stessa directory del primo file
		String mergedFileContent = mergeFiles();
		String directory = files[0].getParentFile().getAbsolutePath();
		File mergedFile = new File( directory + File.separator + "merged.txt");
		FileUtil.saveUtf8File(mergedFile, mergedFileContent);
		
		// No need to close readers and writers since this is a standalone java program
		System.out.println("...Done");
	}
	
	private static String mergeFiles() throws IOException
	{
		int len = files.length;
		BufferedReader[] readers = new BufferedReader[len];
		
		for (int i = 0; i < len; i++)
			readers[i] = FileUtil.getUTF8Reader(files[i]);
		
		StringBuilder writer = new StringBuilder();
		
		boolean condition = true; // si ferma in caso di errore oppure al primo file che restituisce una riga vuota
		int rowNum = 0;
		while (condition)
		{
			// per ogni file leggo la (rowNum)-esima riga
			++rowNum;
			int lenMinus1 = len - 1;
			for (int i = 0; i < len; i++)
			{
				String line = readers[i].readLine();

				// Se una riga di qualunque file è null, si interrompe il ciclo
				if (line == null) {
					condition = false;
					break;
				}
				

				if (rowNum > 1 && i == 0)
					writer.append("\n");
				
				// splitto la riga in colonne e verifico che siano in numero giusto (devono essere ALMENO quante indicate nel numero di estrazione più alto)
				int colsNum = columns[i].length;
				int colsMax = getHighest(i);
				
				String[] splitRow = line.split("\t");
				int splitRowLen = splitRow.length;
				if (splitRowLen < colsMax)
				{
					if (splitRowLen < colsMax - 1) // c'è sicuramente un errore
						fatalErr("File " + files[i] + " at row " + rowNum + ": wrong number of columns, expected at least " + colsMax);
					else // manca solo l'ultima colonna... può essere che sia una stringa nulla, la aggiungo
					{
						int newSplitLen = splitRowLen + 1;
						String[] newSplit = new String[newSplitLen];
						for (int z = 0; z < splitRowLen; z++)
							newSplit[z] = splitRow[z];
						newSplit[splitRowLen] = "";
						splitRow = newSplit;
					}
				}
				
				// estraggo le dovute colonne fino alla penultima
				int loopLen = colsNum - 1;
				int columnToExtract;
				for (int j = 0; j < loopLen; j++)
				{
					columnToExtract = columns[i][j] - 1;
					writer.append( splitRow[columnToExtract] + "\t");
				}
				
				// tratto l'ultima
				columnToExtract = columns[i][loopLen] - 1;
				writer.append( splitRow[columnToExtract]);
				
				// va omesso il \t finale soltanto se siamo all'ultima colonna di tutti i file e non del corrente
				if (i < lenMinus1)
					writer.append("\t");
			}
			
			if (!condition)
				break;
		}
		
		// Se per errore ci fosse il \n finale si rimouve
		String result = writer.toString();
		/*int resLenMinusOne = result.length() - 1;
		if (result.charAt(resLenMinusOne) == '\n')
			result = result.substring(0, resLenMinusOne);*/
		
		return result;
	}
	
	private static Map<Integer, Integer> highestColsMap = new HashMap<>();
	
	private static int getHighest(int i)
	{
		Integer result = highestColsMap.get(i);
		if (result == null)
		{
			int highest = 0;
			for (int z : columns[i])
				if (z > highest)
					highest = z;
			result = highest;
			highestColsMap.put(i, result);
		}
		return result;
	}
	
	private static void parseFiles(String[] args, int filesNum)
	{
		// Creo gli oggetti File
		for (int i = 0; i < filesNum; i++)
		{
			String fileName = args[i*2]; // l'i-esima stringa nomefile è alla (2i)-esima posizione degli argomenti 
			File file = new File(fileName);
			
			// Controlli vari
			if (!file.exists())
				fatalErr("File " + file + " not found");
			if (!file.isFile())
				fatalErr("File " + file + " is not a file");
			if (!file.canRead())
				fatalErr("File " + file + " cannot be read");
			
			// Tutto a posto, si aggiunge il file all'array
			files[i] = file;
		}
	}
		
	private static void parseColumns(String[] args, int filesNum)
	{
		for (int i = 0; i < filesNum; i++)
		{
			String columnsStr = args[i*2+1]; // l'i-esima stringa descrittore delle colonne è alla (2i + 1)-esima posizione degli argomenti 
			String[] splitted = columnsStr.split(";");
			
			// Splitto la stringa sul separatore ; e conto il numero di colonne
			int colsNum = splitted.length;
			if (colsNum == 0)
				usage();
			
			// Creo un array di interi nell'i-esimo indice di columns e cerco di popolarlo
			columns[i] = new int[colsNum];
			int j = 0;
			for (String str : splitted)
			{
				int colNum = 0;
				try { colNum = Integer.parseInt(str); }
				catch(NumberFormatException e) { usage(); }
				
				if (colNum < 1)
					usage();
				
				columns[i][j] = colNum;
				++j;
			}
		}
	}
	
	private static void usage()
	{
		System.out.println("Usage: <filename> <columns> [ <filename> <columns> [...] ]");
		System.out.println("Columns format example: 1;2;24;52 (not 0-based, first column is n.1");
		System.exit(-1);
	}
	
	private static void fatalErr(String msg)
	{
		System.err.println(msg);
		System.exit(-1);
	}
}
