package tvtrader.utils;

import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Factory class that returns a formatter that prevents scientific notation and
 * makes sure the decimal separator is set to '.'<br>
 * 
 * @author Wouter
 *
 */
@UtilityClass
public class PriceFormatter {
	private DecimalFormat formatter;

	static {
		Locale currentLocale = Locale.getDefault();
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
		otherSymbols.setDecimalSeparator('.');

		formatter = new DecimalFormat("0.00000000", otherSymbols);
	}

	/**
	 * Returns a formatter that prevents scientific notation and makes sure the
	 * decimal separator is set to '.'<br>
	 */
	public DecimalFormat getFormatter() {
		return formatter;
	}
}
