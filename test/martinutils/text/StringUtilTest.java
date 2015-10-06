package martinutils.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

public class StringUtilTest
{
	@Test
	public void testTryParseInt()
	{
		assertEquals( new Integer(1), StringUtil.tryParseInt("1") );
		assertEquals( new Integer(258), StringUtil.tryParseInt("258") );
		assertEquals( new Integer(-21), StringUtil.tryParseInt("-21") );
		assertEquals( new Integer(7), StringUtil.tryParseInt("007") );
		assertNull( StringUtil.tryParseInt("300 ") );
		assertNull( StringUtil.tryParseInt("21-") );
		assertNull( StringUtil.tryParseInt("abc1") );
		assertNull( StringUtil.tryParseInt("") );
		assertNull( StringUtil.tryParseInt(null) );
	}
	
	@Test
	public void testTryParseFloat()
	{
		assertEquals( new Float(1), StringUtil.tryParseFloat("1.0") );
		assertEquals( new Float(25.8), StringUtil.tryParseFloat("25.8") );
		assertEquals( new Float(-2.1), StringUtil.tryParseFloat("-2.1") );
		assertNull( StringUtil.tryParseInt("300.0 ") );
		assertNull( StringUtil.tryParseInt("0.21-") );
		assertNull( StringUtil.tryParseInt("0,22") );
		assertNull( StringUtil.tryParseInt("abc1") );
		assertNull( StringUtil.tryParseInt("") );
		assertNull( StringUtil.tryParseInt(null) );
	}

	@Test
	public void testIsNumber()
	{
		assertTrue( StringUtil.isNumber("0") );
		assertTrue( StringUtil.isNumber("-10") );
		assertTrue( StringUtil.isNumber("10.12") );
		assertTrue( StringUtil.isNumber("+1.2") );
		
		assertFalse( StringUtil.isNumber("1.2%") );
		assertFalse( StringUtil.isNumber("84,2") ); // il formato italiano per i floating point non è valido
		assertFalse( StringUtil.isNumber("2$") );
		assertFalse( StringUtil.isNumber(" 2") );
		assertFalse( StringUtil.isNumber("") );
		assertFalse( StringUtil.isNumber(null) );
	}

	@Test
	public void testContainsPattern()
	{
		try {
			StringUtil.containsPattern("hello", null);
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		try {
			StringUtil.containsPattern(null, Pattern.compile("x"));
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		assertTrue( StringUtil.containsPattern("Wii 9adS", Pattern.compile("\\d[a-z]{2}")) );
		assertTrue( StringUtil.containsPattern("Weeeeeeeeez", Pattern.compile("^W.*z$")) );
		
		assertFalse( StringUtil.containsPattern("", Pattern.compile("\\d[a-z]{2}")) );
		assertFalse( StringUtil.containsPattern("Weeeeeeeeez", Pattern.compile("^W.*Z$")) );
		assertFalse( StringUtil.containsPattern("Weeeeeeeeez", Pattern.compile("e\\d")) );
	}

	@Test
	public void testFirstRegexMatch()
	{
		try {
			StringUtil.firstRegexMatch("hello", null);
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		try {
			StringUtil.firstRegexMatch(null, "hello");
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		try {
			StringUtil.firstRegexMatch("", "hello");
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		assertEquals(null, StringUtil.firstRegexMatch("[a-z]", "123"));
		assertEquals("abc", StringUtil.firstRegexMatch("[a-z]+", " abc123"));
	}

	@Test
	public void testReplaceFirst()
	{
		try {
			StringUtil.replaceFirst(null, "match", "replacement");
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		try {
			StringUtil.replaceFirst(new StringBuilder(), "", "replacement");
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		try {
			StringUtil.replaceFirst(new StringBuilder(), null, "replacement");
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		StringBuilder sb = new StringBuilder("hello hello world 123!");
		
		assertEquals(true, StringUtil.replaceFirst(sb, "hello", "Hello"));
		assertEquals(true, StringUtil.replaceFirst(sb, " 123", ""));
		assertEquals(true, StringUtil.replaceFirst(sb, "hello ", null));
		assertEquals(false, StringUtil.replaceFirst(sb, "123", null));
		
		assertEquals("Hello world!", sb.toString());
		
		// Testiamo l'altro replaceFirst, quello dove si specifica l'indice di inizio
		sb = new StringBuilder("hello hello world 123!");
		assertEquals(6, StringUtil.replaceFirst(sb, "hello", "hi", 4));
		assertEquals("hello hi world 123!", sb.toString());
	}

	@Test
	public void testReplaceAll()
	{
		try {
			StringUtil.replaceAll(null, "match", "replacement");
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		try {
			StringUtil.replaceAll(new StringBuilder(), "", "replacement");
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		try {
			StringUtil.replaceAll(new StringBuilder(), null, "replacement");
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		StringBuilder sb = new StringBuilder("hello hello world 123!");
		
		assertEquals(2, StringUtil.replaceAll(sb, "hello", "Hello"));
		assertEquals("Hello Hello world 123!", sb.toString());
		
		assertEquals(1, StringUtil.replaceAll(sb, " 123", ""));
		assertEquals(1, StringUtil.replaceAll(sb, " Hello", null));
		assertEquals(0, StringUtil.replaceAll(sb, "123", null));
		
		assertEquals("Hello world!", sb.toString());
	}
	
	@Test
	public void testMatches()
	{
		try {
			StringUtil.matches(null, Pattern.compile("h"));
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		try {
			StringUtil.matches("h", (Pattern)null);
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		try {
			StringUtil.matches(null, "regex");
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		try {
			StringUtil.matches("hello", (String)null);
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		
		assertTrue( StringUtil.matches("hello", Pattern.compile("l+")) );
		assertTrue( StringUtil.matches("hello", "l+") );
		
		assertFalse( StringUtil.matches("", "l+") );
	}
	
	@Test
	public void testMatchesResult()
	{
		try {
			StringUtil.matchesResult(null, null);
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		try {
			StringUtil.matchesResult("hello", null);
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		try {
			StringUtil.matchesResult(null, Pattern.compile("hi"));
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		Pattern p = Pattern.compile("l+");
		assertEquals( "ll", StringUtil.matchesResult("hello", p) );
		assertEquals( "", StringUtil.matchesResult("hexxo", p) );
		assertEquals( "", StringUtil.matchesResult("", p) );
	}
	
	@Test
	public void testIndexOfRegex()
	{
		Pattern p = Pattern.compile("hello");
		String str = "boy, hello, hello!";
		
		try {
			StringUtil.indexOfRegex(null, p, 0);
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		try {
			StringUtil.indexOfRegex(str, null, 0);
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		try {
			StringUtil.indexOfRegex(null, null, 0);
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		try {
			StringUtil.indexOfRegex(str, p, 20);
			fail("IndexOutOfBoundsException should be thrown");
		}
		catch (IndexOutOfBoundsException e) { }
		
		try {
			StringUtil.indexOfRegex("", p, 0);
			fail("IndexOutOfBoundsException should be thrown");
		}
		catch (IndexOutOfBoundsException e) { }
		
		assertEquals(-1, StringUtil.indexOfRegex(str, p, 15));
		assertEquals(5, StringUtil.indexOfRegex(str, p, 0));
		assertEquals(12, StringUtil.indexOfRegex(str, p, 8));
		
		p = Pattern.compile("h[aeiou]|l");
		assertEquals(12, StringUtil.indexOfRegex(str, p, 10));
		
		p = Pattern.compile("[bB]");
		assertEquals(0, StringUtil.indexOfRegex(str, p, 0));
	}
	
	@Test
	public void testCountUnicodeLetters()
	{
		assertEquals(0, StringUtil.countUnicodeLetters(null));
		assertEquals(0, StringUtil.countUnicodeLetters(""));
		assertEquals(3, StringUtil.countUnicodeLetters("asd"));
		assertEquals(0, StringUtil.countUnicodeLetters("123"));
		assertEquals(2, StringUtil.countUnicodeLetters("1ax"));
		assertEquals(5, StringUtil.countUnicodeLetters("ècco 'a"));
		assertEquals(20, StringUtil.countUnicodeLetters("Néni/bácsi (tetszikezés):"));
		assertEquals(7, StringUtil.countUnicodeLetters("漢語; Hànyǔ"));
		assertEquals(3, StringUtil.countUnicodeLetters("$£%&/(asd)"));
	}
	
	@Test
	public void testSortByStrLen()
	{
		List<String> myList = Arrays.asList("one", "two", "three", "four");
		Collections.sort(myList, StringUtil.sortByStrLenComparator(true));
		assertEquals("three", myList.get(3));
		assertEquals("four", myList.get(2));
		Collections.sort(myList, StringUtil.sortByStrLenComparator(false));
		assertEquals("three", myList.get(0));
		assertEquals("four", myList.get(1));
	}
}
