package martinutils.text;

import java.util.regex.*;

public class RegexTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String testStr = "Mi che @@palle$$";
		String regex = "@@[^$]+\\$\\$";
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(testStr);
		
		if (matcher.find())
			System.out.println(matcher.group());
		else
			System.out.println("Not found");
	}

}
