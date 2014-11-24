package martinutils.text.test;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import martinutils.text.StringUtil;

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

}
