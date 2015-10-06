package martinutils.calc;

import java.util.Calendar;
import java.util.Date;

public class DateCalcUtil {

	/**
	 * Get today's date at 00:00
	 * @return
	 */
	public Date getTodayMidnight() {
		
		Calendar c = Calendar.getInstance();

	    // set the calendar to start of today
	    c.set(Calendar.HOUR_OF_DAY, 0);
	    c.set(Calendar.MINUTE, 0);
	    c.set(Calendar.SECOND, 0);
	    c.set(Calendar.MILLISECOND, 0);

	    Date today = c.getTime();
	    return today;
	}
}
