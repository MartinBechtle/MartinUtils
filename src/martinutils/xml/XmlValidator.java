package martinutils.xml;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import martinutils.io.FileUtil;
import martinutils.runtime.ExecUtil;

/**
 * Verifies that all XML files in a directory are well-formed. Please use DtdValidator from Merck if you also wish to validate against dtd rules.
 * @author martin
 */
public class XmlValidator {

	public static void main(String...args) throws ParserConfigurationException, IOException {
		
		if (args.length != 1) {
			ExecUtil.usage(XmlValidator.class, "<directory_with_xml_files>");
		}
		File directory = new File(args[0]);
		List<File> xmlFiles = FileUtil.listXmlFiles(directory);
		Locale.setDefault(new Locale("en", "EN")); // better to see english validation errors
		
		for (File xmlFile : xmlFiles) { 
			try {
				XmlUtility.readXmlFile(xmlFile);
			}
			catch (SAXException e) {
				System.err.println(xmlFile.getName() + " is not well formed");
				System.err.println(e.getMessage() + "\n");
			}
		}
	}
}
