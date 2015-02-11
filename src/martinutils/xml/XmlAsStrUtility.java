package martinutils.xml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

/**
 * Questa classe contiene metodi utili per trattare XML sotto forma di testo
 * @author martin
 */
public class XmlAsStrUtility
{
	public static final String DUPLICATE_NODE = "DUPLICATE_NODE";
	
	private String xmlStr;
	
	/**
	 * Istanzia una nuova utility per lavorare su una stringa di XML (tipicamente letta da un file)
	 * @param xmlStr la stringa su cui lavorare: non nulla e non vuota
	 */
	public XmlAsStrUtility(String xmlStr, boolean validate)
	{
		if ( StringUtils.isEmpty(xmlStr))
			throw new IllegalArgumentException("xmlStr cannot be empty");
		
		this.xmlStr = xmlStr;
	}
	
	/**
	 * Verifica se la stringa xml rappresenta XML valido. Richiede un certo overhead, usare solo se necessario
	 * @return
	 */
	public boolean isValidXml()
	{
		try {
			XmlUtility.readXml(this.xmlStr);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Cerca un elemento nell'XML in base al nome e in base ad un attributo che lo possa identificare univocamente
	 * @param elementName il nome dell'elemento XML desiderato
	 * @param attribute il nome dell'attributo identificatore
	 * @param value il valore dell'attributo identificatore
	 * @return Se il nodo viene trovato, restituisce una stringa con il contenuto testuale del nodo, <strong><em>incluse</em></strong> le tag di apertura e chiusura del nodo stesso.
	 * Se il nodo non viene trovato, stringa vuota. Se il nodo non è univocamente identificato, ma se ne matchano diversi in base ai criteri scelti stringa DUPLICATE_NODE (costante in questa classe)
	 */
	public String getElementAsStr(String elementName, String attribute, String value)
	{
		return "";
	}
	
	/**
	 * Cerca un elemento nell'XML in base al nome e in base ad un attributo che lo possa identificare univocamente
	 * @param elementName il nome dell'elemento XML desiderato
	 * @param attribute il nome dell'attributo identificatore
	 * @param value il valore dell'attributo identificatore
	 * @return Se il nodo viene trovato, restituisce una stringa con il contenuto testuale del nodo, <strong><em>escluse</em></strong> le tag di apertura e chiusura del nodo stesso.
	 * Se il nodo non viene trovato, stringa vuota. Se il nodo non è univocamente identificato, ma se ne matchano diversi in base ai criteri scelti stringa DUPLICATE_NODE (costante in questa classe)
	 */
	public String getElementContentAsStr(String elementName, String attribute, String value)
	{
		return "";
	}
}
