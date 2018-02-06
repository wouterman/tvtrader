package tvtrader.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import test.logger.Logger;

public class NumberParserTest {
	private static final String UNPARSEABLE = "UNPARSEABLE";
	private static final String _1 = "1";
	
	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}
	
	@Test
	void parseInteger_whenProvidedWithValidInput_shouldReturnInteger() throws ParserException {
		int expected = 1;
		
		int actual = NumberParser.parseInteger(_1);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void parseInteger_whenProvidedWithInvalidInput_shouldThrowParserException() {
		assertThrows(ParserException.class, () -> NumberParser.parseInteger(UNPARSEABLE));
	}
	
}
