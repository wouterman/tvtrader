package tvtrader.exchange.apidata;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BalanceManagerTest {
	private BalanceManager manager;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		manager = new BalanceManager("BTC");
	}

	@Test
	void getBalance_whenBalanceIsNull_shouldReturnZero() {
		double expected = 0;
		
		double actual = manager.getBalance("XXX");
		
		assertEquals(expected, actual);
	}
	
	@Test
	void getBalance_whenBalanceIsKnown_shouldReturnThatBalance() {
		Map<String, Double> balances = new HashMap<>();
		balances.put("BTC", 2.0);
		double expected = 2;
		
		manager.replaceBalances(balances);
		double actual = manager.getBalance("BTC");
		
		assertEquals(expected, actual);
	}

}
