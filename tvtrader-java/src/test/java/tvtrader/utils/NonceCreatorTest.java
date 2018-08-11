package tvtrader.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NonceCreatorTest {

	@BeforeAll
	synchronized static void startup() {

	}
	
	@Test
	void getNonce_whenNonceIsCalled_shouldAlwaysReturnAGreaterNumber() {
		long firstNonce = NonceCreator.getNonce();
		
		long secondNonce = NonceCreator.getNonce();
		
		assertTrue(secondNonce > firstNonce);
	}
}
