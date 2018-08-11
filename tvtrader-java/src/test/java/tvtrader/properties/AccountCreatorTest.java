package tvtrader.properties;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import tvtrader.model.Account;
import tvtrader.stubs.AccountPropertiesStubs;
import tvtrader.stubs.AccountStubs;

import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AccountCreatorTest {
	private static final String BITTREX = "bittrex";

	private static final String CORRUPT_FIELDS = "corruptFields";

	private AccountCreator accountCreator;
	private Properties config;

	@BeforeAll
	synchronized static void startup() {

	}

	@BeforeEach
	void setup() {
		accountCreator = new AccountCreator();
	}

	@Test
	void extractAccounts_whenGivenPropertiesFileWithOneAccount_shouldReturnListWithThatOneAccount()
			throws Exception {
		Properties config = AccountPropertiesStubs.getBittrexBTCAccount();
		Account expected = AccountStubs.getBtcAccount();

		List<Account> accounts = accountCreator.extractAccounts(BITTREX, config);

		assertTrue(accounts.size() == 1);
		Account actual = accounts.get(0);

		assertEquals(expected, actual);
	}
	
	@Test
	void extractAccounts_whenGivenPropertiesFileWithTwoDuplicateAccounts_shouldReturnListWithOneAccount() throws Exception {
		Properties config = AccountPropertiesStubs.getDuplicateBTCAccounts();
		Account expected = AccountStubs.getBtcAccount();

		List<Account> accounts = accountCreator.extractAccounts(BITTREX, config);

		assertEquals(1, accounts.size());
		Account actual = accounts.get(0);

		assertEquals(expected, actual);
	}

	@Test
	void extractAccounts_whenGivenPropertiesFileWithTwoDifferentAccounts_shouldReturnListWithThoseTwoAccounts()
			throws Exception {
		config = AccountPropertiesStubs.getBTCAndETHAccounts();
		Account expectedBtc = AccountStubs.getBtcAccount();
		Account expectedEth = AccountStubs.getEthAccount();

		List<Account> accounts = accountCreator.extractAccounts(BITTREX, config);

		assertEquals(2, accounts.size());
		Account actualBtc = accounts.get(0);
		Account actualEth = accounts.get(1);

		assertAll(() -> assertEquals(expectedBtc, actualBtc),
				() -> assertEquals(expectedEth, actualEth));
	}
	
	@Test
	void extractAccounts_whenGivenPropertiesFileWithNoAccounts_shouldReturnEmptyList() throws Exception {
		config = AccountPropertiesStubs.getNoAccounts();
		
		List<Account> accounts = accountCreator.extractAccounts(BITTREX, config);
		
		assertEquals(0, accounts.size());
	}
	
	@ParameterizedTest
	@MethodSource(value = {CORRUPT_FIELDS})
	void extractAccounts_whenFieldIsMissing_shouldThrowInvalidAccountException(Properties config) throws Exception {
		assertThrows(InvalidAccountException.class, () -> accountCreator.extractAccounts(BITTREX, config));
	}
	
	@SuppressWarnings("unused")
	private static Stream<Properties> corruptFields() {
		return Stream.of(
				AccountPropertiesStubs.getMissingTrailingstoplossField(),
				AccountPropertiesStubs.getMissingStoplossField(),
				AccountPropertiesStubs.getMissingSecretField(),
				AccountPropertiesStubs.getMissingMinimumGainField(),
				AccountPropertiesStubs.getMissingMainCurrencyField(),
				AccountPropertiesStubs.getMissingBuyLimitField(),
				AccountPropertiesStubs.getMissingApikeyField(),
				AccountPropertiesStubs.getUnparseableMinimumGain(),
				AccountPropertiesStubs.getUnparseableTrailingstoploss(),
				AccountPropertiesStubs.getUnparseableStoploss(),
				AccountPropertiesStubs.getUnparseableBuyLimit()
				);
	}

}
