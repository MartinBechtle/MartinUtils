package martinutils.text;

import java.util.regex.Pattern;

public class MerckXmlUtil
{
	/**
	 * Matcha un indexmarker con apertura, contenuto (anche vuoto) e chiusura. Il group 1 è il contenuto.
	 */
	public static final String INDEXMARKER_REGEX_STANDARD = "<IndexMarker[^<>]+>([^<>]*)</IndexMarker>";
	
	/**
	 * Matcha un indexmarker con apertura, contenuto (anche vuoto) e chiusura. Il group 1 è il contenuto.
	 */
	public static final Pattern INDEXMARKER_PATTERN_STANDARD = Pattern.compile(INDEXMARKER_REGEX_STANDARD);
	
	/**
	 * Matcha un qualsiasi indexmarker, sia autochiudente che con apertura e chiusura. Con o senza contenuto.
	 */
	public static final String INDEXMARKER_REGEX_GENERIC = "<IndexMarker(([^<>]+/>)|(.*?</IndexMarker>))";
	
	/**
	 * Matcha un qualsiasi indexmarker, sia autochiudente che con apertura e chiusura. Con o senza contenuto.
	 */
	public static final Pattern INDEXMARKER_PATTERN_GENERIC = Pattern.compile(INDEXMARKER_REGEX_GENERIC);
	
	/**
	 * Matcha un XRef nel formato standard, ovvero autochiudente
	 */
	public static final String XREF_REGEX_STANDARD = "<XRef[^<>]+/>";
	
	/**
	 * Matcha un XRef nel formato standard, ovvero autochiudente
	 */
	public static final Pattern XREF_PATTERN_STANDARD = Pattern.compile(XREF_REGEX_STANDARD);
	
	/**
	 * Matcha qualsiasi XRef, sia autochiudente che con apertura e chiusura. Con o senza contenuto.
	 */
	public static final String XREF_REGEX_GENERIC = "<XRef(([^<>]+/>)|(.*?</XRef>))";
	
	/**
	 * Matcha qualsiasi XRef, sia autochiudente che con apertura e chiusura. Con o senza contenuto.
	 */
	public static final Pattern XREF_PATTERN_GENERIC = Pattern.compile(XREF_REGEX_GENERIC);
	
	/**
	 * Matcha qualsiasi tag XML (attenzione, tag, non elemento), purchè non abbia le parentesi angolate all'interno degli attributi 
	 */
	public static final String XML_TAG_REGEX = "<[^<>]+>";
	
	/**
	 * Matcha qualsiasi tag XML (attenzione, tag, non elemento), purchè non abbia le parentesi angolate all'interno degli attributi 
	 */
	public static final Pattern XML_TAG_PATTERN = Pattern.compile(XML_TAG_REGEX);
	
	/**
	 * Matcha un qualsiasi UniqueID dell'XML Merck (UniqueID, IDRef o ParaID). Il group 1 è il nome dell'attributo e il group 2 è il valore
	 */
	public static final String UNIQUE_ID_REGEX_GENERIC = "(UniqueID|IDRef|ParaID)=\"(.*?)\"";
	
	/**
	 * Matcha un qualsiasi UniqueID dell'XML Merck (UniqueID, IDRef o ParaID). Il group 1 è il nome dell'attributo e il group 2 è il valore
	 */
	public static final Pattern UNIQUE_ID_PATTERN_GENERIC = Pattern.compile(UNIQUE_ID_REGEX_GENERIC);
	
	/**
	 * Matcha l'attributo UniqueID in una tag dell'XML Merck. Il group 1 è il valore dell'attributo
	 */
	public static final String UNIQUE_ID_REGEX_SPECIFIC = "UniqueId=\"(.*?)\"";
	
	/**
	 * Matcha l'attributo UniqueID in una tag dell'XML Merck. Il group 1 è il valore dell'attributo
	 */
	public static final Pattern UNIQUE_ID_PATTERN_SPECIFIC = Pattern.compile(UNIQUE_ID_REGEX_SPECIFIC);
	
	/**
	 * Rimuove l'underscore e i due caratteri di locale alla fine di una stringa, tipicamente usato per gli UniqueID
	 * @param id
	 * @return
	 */
	public static String normalizeId(String id)
	{
		if (id == null)
			return "";
		
		return id.replaceAll("_[a-z]{2}$", "");
	}
	
	/**
	 * Normaliza tutti gli attributi con _xx alla fine, togliendo il locale
	 * @param str
	 * @return
	 */
	public static String normalizeAllIds(String str)
	{
		if (str == null)
			return str;
		
		String result = str.replaceAll("\"(v\\d{2,9})_[a-z]{2}\"", "\"$1\"");
		return result;
	}
	
	/**
	 * Data una stringa con XML Merck, rimuove le tag di metadati (Indexmarker)
	 * @param str
	 * @return
	 */
	public static String removeMetaData(String str)
	{
		if (str == null)
			return "";
		
		return str.replaceAll("<IndexMarker.*?</IndexMarker>", "");
	}
	
	/**
	 * Rimuove tutto l'XML da una stringa
	 * @param str
	 * @param removeMetadata se true rimuove il contenuto testuale dei metadati (ovvero gli IndexMarkers)
	 * @return
	 */
	public static String removeXml(String str, boolean removeMetadata)
	{
		if (removeMetadata)
			str = str.replaceAll(INDEXMARKER_REGEX_GENERIC, "");
		
		str = str.replaceAll("<[^<>]*>","");
		return str;
	}
}
