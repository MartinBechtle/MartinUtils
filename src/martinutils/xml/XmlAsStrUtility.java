package martinutils.xml;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

/**
 * Questa classe contiene metodi utili per trattare XML sotto forma di testo
 * @author martin, angelo
 */
public class XmlAsStrUtility
{
	public static final String DUPLICATE_NODE = "DUPLICATE_NODE";
	
	private String xmlStr;
	
	/**
	 * Istanzia una nuova utility per lavorare su una stringa di XML (tipicamente letta da un file)
	 * @param xmlStr la stringa su cui lavorare: non nulla e non vuota
	 */
	public XmlAsStrUtility(String xmlStr)
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
		return getElementOrContentAsStr(elementName, attribute, value, false);
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
		return getElementOrContentAsStr(elementName, attribute, value, true);
	}
	
	/**
	 * Cerca un elemento nell'XML in base al nome e in base ad un attributo che lo possa identificare univocamente
	 * 
	 * @param elementName il nome dell'elemento XML desiderato
	 * @param attribute il nome dell'attributo identificatore
	 * @param value il valore dell'attributo identificatore
	 * @param onlyContent flag che fa escludere il tag stesso dalla stringa
	 * @return Se il nodo viene trovato, restituisce una stringa con il contenuto testuale del nodo. Può contenere le tag di apertura e chiusura del nodo stesso in base al flag onlyContent.
	 */
	private String getElementOrContentAsStr(String elementName, String attribute, String value, boolean onlyContent)
	{
		if (elementName == null || attribute == null || value == null)
			throw new IllegalArgumentException();
		if ("".equals(elementName) || "".equals(attribute))
			throw new IllegalArgumentException();		
		
		String beginningElementStr 	= String.format("<%s ?[^>]* +%s ?= ?\"%s\" ?[^>]*>",
										Pattern.quote(elementName), Pattern.quote(attribute), Pattern.quote(value) );
		
		String genericElementStr 	= String.format("</? ?%s[^>]*>",
										Pattern.quote(elementName)) ;
		
		Pattern beginningElement 	= Pattern.compile(beginningElementStr);
		Pattern genericElement 		= Pattern.compile(genericElementStr);
		
		Matcher beginningMatcher 	= beginningElement.matcher(xmlStr);
		
		// cerca l'elemento di apertura voluto se non c'è restituiamo vuoto
		if (! beginningMatcher.find())
			return "";
		
		int startIdx 		= beginningMatcher.start();
		int endIdx 			= beginningMatcher.end();
		int contentStartIdx = endIdx;
		int contentEndIdx 	= endIdx;
		
		// se viene trovato nuovamente non è univoco
		if (beginningMatcher.find())
			return DUPLICATE_NODE;
		
		Matcher genericMatcher = genericElement.matcher(xmlStr);
		
		// cerchiamo gli elementi sia di apertura che di chiusura successivi e teniamo il conto di quelli aperti non chiusi
		int openCount = 1;
		while (genericMatcher.find(endIdx)) {
			String matched = genericMatcher.group();
			
			endIdx = genericMatcher.end();
			contentEndIdx = genericMatcher.start();
			
			if(matched.startsWith("</")) openCount--; else openCount++;
			
			if(openCount == 0) break;
		}
		
		// il matcher non ha trovato più risultati e le aperture non corrispondono alle chiusure
		// quindi l'xml non è valido
		if(openCount > 0) throw new RuntimeException("Invalid xml");
		
		if(onlyContent) return xmlStr.substring(contentStartIdx, contentEndIdx);
		
		return xmlStr.substring(startIdx, endIdx);
	}
}
