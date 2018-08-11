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
import tvtrader.dao.AccountRepository;
import tvtrader.exchange.ExchangeException;
import tvtrader.model.Account;
import tvtrader.model.ApiCredentials;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
class AccountServiceTest {
	
	private static final String BTC = "BTC";
	private static final String ACCOUNT = "ACCOUNT";
	private static final String EXCHANGE = "EXCHANGE";
	@Mock
	private AccountRepository repository;
	@InjectMocks
	private AccountService service;

	@BeforeEach
	void init() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void addAccount_shouldDelegateToRepository() {
		ApiCredentials credentials = new ApiCredentials("", "");
		Account account = new Account(EXCHANGE, ACCOUNT, BTC, 0, 0, 0, 0, credentials);
		service.addAccount(EXCHANGE, account);
		
		Mockito.verify(repository, Mockito.times(1)).addAccount(EXCHANGE, account);
	}

	@Test
	void hasAccount_shouldDelegateToRepository() {
		service.hasAccount(EXCHANGE, ACCOUNT);
		
		Mockito.verify(repository, Mockito.times(1)).hasAccount(EXCHANGE, ACCOUNT);
	}

	@Test
	void getAccount_shouldDelegateToRepository() {
		service.getAccount(EXCHANGE, ACCOUNT);

		Mockito.verify(repository, Mockito.times(1)).getAccount(EXCHANGE, ACCOUNT);
	}
	
	@Test
	void getBuyLimit_whenAccountIsKnown_shouldReturnBuylimit() throws Exception {
		ApiCredentials credentials = new ApiCredentials("", "");
		Account account = new Account(EXCHANGE, ACCOUNT, BTC, 1, 0, 0, 0, credentials);

		Mockito.when(repository.getAccount(EXCHANGE, ACCOUNT)).thenReturn(account);
		
		double actual = service.getBuyLimit(EXCHANGE, ACCOUNT);
		
		assertEquals(1, actual);
	}

	@Test
	void getBuyLimit_whenAccountIsUnknown_shouldThrowExchangeException() {
		Mockito.when(repository.getAccount(EXCHANGE, EXCHANGE)).thenReturn(null);
			
		assertThrows(ExchangeException.class, () -> service.getBuyLimit(EXCHANGE, ACCOUNT));;
	}
	
	@Test
	void getStoploss_whenAccountIsKnown_shouldReturnStoploss() throws Exception {
		ApiCredentials credentials = new ApiCredentials("", "");
		Account account = new Account(EXCHANGE, ACCOUNT, BTC, 0, 1, 0, 0, credentials);

		Mockito.when(repository.getAccount(EXCHANGE, ACCOUNT)).thenReturn(account);
		
		double actual = service.getStoploss(EXCHANGE, ACCOUNT);
		
		assertEquals(1, actual);
	}

	@Test
	void getStoploss_whenAccountIsUnknown_shouldThrowExchangeException() {
		Mockito.when(repository.getAccount(EXCHANGE, EXCHANGE)).thenReturn(null);
			
		assertThrows(ExchangeException.class, () -> service.getStoploss(EXCHANGE, ACCOUNT));;
	}
	
	@Test
	void getTrailingStoploss_whenAccountIsKnown_shouldReturnTrailingStoploss() throws Exception {
		ApiCredentials credentials = new ApiCredentials("", "");
		Account account = new Account(EXCHANGE, ACCOUNT, BTC, 0, 0, 1, 0, credentials);

		Mockito.when(repository.getAccount(EXCHANGE, ACCOUNT)).thenReturn(account);
		
		double actual = service.getTrailingStoploss(EXCHANGE, ACCOUNT);
		
		assertEquals(1, actual);
	}
	
	@Test
	void getTrailingStoploss_whenAccountIsUnknown_shouldThrowExchangeException() {
		Mockito.when(repository.getAccount(EXCHANGE, EXCHANGE)).thenReturn(null);
			
		assertThrows(ExchangeException.class, () -> service.getTrailingStoploss(EXCHANGE, ACCOUNT));;
	}

	@Test
	void getMinimumGain_whenAccountIsKnown_shouldReturnMinimumGain() throws Exception {
		ApiCredentials credentials = new ApiCredentials("", "");
		Account account = new Account(EXCHANGE, ACCOUNT, BTC, 0, 0, 0, 1, credentials);

		Mockito.when(repository.getAccount(EXCHANGE, ACCOUNT)).thenReturn(account);
		
		double actual = service.getMinimumGain(EXCHANGE, ACCOUNT);
		
		assertEquals(1, actual);
	}
	
	@Test
	void getMinimumGain_whenAccountIsUnknown_shouldThrowExchangeException() {
		Mockito.when(repository.getAccount(EXCHANGE, EXCHANGE)).thenReturn(null);
			
		assertThrows(ExchangeException.class, () -> service.getMinimumGain(EXCHANGE, ACCOUNT));;
	}
	
	@Test
	void getMainCurrency_whenAccountIsKnown_shouldReturnMainCurrency() throws Exception {
		ApiCredentials credentials = new ApiCredentials("", "");
		Account account = new Account(EXCHANGE, ACCOUNT, BTC, 0, 0, 0, 0, credentials);

		Mockito.when(repository.getAccount(EXCHANGE, ACCOUNT)).thenReturn(account);
		
		String actual = service.getMainCurrency(EXCHANGE, ACCOUNT);
		
		assertEquals(BTC, actual);
	}

	@Test
	void getMainCurrency_whenAccountIsUnknown_shouldThrowExchangeException() {
		Mockito.when(repository.getAccount(EXCHANGE, EXCHANGE)).thenReturn(null);
			
		assertThrows(ExchangeException.class, () -> service.getMainCurrency(EXCHANGE, ACCOUNT));;
	}
	
	@Test
	void getCredentials_whenAccountIsKnown_shouldReturnCredentials() throws Exception {
		ApiCredentials credentials = new ApiCredentials("", "");
		Account account = new Account(EXCHANGE, ACCOUNT, BTC, 0, 0, 0, 0, credentials);

		Mockito.when(repository.getAccount(EXCHANGE, ACCOUNT)).thenReturn(account);
		
		ApiCredentials actual = service.getCredentials(EXCHANGE, ACCOUNT);
		
		assertEquals(credentials, actual);
	}

	@Test
	void getCredentials_whenAccountIsUnknown_shouldThrowExchangeException() {
		Mockito.when(repository.getAccount(EXCHANGE, EXCHANGE)).thenReturn(null);
			
		assertThrows(ExchangeException.class, () -> service.getCredentials(EXCHANGE, ACCOUNT));;
	}

}
