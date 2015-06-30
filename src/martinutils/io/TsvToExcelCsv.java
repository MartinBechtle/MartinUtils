package martinutils.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import martinutils.runtime.ExecUtil;

/**
 * Converte uno o più file Tsv utf8 (tab-separated values) in Csv (comma-separated values) in formato ISO.
 * Attenzione, questa classe è incompleta. Non è da usare come input utf8 poichè non c'è garanzia che l'output ISO sia valido
 * Serve ad aprire comodamente i tsv con MS Excel.
 * @author martin
 */
public class TsvToExcelCsv {

	public static void main(String[] args) throws IOException, LineFormatException {

		if (args.length != 1) {
			ExecUtil.usage(TsvToExcelCsv.class, "<input>\ninput can be either a file or a directory. If a directory is passed, the program is executed on all files");
		}
		
		File input = new File(args[0]);
		if (input.isDirectory()) {
			
			List<File> files = FileUtil.listFilesByExt(input, "tsv");
			for (File file : files) {
				convert(file);
			}
		}
		else {
			convert(input);
		}
		System.out.println("Done");
	}

	private static void convert(File tsvFile) throws LineFormatException, IOException {
		
		System.out.println("Converting " + tsvFile);
		LineReader reader = new LineReader(tsvFile).readLines();
		LineWriter writer = new LineWriter().setOutputAsExcelCsv();
		
		for (String cols[] : reader) {
			writer.printLine(cols);
		}
		File csvFile = new File( FileUtil.changeExtension(tsvFile.getAbsolutePath(), "csv") );
		writer.saveToFile(csvFile, CharSets.ISO);
	}
}
