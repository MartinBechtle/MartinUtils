package martinutils.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class StringUtil
{
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
	 * Tells wether a String is a number. Quite different from commons.io.StringUtils.isNumeric().
	 * @param str
	 * @return true if the string can be parsed as double or integer
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
		if (str == null)
			throw new IllegalArgumentException("str cannot be null");
		if (pattern == null)
			throw new IllegalArgumentException("pattern cannot be null");
		
		return pattern.matcher(str).find();
	}
	/**
	 * Get the first substring in the string matching a regex
	 * @param regex the regular expression, case sensitive
	 * @param str the string in which to match
	 * @return null if not found
	 */
	public static String firstRegexMatch(String regex, String str)
	{
		if (str == null)
			throw new IllegalArgumentException("str cannot be null");
		if (StringUtils.isEmpty(regex))
			throw new IllegalArgumentException("pattern cannot be empty");
		
		if (str.length() > 0)
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
	 * @param sb the StringBuilder to work with, must not be null
	 * @param match not empty
	 * @param replacement
	 * @return true if something has been replaced
	 */
	public static boolean replaceFirst(StringBuilder sb, String match, String replacement)
	{
		if (sb == null)
			throw new IllegalArgumentException("sb cannot be null");
		
		if (StringUtils.isEmpty(match))
			throw new IllegalArgumentException("match cannot be empty");
		
		if (sb.length() > 0)
		{
			if (replacement == null)
				replacement = "";
			
			int start = sb.indexOf(match);
			if (start > -1)
			{
				int end = start + match.length();
				sb.replace(start, end, replacement);
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Replace all occurrencies of a string in a stringbuilder
	 * @param sb the stringbuilder in which to replace, not null
	 * @param match the substring to match, not empty
	 * @param replacement the replacement for the matched substring
	 * @return the number of replacements done
	 */
	public static int replaceAll(StringBuilder sb, String match, String replacement)
	{
		if (sb == null)
			throw new IllegalArgumentException("sb cannot be null");
		
		if (StringUtils.isEmpty(match))
			throw new IllegalArgumentException("match cannot be empty");
		
		if (replacement == null)
			replacement = "";
		
		int replacements = 0;
		int matchLen = match.length();
		int replacementLen = replacement.length();
		
		if (sb.length() > 0)
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
