package tvtrader.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import test.logger.Logger;
import tvtrader.accounts.Account;

class AccountRepositoryTest {
	private static final String EXCHANGE = "EXCHANGE";
	private static final String ACCOUNTNAME = "ACCOUNTNAME";
	private AccountRepository repository;

	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
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
		Account account = new Account(ACCOUNTNAME, null, 0, 0, 0, 0, null);
		repository.addAccount(EXCHANGE, account);
		assertTrue(repository.hasAccount(EXCHANGE, ACCOUNTNAME));
	}
	
	@Test
	void removeAccount_whenKnownAccount_shouldRemoveAccount() {
		Account account = new Account(ACCOUNTNAME, null, 0, 0, 0, 0, null);
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
