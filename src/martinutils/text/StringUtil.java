package martinutils.text;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import martinutils.io.FileUtil;


public class StringUtil
{
	/**
	 * Restituisce tue se la stringa è null o vuota
	 * @param str
	 * @return
	 */
	public static boolean isEmptyOrNull(String str)
	{
		return str == null || str.length() == 0;
	}
	
	/**
	 * Parse a fucking int withouth having to fucking catch the fucking numberformat exception
	 * @param str
	 * @return null if is not valid integer
	 */
	public static Integer tryParseInt(String str)
	{
		try {
			return Integer.parseInt(str);
		}
		catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * Tells wether a String is a number.
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str)
	{
		if (str == null || str.length() == 0)
			return false;
		
		char c = str.charAt(0);
		
		if ((c >= '0' && c <= '9') || c == '.' || c == '-' || c == '+' )
		{
			try
			{
				Integer.parseInt(str);
			}
			catch (Exception e)
			{
				try {
					Double.parseDouble(str);
				}
				catch (Exception exc) {
					return false;
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if the given string matches the pattern (NOT the whole string, but any subsequence)
	 * @param str the string to match
	 * @param pattern the pattern to match
	 * @return
	 */
	public static boolean containsPattern(String str, Pattern pattern)
	{
		return pattern.matcher(str).find();
	}
	
	/**
	 * Check if the given string matches entireley the given regex (java library version sucks)
	 * @param string
	 * @param pattern
	 * @return
	 */
	public static boolean matches(String string, Pattern pattern)
	{
		Matcher matcher = pattern.matcher(string);
		
		if (matcher.find())
		{
			string = string.replace(matcher.group(), "");
			int len = string.length();
			
			if (len == 0) // if the string has no length once the matching pattern has been replaced, then it matches exactly
				return true;
			
			if (len > 1) // if the string has more than 1 remaining character, it doesn't match for sure
				return false;
				
			// if it has only 1 remaining char, check that it's not the BOM
			char c = string.charAt(0);
			return (c == FileUtil.BOM);
		}
		// Return false if matcher doesn't find anything
		return false;
	}
	public static String firstRegexMatch(String regex, String str)
	{
		if (str != null && str.length() > 0)
		{			
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(str);
			
			if (m.find())
				return m.group();
		}
		
		return null;
	}
	
	/**
	 * Replace the first occurrence of a given string in a stringbuilder
	 * @param sb the StringBuilder to work with
	 * @param match
	 * @param replacement
	 * @return true if something has been replaced
	 */
	public static boolean replaceFirst(StringBuilder sb, String match, String replacement)
	{
		if (sb != null && sb.length() > 0 && match != null & match.length() > 0)
		{
			if (replacement == null) replacement = "";
			int start = sb.indexOf(match);
			if (start > -1) {
				int end = start + match.length();
				sb.replace(start, end, replacement);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Replace all occurrencies of a string in a stringbuilder
	 * @param sb the stringbuilder in which to replace
	 * @param match the substring to match
	 * @param replacement the replacement for the matched substringg
	 * @return the number of replacements done
	 */
	public static int replaceAll(StringBuilder sb, String match, String replacement)
	{
		int replacements = 0;
		int matchLen = (match != null) ? match.length() : 0;
		int replacementLen = (replacement != null) ? replacement.length() : 0;
		
		if (sb != null && sb.length() > 0 && matchLen > 0)
		{
			int offset = 0;
			int start;
			int end;
			
			while ((start = sb.indexOf(match, offset)) > -1 )
			{
				end = start + matchLen;
				offset = start + replacementLen;
				sb.replace(start, end, replacement);
				replacements++;
			}
		}
		
		return replacements;
	}
}
