package martinutils.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang3.StringUtils;

public class StringUtil
{
	/**
	 * Parse an int withouth having to catch the numberformat exception
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
	 * Parse an int withouth having to catch the numberformat exception
	 * @param str
	 * @return null if is not valid integer
	 */
	public static Long tryParseLong(String str)
	{
		try {
			return Long.parseLong(str);
		}
		catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * Parse a float withouth having to catch the numberformat exception
	 * @param str a float with a dot as decimal separator
	 * @return null if is not valid float
	 */
	public static Float tryParseFloat(String str)
	{
		try {
			return Float.parseFloat(str);
		}
		catch (NumberFormatException e) {
			return null;
		}
	}
	
	public static boolean isInteger(String str)
	{
		return tryParseInt(str) != null;
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
	 * Replace the first occurrence of a given string in a stringbuilder starting from a specific index
	 * @param sb the StringBuilder to work with
	 * @param match
	 * @param replacement
	 * @param startIndex
	 * @return the index of the found match, or -1 if not found
	 */
	public static int replaceFirst(StringBuilder sb, String match, String replacement, int startIndex)
	{
		if (sb == null)
			throw new IllegalArgumentException("sb cannot be null");
		
		if (StringUtils.isEmpty(match))
			throw new IllegalArgumentException("match cannot be empty");
		
		if (sb.length() > 0)
		{
			if (replacement == null)
				replacement = "";
			
			int start = sb.indexOf(match, startIndex);
			
			if (start > -1)
			{
				int end = start + match.length();
				sb.replace(start, end, replacement);
				return start;
			}
		}
		
		return -1;
	}
	
	/**
	 * Replace all occurrencies of a string in a StringBuilder. Please note: performance is lower than replacing on String. Use only if you have
	 * the specific need of counting how many occurrencies have replaced or if the number of replacements is low.
	 * 
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
	
	/**
	 * Restituisce true se una parte della stringa matcha il Pattern specificato, contrariamente al metodo Pattern.matches che invece verifica se l'intera stringa matcha
	 * @param str la stringa da matchare
	 * @param pattern un pattern regex
	 * @return
	 */
	public static boolean matches(String str, Pattern pattern)
	{
		if (pattern == null)
			throw new IllegalArgumentException("pattern cannot be null");
		if (str == null)
			throw new IllegalArgumentException("str cannot be null");
		
		return pattern.matcher(str).find();
	}
	
	/**
	 * Restituisce true se una parte della stringa matcha la regex specificata, contrariamente al metodo String.matches che invece verifica se l'intera stringa matcha
	 * @param str la stringa da matchare
	 * @param regex una espressione regolare
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static boolean matches(String str, String regex) throws PatternSyntaxException
	{
		if (str == null)
			throw new IllegalArgumentException("str cannot be null");
		if (regex == null)
			throw new IllegalArgumentException("regex cannot be null");
		
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(str).find();
	}
	
	/**
	 * Data una stringa, restituisce la prima sottosequenza che matcha il pattern specificato
	 * @param str una stringa non nulla
	 * @param pattern un pattern non nullo
	 * @return stringa vuota se non matcha alcuna sottosequenza, altrimenti la sottosequenza matchata
	 */
	public static String matchesResult(String str, Pattern pattern)
	{
		if (str == null)
			throw new IllegalArgumentException("str cannot be null");
		if (pattern == null)
			throw new IllegalArgumentException("pattern cannot be null");
		
		if (str.length() > 0)
		{
			Matcher matcher = pattern.matcher(str);
			if (matcher.find())
				return matcher.group();
		}
		
		return "";
	}
	
	/**
	 * Conta il numero di volte che il Matcher matcha
	 * @param matcher
	 * @return
	 */
	public static int countMatches(Matcher matcher)
	{
		if (matcher == null)
			throw new IllegalArgumentException("matcher cannot be null");
		
		int count = 0;
		while (matcher.find()) count++;
		return count;
	}
	
	/**
	 * Conta il numero di volte che un pattern matcha su una stringa
	 * @param p
	 * @param str
	 * @return
	 */
	public static int countMatches(Pattern p, String str)
	{
		if (p == null || str == null)
			throw new IllegalArgumentException("args cannot be null");
		
		Matcher matcher = p.matcher(str);
		return countMatches(matcher);
	}
	
	/**
	 * Restituisce il primo indice della stringa in cui viene matchato il pattern
	 * @param str la stringa in cui cercare
	 * @param p il pattern (regex)
	 * @param start l'indice da cui iniziare a cercare
	 * @return
	 */
	public static int indexOfRegex(CharSequence str, Pattern p, int start)
	{
		if (p == null)
			throw new IllegalArgumentException("p cannot be null");
		if (start < 0)
			throw new IllegalArgumentException("start must be > 0");
		if (str == null)
			throw new IllegalArgumentException("str cannot be null");
		if (start >= str.length())
			throw new IndexOutOfBoundsException("start > str length");
		
		Matcher m = p.matcher(str);
		if (m.find(start)) {
			return m.start();
		}
		return -1;
	}
	
	/**
	 * Restituisce il numero di lettere in una stringa, escludendo dunque numeri, spazi, punteggiatura. Utilizza la regex \pL.
	 * @param str
	 * @return
	 */
	public static int countUnicodeLetters(String str)
	{
		if (str == null || str.isEmpty())
			return 0;
		String stripped = str.replaceAll("\\pL", "");
		return str.length() - stripped.length();
	}
	
	/**
	 * Calcola una percentuale
	 * @param partial
	 * @param total
	 * @return restituisce la percentuale sotto forma di stringa con % alla fine
	 */
	public static String calculatePercentStr(int partial, int total)
	{
		return calculatePercent(partial, total) + "%";
	}
	
	/**
	 * Calcola una percentuale
	 * @param p la percentuale compresa fra 0 e 1
	 * @return restituisce la percentuale moltiplicata per 100, arrotondata, con il segno % a seguire
	 */
	public static String calculatePercentStr(double p)
	{
		return Math.round(p * 100) + "%";
	}
	
	/**
	 * Calcola una percentuale
	 * @param partial
	 * @param total
	 * @return retituisce la percentuale in forma numerica
	 */
	public static int calculatePercent(int partial, int total)
	{
		double percentDouble = (double)partial / (double)total;
		int percent = (int)Math.round(percentDouble * 100);
		return percent;
	}
}
