package martinutils.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import martinutils.runtime.ExecUtil;

/**
 * Rimuove righe duplicate da un file di testo utf-8
 * @author Martin
 */
public class RemoveDuplicate
{
	public static void main(String[] args) throws IOException
	{
		if (args.length < 2)
			ExecUtil.die("Usage: RemoveDuplicate <input.txt> <output.txt>");
		
		List<String> lines = FileUtil.readUTF8Lines( new File(args[0]) );
		Set<String> uniqueLines = new HashSet<>(lines);
		
		BufferedWriter br = FileUtil.getUTF8Writer( new File(args[1]) );
		for (String str : uniqueLines)
			br.append(str + "\n");
		
		br.close();
		System.out.println("Done");
	}
}
