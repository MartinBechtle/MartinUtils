package martinutils.text;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;

public class DecimalFormatterTest {

	@Test
	public void testDecimalFormatter() {
		
		DecimalFormatter df = new DecimalFormatter(Locale.ITALY, 2);
		String result = df.format(2.001);
		assertEquals("2,00", result);
		
		result = df.format(10000);
		assertEquals("10.000,00", result);
		
		result = df.format(1000.1);
		assertEquals("1.000,10", result);
		
		df.setGroupThousands(false);
		result = df.format(1000.1);
		assertEquals("1000,10", result);
		
		df.setDecimalDigits(3);
		result = df.format(1000.1);
		assertEquals("1000,100", result);
		
		df = new DecimalFormatter(Locale.US, 2);
		result = df.format(2.001);
		assertEquals("2.00", result);
		
		result = df.format(10000);
		assertEquals("10,000.00", result);
		
		result = df.format(1000.1);
		assertEquals("1,000.10", result);
		
		df.setThousandsSeparator('\'');
		df.setDecimalSeparator(';');
		result = df.format(1000.1);
		assertEquals("1'000;10", result);
	}
	
	@Test
	public void testCurrency() {
		
		DecimalFormatter df = new DecimalFormatter(Locale.ITALY, 2);
		df.setUnit(true, "EUR", true, true, false);
		String result = df.format(2.001);
		assertEquals("2,00 EUR", result);
		
		df.setUnit(true, "€", false, true, false);
		result = df.format(2.001);
		assertEquals("€ 2,00", result);
		
		df.setUnit(true, "€", false, false, false);
		result = df.format(2.001);
		assertEquals("€2,00", result);
		
		df.setUnit(true, "€", true, false, false);
		result = df.format(2.001);
		assertEquals("2,00€", result);
		
		df.setUnit(true, "€", true, true, true);
		result = df.format(2.001);
		String expectedResult = String.format("2,00%s€", StringUtil.NBSP);
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testPercentage() {
		
	}

}
