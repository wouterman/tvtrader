package tvtrader.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import test.logger.Logger;

class NonceCreatorTest {

	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}
	
	@Test
	void getNonce_whenNonceIsCalled_shouldAlwaysReturnAGreaterNumber() {
		long firstNonce = NonceCreator.getNonce();
		
		long secondNonce = NonceCreator.getNonce();
		
		assertTrue(secondNonce > firstNonce);
	}
}
