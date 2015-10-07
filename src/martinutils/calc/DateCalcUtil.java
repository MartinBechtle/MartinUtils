package martinutils.calc;

import java.util.Calendar;
import java.util.Date;

public class DateCalcUtil {
	
	/**
	 * Duration of a day in milliseconds (useful to do calculations on long vars representing time)
	 */
	public static final int DAY_DURATION = 1000 * 60 * 60 * 24;
	
	/**
	 * Get today's date at 00:00
	 * @return
	 */
	public static Date getTodayMidnight() {
		
		Calendar c = Calendar.getInstance();

	    // set the calendar to start of today
	    c.set(Calendar.HOUR_OF_DAY, 0);
	    c.set(Calendar.MINUTE, 0);
	    c.set(Calendar.SECOND, 0);
	    c.set(Calendar.MILLISECOND, 0);

	    Date today = c.getTime();
	    return today;
	}
	
	/**
	 * Return a copy of the specified date, set to midnight of that day
	 * @return
	 */
	public static Date getDateAtMidnight(Date date) {
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);

	    // set the calendar to start of the date
	    c.set(Calendar.HOUR_OF_DAY, 0);
	    c.set(Calendar.MINUTE, 0);
	    c.set(Calendar.SECOND, 0);
	    c.set(Calendar.MILLISECOND, 0);

	    Date today = c.getTime();
	    return today;
	}
}
