package tvtrader.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

/**
 * Helper class for parsing strings to integers.<br>
 * 
 * @author Wouter
 *
 */
@Log4j2
@UtilityClass
public class NumberParser {

	/**
	 * Parses the provided number to an int.<br>
	 * 
	 * @throws ParserException
	 *             If the string couldn't be parsed.
	 */
	public int parseInteger(String number) throws ParserException {
		log.debug("Parsing interval: {}", number);
		int num;

		try {
			num = Integer.parseInt(number);
		} catch (NullPointerException | NumberFormatException mfe) {
			throw new ParserException("Couldn't parse one of the provided intervals. Please check your config!");
		}

		return num;
	}
}
