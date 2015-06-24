package martinutils.xml;

import java.io.IOException;
import java.nio.file.Paths;

import martinutils.io.FileUtil;
import martinutils.io.PathUtil;
import martinutils.runtime.ExecUtil;

/**
 * Prende in input un file XML. Da esso rimuove tutto il whitespace tra le tag, per poi aggiungere uno \n dopo ogni carattere >
 * Questo serve a dare una formattazione standard in modo che dei file XML possano essere facilmente confrontati
 * @author martin
 */
public class XmlFormatter {

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			ExecUtil.usage(XmlFormatter.class, "<xml_file>");
		}
		String outFile = FileUtil.changeExtension(args[0], "out.xml");
		String fileContent = PathUtil.readUTF8File(Paths.get(args[0]));
		fileContent = fileContent.replaceAll(">[\\n\\t\\r]+<", "><").replaceAll(">", ">\n");
		PathUtil.saveUtf8File(Paths.get(outFile), fileContent);
	}

}
