package martinutils.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {

	/**
	 * Rounds a double with "half up" mode, up to the specified amount of places after decimal separator. It is overflow-safe because it parses the double as BigDecimal
	 * @param value the value to round
	 * @param places the number of places to keep after decimal separator
	 * @return
	 */
	public static double round(double value, int places) {
		
	    if (places < 0) throw new IllegalArgumentException();
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
