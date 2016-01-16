package martinutils.text;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class StringSplitterTest {

	@Test
	public void testSplit() {
		
		StringSplitter splitter = new StringSplitter("[ \\s]"); // split on space or whitespace
		
		String str = "This is a\nsentence";
		String[] result = splitter.split(str);
		String[] expected = new String[] {"This", "is", "a", "sentence"};
		assertArrayEquals(expected, result);
		
		result = splitter.getSeparators();
		expected = new String[] {" ", " ", "\n"};
		assertArrayEquals(expected, result);
		
		str = "This is a\nsentence\t";
		result = splitter.split(str);
		expected = new String[] {"This", "is", "a", "sentence"};
		assertArrayEquals(expected, result);
		
		result = splitter.getSeparators();
		expected = new String[] {" ", " ", "\n", "\t"};
		assertArrayEquals(expected, result);
		
		str = " This is a\nsentence\t";
		result = splitter.split(str);
		expected = new String[] {"", "This", "is", "a", "sentence"};
		assertArrayEquals(expected, result);
		
		result = splitter.getSeparators();
		expected = new String[] {" ", " ", " ", "\n", "\t"};
		assertArrayEquals(expected, result);
		
		splitter = new StringSplitter("[ \\s\\p{P}]");
		str = "Lack of nutrients can result in deficiency syndromes (eg, kwashiorkor, pellagra) or other disorders ({100153}).";
		result = splitter.split(str);
		expected = new String[] {"Lack", "of", "nutrients", "can", "result", "in", "deficiency", "syndromes", "", "eg",
				"", "kwashiorkor", "", "pellagra", "", "or", "other", "disorders", "", "", "100153"};
		assertArrayEquals(expected, result);
		
		result = splitter.getSeparators();
		expected = new String[] {" ", " ", " ", " ", " ", " ", " ", " ", "(", ",",
				" ", ",", " ", ")", " ", " ", " ", " ", "(", "{", "}", ")", "."};
		assertArrayEquals(expected, result);
		
	}
	
	@Test
	public void testSplitWithSeparators() {
		
		StringSplitter splitter = new StringSplitter("[ \\s]"); // split on space or whitespace
		
		String str = "This is a\nsentence";
		String[] result = splitter.splitWithSeparators(str);
		String[] expected = new String[] {"This", " ", "is", " ", "a", "\n", "sentence"};
		assertArrayEquals(expected, result);
		
		splitter = new StringSplitter("[ \\s\\p{P}]+");
		str = "L'apport excessif de macronutriments peut induire une obésité ({100154}) et des maladies apparentées;";
		result = splitter.splitWithSeparators(str);
		expected = new String[] {"L", "'", "apport", " ", "excessif", " ", "de", " ", "macronutriments", " ", "peut", " ", 
				"induire", " ", "une", " ", "obésité", " ({", "100154", "}) ", "et", " ", "des", " ", "maladies", " ", "apparentées", ";"  };
		assertArrayEquals(expected, result);
		
		splitter = new StringSplitter("[ \\s\\p{P}]");
		str = "Lack of nutrients can result in deficiency syndromes (eg, kwashiorkor, pellagra) or other disorders ({100153}).";
		result = splitter.splitWithSeparators(str);
		expected = new String[] {"Lack", " ", "of", " ", "nutrients", " ", "can", " ", "result", " ", "in", " ", 
				"deficiency", " ", "syndromes", " ", "", "(", "eg", ",", "", " ", "kwashiorkor", ",", "", " ",
				"pellagra", ")", "", " ", "or", " ", "other", " ", "disorders", " ", "", "(", "", "{", "100153", "}", ")", "."};
		assertArrayEquals(expected, result);
	}

}
