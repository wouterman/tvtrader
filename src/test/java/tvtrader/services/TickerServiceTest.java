package tvtrader.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import test.logger.Logger;
import tvtrader.bittrex.BittrexTicker;
import tvtrader.exchange.Exchange;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.ExchangeFactory;
import tvtrader.exchange.apidata.Ticker;
import tvtrader.model.Configuration;
import tvtrader.model.ConfigurationField;

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
	private Configuration configuration;
	@Mock
	private ExchangeFactory factory;

	private TickerService service;

	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}

	@BeforeEach
	void init() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		service = new TickerService(configuration);
		service.setFactory(factory);
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
		service.setTickerRefreshRate(ONE_MINUTE);

		Mockito.when(factory.getExchange(EXCHANGE)).thenReturn(exchange);

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
	
	@Test
	void update_shouldUpdateRefreshRate_whenReceivingUpdate() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setTickerRefreshRate(ONE_MINUTE);
		
		service.update(ConfigurationField.TICKERREFRESHRATE, configuration);
		
		assertEquals(ONE_MINUTE, service.getTickerRefreshRate());
	}
	
	@Test
	void update_shouldNotUpdateRefreshRate_whenReceivingIrrelevantUpdate() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setTickerRefreshRate(ONE_MINUTE);
		
		service.update(ConfigurationField.EXPECTEDSENDER, configuration);
		
		assertEquals(NOT_SET, service.getTickerRefreshRate());
	}
}
