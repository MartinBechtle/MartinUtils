package martinutils.xml;

import static org.junit.Assert.*;

import org.junit.Test;

public class XmlAsStrUtilityTest
{
	@Test
	public void testConstructor()
	{
		try {
			new XmlAsStrUtility(null);
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		try {
			new XmlAsStrUtility(null);
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
	}
	
	String testXml = "<nodo id=\"v001\">contenuto</nodo><duplicato id=\"\"/><duplicato id=\"\">asd</duplicato>"
					+ "<para id=\"001\">siamo <para id=\"002\"><bold>fighi</bold> sai?</para></para>";
	
	@Test
	public void testGetElementAsStr() 
	{
		XmlAsStrUtility util = new XmlAsStrUtility(testXml);
		
		try {
			util.getElementAsStr(null, "id", "v001");
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		try {
			util.getElementAsStr("asd", null, "v001");
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		try {
			util.getElementAsStr("asd", "id", null);
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		try {
			util.getElementAsStr("", "id", "v001");
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		try {
			util.getElementAsStr("nodo", "", "v001");
			fail("IllegalArgumentException should be thrown");
		}
		catch (IllegalArgumentException e) { }
		
		String result = util.getElementAsStr("nodo", "id", "v001");
		assertEquals("<nodo id=\"v001\">contenuto</nodo>", result);
		
		result = util.getElementAsStr("nodooo", "id", "v001");
		assertEquals("", result);
		
		result = util.getElementAsStr("nodo", "iddd", "v001");
		assertEquals("", result);
		
		result = util.getElementAsStr("nodooo", "id", "01");
		assertEquals("", result);
		
		result = util.getElementAsStr("nodooo", "id", "");
		assertEquals("", result);
		
		result = util.getElementAsStr("nodooo", "id", "");
		assertEquals("", result);
		
		result = util.getElementAsStr("para", "id", "001");
		assertEquals("<para id=\"001\">siamo <para id=\"002\"><bold>fighi</bold> sai?</para></para>", result);
	}

	@Test
	public void testGetElementContentAsStr()
	{
		// per ora test semplice semplice, poi si fa più avanzato
		XmlAsStrUtility util = new XmlAsStrUtility(testXml);
		
		String result = util.getElementContentAsStr("para", "id", "001");
		assertEquals("siamo <para id=\"002\"><bold>fighi</bold> sai?</para>", result);
	}

}
