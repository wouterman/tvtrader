package tvtrader.checkers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import tvtrader.exchange.ExchangeException;
import tvtrader.jobs.MailFetchJob;
import tvtrader.mail.MailClient;
import tvtrader.model.MarketOrder;
import tvtrader.model.OrderType;
import tvtrader.orders.GainChecker;
import tvtrader.orders.OrderBuilder;
import tvtrader.orders.OrderLineParser;
import tvtrader.orders.OrderPlacer;
import tvtrader.services.AccountService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderCheckerTest {

	private static final String SUBJECT = "SUBJECT";
	private static final String EXCHANGE = "EXCHANGE";
	private static final String ACCOUNT = "ACCOUNT";
	private static final String MAINCOIN = "MAINCOIN";
	private static final double BUYLIMIT = 1.0;
	
	private Optional<MarketOrder> optional;
	private MarketOrder order;
	
	@Mock
	private MailClient client;
	@Mock
	private AccountService accountService;
	@Mock
	private OrderLineParser orderLineParser;
	@Mock
	private GainChecker gainChecker;
	@Mock
	private OrderPlacer orderPlacer;
	@Mock
	private OrderBuilder orderBuilder;

	@InjectMocks
	private MailFetchJob orderChecker;

	@BeforeAll
	synchronized static void startup() {

	}

	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);
		
		order = new MarketOrder();
		order.setExchange(EXCHANGE);
		order.setAccount(ACCOUNT);
		order.setOrderType(OrderType.LIMIT_SELL);
		order.setQuantity(1.0);
		order.setRate(1.0);
		optional = Optional.of(order);
	}

	@Test
	void run_whenValidOrderLine_shouldAddOrderToQueue() throws Exception {
		when(client.fetchSubjectLines()).thenReturn(Arrays.asList(SUBJECT));
		when(orderLineParser.parseOrderLine(SUBJECT)).thenReturn(optional);
		when(accountService.getMainCurrency(EXCHANGE, ACCOUNT)).thenReturn(MAINCOIN);
		when(accountService.getBuyLimit(EXCHANGE, ACCOUNT)).thenReturn(BUYLIMIT);
		when(gainChecker.checkGain(order)).thenReturn(true);
		
		orderChecker.run();
		
		verify(orderPlacer, Mockito.times(1)).addOrder(order);
	}
	
	@Test
	void run_whenNoEmails_shouldNotAddOrderToQueue() throws Exception {
		when(client.fetchSubjectLines()).thenReturn(Collections.emptyList());
		
		orderChecker.run();
		
		verify(orderPlacer, Mockito.never()).addOrder(order);
	}
	
	@Test
	void run_whenNoOrderEmails_shouldNotAddOrderToQueue() throws Exception {
		when(client.fetchSubjectLines()).thenReturn(Arrays.asList(SUBJECT));

		optional = Optional.empty();
		when(orderLineParser.parseOrderLine(SUBJECT)).thenReturn(optional);
		
		orderChecker.run();
		
		verify(orderPlacer, Mockito.never()).addOrder(order);
	}
	
	@Test
	void run_whenGainNotMet_shouldNotAddOrderToQueue() throws Exception {
		when(client.fetchSubjectLines()).thenReturn(Arrays.asList(SUBJECT));
		when(orderLineParser.parseOrderLine(SUBJECT)).thenReturn(optional);
		when(accountService.getMainCurrency(EXCHANGE, ACCOUNT)).thenReturn(MAINCOIN);
		when(accountService.getBuyLimit(EXCHANGE, ACCOUNT)).thenReturn(BUYLIMIT);
		when(gainChecker.checkGain(order)).thenReturn(false);
		
		orderChecker.run();
		
		verify(orderPlacer, Mockito.never()).addOrder(order);
	}
	
	@Test
	void run_whenRateNotSet_shouldNotAddOrderToQueue() throws Exception {
		when(client.fetchSubjectLines()).thenReturn(Arrays.asList(SUBJECT));
		when(orderLineParser.parseOrderLine(SUBJECT)).thenReturn(optional);
		when(accountService.getMainCurrency(EXCHANGE, ACCOUNT)).thenReturn(MAINCOIN);
		when(accountService.getBuyLimit(EXCHANGE, ACCOUNT)).thenReturn(BUYLIMIT);
		
		order.setRate(0);
		
		orderChecker.run();
		
		verify(orderPlacer, Mockito.never()).addOrder(order);
	}
	
	@Test
	void run_whenQuantityNotSet_shouldNotAddOrderToQueue() throws Exception {
		when(client.fetchSubjectLines()).thenReturn(Arrays.asList(SUBJECT));
		when(orderLineParser.parseOrderLine(SUBJECT)).thenReturn(optional);
		when(accountService.getMainCurrency(EXCHANGE, ACCOUNT)).thenReturn(MAINCOIN);
		when(accountService.getBuyLimit(EXCHANGE, ACCOUNT)).thenReturn(BUYLIMIT);
		
		order.setQuantity(0);
		
		orderChecker.run();
		
		verify(orderPlacer, Mockito.never()).addOrder(order);
	}
	
	@Test
	void run_whenExchangeExceptionIsThrown_shouldNotAddOrderToQueue() throws Exception {
		when(client.fetchSubjectLines()).thenReturn(Arrays.asList(SUBJECT));
		when(orderLineParser.parseOrderLine(SUBJECT)).thenReturn(optional);
		when(accountService.getMainCurrency(EXCHANGE, ACCOUNT)).thenThrow(ExchangeException.class);
		when(accountService.getBuyLimit(EXCHANGE, ACCOUNT)).thenReturn(BUYLIMIT);
		
		orderChecker.run();
		
		verify(orderPlacer, Mockito.never()).addOrder(order);
	}
	
	@Test
	void run_whenExceptionIsThrown_shouldShallowException() throws Exception {
		when(client.fetchSubjectLines()).thenReturn(Arrays.asList(SUBJECT));
		when(orderLineParser.parseOrderLine(SUBJECT)).thenReturn(optional);
		when(accountService.getMainCurrency(EXCHANGE, ACCOUNT)).thenThrow(Exception.class);
		when(accountService.getBuyLimit(EXCHANGE, ACCOUNT)).thenReturn(BUYLIMIT);
		
		orderChecker.run();
		
		verify(orderPlacer, Mockito.never()).addOrder(order);
	}
	
}
