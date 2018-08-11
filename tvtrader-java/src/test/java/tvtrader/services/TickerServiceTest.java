package tvtrader.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import tvtrader.bittrex.BittrexTicker;
import tvtrader.exchange.Exchange;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.ExchangeFactory;
import tvtrader.exchange.apidata.Ticker;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
class TickerServiceTest {
	private static final String UNKNOWN = "UNKNOWN";
	private static final double LAST = 3.0;
	private static final double BID = 2.0;
	private static final double ASK = 1.0;
	private static final String MARKET = "MARKET";
	private static final String EXCHANGE = "EXCHANGE";
	private static final String MAINCOIN = "MAINCOIN";
	private static final String ALTCOIN = "ALTCOIN";
	private static final int ONE_MINUTE = 60;
	private static final int NOT_SET = 0;

	@Mock
	private Exchange exchange;
	
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private ExchangeFactory factory;

	@InjectMocks
	private TickerService service;

	@BeforeAll
	synchronized static void startup() {

	}

	@BeforeEach
	void init() throws Exception {
		MockitoAnnotations.initMocks(this);

	}

	@Test
	void getAsk_whenExchangeIsUnknown_shouldThrowExchangeException() throws Exception {
		Mockito.when(factory.getExchange(EXCHANGE)).thenThrow(ExchangeException.class);

		assertThrows(ExchangeException.class, () -> service.getAsk(EXCHANGE, MAINCOIN, ALTCOIN));
	}

	@Test
	void getAsk_whenRefreshIsNeeded_shouldRefreshTickers() throws Exception {
		Mockito.when(factory.getExchange(EXCHANGE)).thenReturn(exchange);

		Ticker ticker = new BittrexTicker(MARKET, ASK, BID, LAST);
		Map<String, Ticker> tickers = new HashMap<>();
		tickers.put(MARKET, ticker);
		Mockito.when(exchange.getTickers()).thenReturn(tickers);

		Mockito.when(exchange.createMarket(MAINCOIN, ALTCOIN)).thenReturn(MARKET);

		assertEquals(ASK, service.getAsk(EXCHANGE, MAINCOIN, ALTCOIN));
	}

	@Test
	void getAsk_whenRefreshNotNeeded_shouldNotRefreshTickers() throws Exception {
		Mockito.when(factory.getExchange(EXCHANGE)).thenReturn(exchange);
		Mockito.when(configurationService.getTickerRefreshRate()).thenReturn(ONE_MINUTE);

		Ticker ticker = new BittrexTicker(MARKET, ASK, BID, LAST);
		Map<String, Ticker> tickers = new HashMap<>();
		tickers.put(MARKET, ticker);
		Mockito.when(exchange.getTickers()).thenReturn(tickers).thenThrow(Exception.class);

		Mockito.when(exchange.createMarket(MAINCOIN, ALTCOIN)).thenReturn(MARKET);

		service.getAsk(EXCHANGE, MAINCOIN, ALTCOIN);
		assertEquals(ASK, service.getAsk(EXCHANGE, MAINCOIN, ALTCOIN));
	}

	@Test
	void getAsk_whenMarketIsUnknown_shouldThrowExchangeException() throws Exception {
		Mockito.when(factory.getExchange(EXCHANGE)).thenReturn(exchange);

		Ticker ticker = new BittrexTicker(MARKET, ASK, BID, LAST);
		Map<String, Ticker> tickers = new HashMap<>();
		tickers.put(MARKET, ticker);
		Mockito.when(exchange.getTickers()).thenReturn(tickers);

		Mockito.when(exchange.createMarket(MAINCOIN, ALTCOIN)).thenReturn(MARKET);
		
		service.getAsk(EXCHANGE, MAINCOIN, ALTCOIN);
		
		assertThrows(ExchangeException.class, () -> service.getAsk(EXCHANGE, UNKNOWN, UNKNOWN));
	}

	@Test
	void getBid_whenRefreshIsNeeded_shouldRefreshTickers() throws Exception {
		Mockito.when(factory.getExchange(EXCHANGE)).thenReturn(exchange);

		Ticker ticker = new BittrexTicker(MARKET, ASK, BID, LAST);
		Map<String, Ticker> tickers = new HashMap<>();
		tickers.put(MARKET, ticker);
		Mockito.when(exchange.getTickers()).thenReturn(tickers);

		Mockito.when(exchange.createMarket(MAINCOIN, ALTCOIN)).thenReturn(MARKET);

		assertEquals(BID, service.getBid(EXCHANGE, MAINCOIN, ALTCOIN));
	}

}
