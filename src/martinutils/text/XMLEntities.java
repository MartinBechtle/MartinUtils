package martinutils.text;

import java.util.*;
import java.util.regex.*;

import martinutils.runtime.TestUtil;


public class XMLEntities
{
	private static Map<String, String> attrEntitiesMap;
	private static Map<String, String> nodeEntitiesMap;
	public static Pattern entityPattern;
	
	static {
		nodeEntitiesMap = new HashMap<String, String>();
		attrEntitiesMap = new HashMap<String, String>();

		nodeEntitiesMap.put("&","&amp;");
		nodeEntitiesMap.put("<","&lt;");
		nodeEntitiesMap.put(">","&gt;");
		
		attrEntitiesMap.put("\"","&quot;");
		attrEntitiesMap.put("'","&apos;");
		attrEntitiesMap.put("&","&amp;");
		attrEntitiesMap.put("<","&lt;");
		attrEntitiesMap.put(">","&gt;");
		
		entityPattern = Pattern.compile("(&[a-z]{2,4};)|(&#[0-9]{2,4};)");
	}
	
	public static enum MODE { NODE, ATTRIBUTE };
	
	/**
	 * Replaces entity characters with xml entities in a String. Ignores existing entities.
	 * @param str
	 * @param mode
	 * @return
	 */
	public static String xmlEntities(String str, MODE mode)
	{
		if (str == null)
			return "";
		if (mode == null)
			throw new IllegalArgumentException("mode cannot be null");
		
		Map<String, String> map = (mode == MODE.NODE) ? nodeEntitiesMap : attrEntitiesMap;
		
		int strlen = str.length();
		if (strlen < 1) return str;
		
		StringBuilder sb = new StringBuilder();
		
		// Search already existing entities
		Set<Integer> entitiesIndexes = new HashSet<>();
		Matcher matcher = entityPattern.matcher(str);
		while (matcher.find())
			entitiesIndexes.add(matcher.start()); // create an index of already existing entities, which have to be ignored
		
		for ( int i = 0; i < strlen; i++ )
		{
			String ch = str.charAt(i) + "";
			
			if (entitiesIndexes.contains(i)) {
				sb.append(ch); // already an entity, ignore
				continue;
			}
			
			String entity = (String)map.get(ch);
			if (entity == null)
				sb.append(ch);
			else
				sb.append(entity);
		}
		
		return sb.toString();
	}
	
	public static String unXmlEntities(String str, MODE mode)
	{
		if (str == null)
			return "";
		if (mode == null)
			throw new IllegalArgumentException("mode cannot be null");
		
		Map<String, String> map = (mode == MODE.NODE) ? nodeEntitiesMap : attrEntitiesMap;
		
		int strlen = str.length();
		if (strlen < 1) return str;
		
		// Let's replace all entity values occurrences with the corresponding map key
		StringBuilder sb = new StringBuilder(str);
		for ( Map.Entry<String, String> entity : map.entrySet() )
		{
			String match = entity.getValue();
			String replacement = entity.getKey();
			StringUtil.replaceAll(sb, match, replacement);
		}
		
		return sb.toString();
	}
	
	public static void main(String[] args)
	{
		test();
		System.out.println("All tests succeded");
	}
	
	private static void test()
	{
		String test				= "Questa è una grandissima & provola di &amp; simpatia < 0 &gt; 1";
		
		String result1 			= xmlEntities(test, MODE.NODE);
		String expectedResult1 	= "Questa è una grandissima &amp; provola di &amp; simpatia &lt; 0 &gt; 1";
		
		String result2			= unXmlEntities(test, MODE.NODE);
		String expectedResult2	= "Questa è una grandissima & provola di & simpatia < 0 > 1";
		
		TestUtil.assertTrue( result1.equals(expectedResult1) );
		TestUtil.assertTrue( result2.equals(expectedResult2) );
	}

}
