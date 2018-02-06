package tvtrader.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import test.logger.Logger;

class HashingUtilityTest {
	private String data = "The quick brown fox jumps over the lazy dog";
	private String key = "herpderp";

	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}
	
	@BeforeEach
	void setup() {
	}
	
	@Test
	void calculateSignature_whenProvidedWithSha512Encoding_shouldCalculateCorrectSignature() throws Exception  {
		HashingUtility hasher = new HashingUtility(Encoding.SHA512);
		
		// https://hash.online-convert.com/sha512-generator
		String expected = "b00a1291a9aabaa72e6c8cf777e94bcd045df9f3e0ea754871884eab760a43362fa8fd9efe5d4a692c703e5e0260cc44d75338560d44a4c07d84aa45ba2e1ace";
		
		String actual = hasher.calculateSignature(data, key);

		assertEquals(actual, expected);
	}
	
	@Test
	void calculateSignature_whenProvidedWithSha256Encoding_shouldCalculateCorrectSignature() throws Exception  {
		HashingUtility hasher = new HashingUtility(Encoding.SHA256);
		
		// https://hash.online-convert.com/sha256-generator
		String expected = "3ce1e2a0df34ecba2fa41270af3362bc7a72426888cf17eff836d9bf1ead74bf";
		String actual = hasher.calculateSignature(data, key);
		
		assertEquals(actual, expected);
	}
}
