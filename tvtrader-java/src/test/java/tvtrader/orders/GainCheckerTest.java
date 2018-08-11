package tvtrader.orders;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tvtrader.exchange.ExchangeException;
import tvtrader.model.MarketOrder;
import tvtrader.model.OrderType;
import tvtrader.services.AccountService;
import tvtrader.services.ExchangeService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class GainCheckerTest {
	private static final double BALANCE = 2.0;
	private static final String EXCHANGE = "EXCHANGE";
	private static final String MAINCOIN = "MAINCOIN";
	private static final String ALTCOIN = "ALTCOIN";
	private static final String ACCOUNT = "ACCOUNT";

	@Mock private ExchangeService exchangeService;
	@Mock private AccountService accountService;
	
	@InjectMocks
	private GainChecker checker;
	
	@Mock private MarketOrder order;

	@BeforeAll
	synchronized static void startup() {

	}

	@BeforeEach
	void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		when(exchangeService.getTakerFee(EXCHANGE)).thenReturn(0.0);
		when(exchangeService.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, BALANCE)).thenReturn(1.0);
		
		when(order.getExchange()).thenReturn(EXCHANGE);
		when(order.getAccount()).thenReturn(ACCOUNT);
		when(order.getMainCoin()).thenReturn(MAINCOIN);
		when(order.getAltCoin()).thenReturn(ALTCOIN);
		when(order.getOrderType()).thenReturn(OrderType.LIMIT_SELL);
		when(order.getQuantity()).thenReturn(BALANCE);
	}
	
	
	@Test
	void checkGain_whenGainIsMet_shouldReturnTrue() throws Exception {
		when(accountService.getMinimumGain(EXCHANGE, ACCOUNT)).thenReturn(1.0);
		when(order.getRate()).thenReturn(1.0);
		
		assertTrue(checker.checkGain(order));
	}

	@Test
	void checkGain_whenGainIsNotMet_shouldReturnFalse() throws Exception {
		when(accountService.getMinimumGain(EXCHANGE, ACCOUNT)).thenReturn(1.0);
		when(order.getRate()).thenReturn(0.1);
		
		assertFalse(checker.checkGain(order));
	}
	
	@Test
	void checkGain_whenExchangeExceptionIsThrown_shouldReturnFalse() throws Exception {
		when(accountService.getMinimumGain(EXCHANGE, ACCOUNT)).thenThrow(ExchangeException.class);
		
		assertFalse(checker.checkGain(order));
	}

	@Test
	void checkGain_whenMinimumGainIsZero_shouldReturnTrue() throws Exception {
		when(accountService.getMinimumGain(EXCHANGE, ACCOUNT)).thenReturn(0.0);

		assertTrue(checker.checkGain(order));
	}
	
	@Test
	void checkGain_whenBuyOrder_shouldReturnTrue() throws Exception {
		when(order.getOrderType()).thenReturn(OrderType.LIMIT_BUY);

		assertTrue(checker.checkGain(order));
	}
}
