package tvtrader.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tvtrader.model.Account;
import tvtrader.model.ApiCredentials;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountRepositoryTest {
	private static final String EXCHANGE = "EXCHANGE";
	private static final String ACCOUNTNAME = "ACCOUNTNAME";
	private static final String CURRENCY = "CURRENCY";

	private AccountRepository repository;

	@BeforeAll
	synchronized static void startup() {

	}

	@BeforeEach
	void init() throws Exception {
		repository = new AccountRepository();
	}

	@Test
	void hasAccount_whenUnknownAccount_shouldReturnFalse() {
		assertFalse(repository.hasAccount(EXCHANGE, ACCOUNTNAME));
	}
	
	@Test
	void hasAccount_whenKnownAccount_shouldReturnTrue() {
		Account account = new Account(EXCHANGE, ACCOUNTNAME, CURRENCY, 0, 0, 0, 0, new ApiCredentials("", ""));
		repository.addAccount(EXCHANGE, account);
		assertTrue(repository.hasAccount(EXCHANGE, ACCOUNTNAME));
	}
	
	@Test
	void removeAccount_whenKnownAccount_shouldRemoveAccount() {
		Account account = new Account(EXCHANGE, ACCOUNTNAME, CURRENCY, 0, 0, 0, 0, new ApiCredentials("", ""));
		repository.addAccount(EXCHANGE, account);
		
		repository.removeAccount(EXCHANGE, ACCOUNTNAME);
		
		assertFalse(repository.hasAccount(EXCHANGE, ACCOUNTNAME));
	}
	
	@Test
	void removeAccount_whenUnknownAccount_shouldDoNothing() {
		repository.removeAccount(EXCHANGE, ACCOUNTNAME);
		
		assertFalse(repository.hasAccount(EXCHANGE, ACCOUNTNAME));
	}

}
