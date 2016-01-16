package martinutils.text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility for string splitting, but maintaining separators
 * @author martin
 */
public class StringSplitter {
	
	private String regex;
	private Pattern pattern;
	private String[] separators;
	
	public StringSplitter(String regex) {
		
		this.regex = regex;
		this.pattern = Pattern.compile(regex);
	}
	
	/**
	 * Split the string exactly like String.split. The separators are stored in this instance for later usage.
	 * If a separator is found at the start of the string, the first element of the array will be an empty string. This does not happen if one or more separators are found at the end
	 * @param str the string to split
	 * @return
	 */
	public String[] split(String str) {
		
		if (str == null) {
			throw new IllegalArgumentException("str cannot be null");
		}
		List<String> separators = new ArrayList<>();
		
		if (str.isEmpty()) {
			this.separators = new String[0];
			return new String[] { "" };
		}
		Matcher m = pattern.matcher(str);
		while (m.find()) {
			separators.add(m.group());
		}
		this.separators = separators.toArray(new String[0]);
		return str.split(regex);
	}
	
	/**
	 * Split the string keeping all separators in the returned array
	 * @param str the string to split
	 * @return
	 */
	public String[] splitWithSeparators(String str) {
		
		String[] splitted = split(str);
		int totalLen = splitted.length + separators.length;
		String[] result = new String[splitted.length + separators.length];
		
		int splitIdx = 0, sepIdx = 0;
		boolean flag = true;
		for (int i = 0; i < totalLen; i++) {
			
			if (flag && splitIdx < splitted.length) {
				flag = false;
				result[i] = splitted[splitIdx++];
			}
			else {
				flag = true;
				result[i] = separators[sepIdx++];
			}
		}
		return result;
	}
	
	/**
	 * Get all separators from the last split action
	 * @return
	 */
	public String[] getSeparators() {
		return separators;
	}
}
