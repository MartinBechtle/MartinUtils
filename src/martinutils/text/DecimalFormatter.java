package martinutils.text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import martinutils.runtime.Assert;

/**
 * <p>Utility class to format decimal numbers with a fixed number of decimal digits</p>
 * <p>Do not use if you want to print a variable number of digits or do any rounding</p>
 * <p>It also supports formatting with a "unit", which can be a unit of measurement, a currency, or even the percent symbol</p>
 * @author martin
 */
public class DecimalFormatter {

	protected DecimalFormatSymbols symbols;
	protected int decimalDigits;
	protected boolean groupThousands = true;
	protected boolean unit = false;
	protected boolean unitAfter;
	protected boolean unitWithSpace;
	protected boolean unitNonBreaking;
	
	/**
	 * Create a DecimalFormatter specifying a locale and the number of decimal digits.
	 * By default this object will print no unit or percentage and will use grouping, unless differently specified (see all the methods).
	 * @param value notnull value
	 * @param locale notnull locale
	 * @param decimalDigits the number of decimal digits, must be >= 0
	 */
	public DecimalFormatter(Locale locale, int decimalDigits) {
		
		Assert.notNull(locale, "locale");
		
		setDecimalDigits(decimalDigits);
		symbols = new DecimalFormatSymbols(locale);
	}
	
	/**
	 * Set the number of desired decimal digits. Valid values are from 0 upwards. If 0, then no decimal digits will be shown, but beware
	 * that this class does not do any rounding to the nearest int. If more than 0, trailing zeros will be inserted to match the exact number.
	 * Example: 2.10 will be formatted as 2.100 if the decimal digits are set to 3. While 2.12345 will be formatted as 2.123
	 * @param n
	 * @return
	 */
	public DecimalFormatter setDecimalDigits(int n) {
		if (n < 0) throw new IllegalArgumentException("n must be >= 0");
		this.decimalDigits = n;
		return this;
	}
	
	/**
	 * Get the number of desired decimal digits
	 * @return this object for method chaining
	 */
	public int getDecimalDigits() {
		return decimalDigits;
	}
	
	/** Get the formatted value for this decimal */
	public String format(Number value) {
		
		StringBuilder format = new StringBuilder();
		format.append(groupThousands ? "###,##0" : "##0");
		
		if (decimalDigits > 0) {
			format.append(".");
			for (int i = 0; i < decimalDigits; i++) {
				format.append('0');
			}
		}
		DecimalFormat df = new DecimalFormat(format.toString(), symbols);
		df.setGroupingUsed(groupThousands);
		df.setGroupingSize(3);
		
		String result = df.format(value);
		
		if (unit) {
			String unit = symbols.getCurrencySymbol();
			
			if (unitWithSpace) {
				unit = String.format(unitAfter ? " %s" : "%s ", unit);
			}
			if (unitNonBreaking) {
				unit = unit.replace(" ", StringUtil.NBSP);
			}
			if (unitAfter) {
				result = result + unit;
			} else {
				result = unit + result;
			}
		}
		return result;
	}
	
	/** True if thousands will be grouped */
	public boolean getGroupThousands() {
		return groupThousands;
	}
	
	/**
	 * Set a symbol to be used before or after the formatted decimal, with or without space or nonbreaking space.
	 * @param showUnit show a unit symbol or not
	 * @param symbol the symbol, mandatory only if showUnit is true
	 * @param after put the symbol after if true, else before
	 * @param withSpace use a space between the symbol and the decimal
	 * @param nonbreakingSpace make the previously mentioned space nonbreaking
	 * @return
	 */
	public DecimalFormatter setUnit(boolean showUnit, String symbol, boolean after, boolean withSpace, boolean nonbreakingSpace) {
		
		this.unit = showUnit;
		if (showUnit) {
			Assert.notEmpty(symbol, "symbol");
			symbols.setCurrencySymbol(symbol);
			unitAfter = after;
			unitWithSpace = withSpace;
			unitNonBreaking = nonbreakingSpace;
		}
		return this;
	}
	
	/** Get the symbol used for the unit, if any (else empty string) */
	public String getUnitSymbol() {
		return this.unit ? symbols.getCurrencySymbol() : "";
	}
	
	/** Returns true if a symbol has been set as unit for this formatter */
	public boolean isUnit() {
		return this.unit;
	}
	
	public DecimalFormatter setThousandsSeparator(char separator) {
		symbols.setGroupingSeparator(separator);
		return this;
	}
	
	public char getThousandsSeparator() {
		return symbols.getGroupingSeparator();
	}
	
	public DecimalFormatter setDecimalSeparator(char separator) {
		symbols.setDecimalSeparator(separator);
		return this;
	}
	
	public char getDecimalSeparator() {
		return symbols.getDecimalSeparator();
	}

	/**
	 * Set true or false if the formatter must group the thousands
	 * @param groupThousands if true the formatter will gorup the thousands
	 * @return this object for method chaining
	 */
	public DecimalFormatter setGroupThousands(boolean groupThousands) {
		this.groupThousands = groupThousands;
		return this;
	}
	
	// TODO methods to do percentage or currency
	// TODO unit tests
}
