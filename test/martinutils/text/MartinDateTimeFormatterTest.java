package martinutils.text;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MartinDateTimeFormatterTest
{
	@Test
	public void testConstructors()
	{
		MartinDateTimeFormatter formatter1 = new MartinDateTimeFormatter(true, true, true);
		MartinDateTimeFormatter formatter2 = new MartinDateTimeFormatter().showDate().showTime().displaySec();
		String format1 = formatter1.format();
		String format2 = formatter2.format();
		assertEquals(format1, format2);
	
		formatter1 = new MartinDateTimeFormatter(false, true, true);
		formatter2 = new MartinDateTimeFormatter().showTime().displaySec();
		format1 = formatter1.format();
		format2 = formatter2.format();
		assertEquals(format1, format2);
		
		formatter1 = new MartinDateTimeFormatter(true, false, true);
		formatter2 = new MartinDateTimeFormatter().showDate().displaySec();
		format1 = formatter1.format();
		format2 = formatter2.format();
		assertEquals(format1, format2);
		
		formatter1 = new MartinDateTimeFormatter(true, true, false);
		formatter2 = new MartinDateTimeFormatter().showDate().showTime();
		format1 = formatter1.format();
		format2 = formatter2.format();
		assertEquals(format1, format2);
		
		formatter1 = new MartinDateTimeFormatter(false, false, true);
		formatter2 = new MartinDateTimeFormatter().displaySec();
		format1 = formatter1.format();
		format2 = formatter2.format();
		assertEquals(format1, format2);
		
		formatter1 = new MartinDateTimeFormatter(false, true, false);
		formatter2 = new MartinDateTimeFormatter().showTime();
		format1 = formatter1.format();
		format2 = formatter2.format();
		assertEquals(format1, format2);
		
		formatter1 = new MartinDateTimeFormatter(true, false, false);
		formatter2 = new MartinDateTimeFormatter().showDate();
		format1 = formatter1.format();
		format2 = formatter2.format();
		assertEquals(format1, format2);
		
		formatter1 = new MartinDateTimeFormatter(false, false, false);
		formatter2 = new MartinDateTimeFormatter();
		format1 = formatter1.format();
		format2 = formatter2.format();
		assertEquals(format1, format2);
		
		formatter1 = new MartinDateTimeFormatter(true, true, true, "yyyy/MM/dd", "HH:mm:ss", "Europe/Rome");
		formatter2 = new MartinDateTimeFormatter().showDate().showTime().displaySec()
												.setDateFormat("yyyy/MM/dd").setTimeFormat("HH:mm:ss").setTimeZone("Europe/Rome");
		format1 = formatter1.format();
		format2 = formatter2.format();
		assertEquals(format1, format2);
	}
	
	@Test
	public void testFormat()
	{
		// La seguente data è Lunedì 23 Febbraio 2015, ore 14:29:50 a Roma e 22.29:50 a Tokyo
		java.util.Date date = new java.util.Date(1424698190774l);
		
		MartinDateTimeFormatter formatter = new MartinDateTimeFormatter().showDate().showTime().displaySec()
				.setDateFormat("yyyy/MM/dd").setTimeFormat("HH:mm:ss").setTimeZone("Europe/Rome");
		assertEquals("2015/02/23 14:29:50", formatter.format(date) );
		
		formatter = new MartinDateTimeFormatter().showDate().showTime()
				.setDateFormat("yyyy/MM/dd").setTimeFormat("HH:mm:ss").setTimeZone("Europe/Rome");
		assertEquals("2015/02/23 14:29", formatter.format(date) );
		
		formatter = new MartinDateTimeFormatter().showDate()
				.setDateFormat("yyyy/MM/dd").setTimeFormat("HH:mm:ss").setTimeZone("Europe/Rome");
		assertEquals("2015/02/23", formatter.format(date) );
		
		formatter = new MartinDateTimeFormatter().showTime()
				.setDateFormat("yyyy/MM/dd").setTimeFormat("HH:mm:ss").setTimeZone("Europe/Rome");
		assertEquals("14:29", formatter.format(date) );
		
		formatter = new MartinDateTimeFormatter().showDate().showTime().displaySec()
				.setDateFormat("yyyy/MM/dd").setTimeFormat("HH:mm:ss").setTimeZone("Asia/Tokyo");
		assertEquals("2015/02/23 22:29:50", formatter.format(date) );
	}
}
