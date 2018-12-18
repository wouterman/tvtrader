package tvtrader.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tvtrader.model.Account;
import tvtrader.model.AccountId;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class AccountDaoTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private AccountDao accountDao;

	@Test
	public void whenFindByExchangeAndName_thenReturnAccount() {
		Account account = new Account();

		entityManager.persist(account);
		entityManager.flush();

		Optional<Account> actual = accountDao.findById(new AccountId(account.getExchange(), account.getName()));

		assertEquals(account, actual.get());
	}

	@Test
	public void whenFindAll_thenReturnAllAccounts() {
		Account account = new Account();

		entityManager.persist(account);
		entityManager.flush();

		List<Account> actual = accountDao.findAll();

		assertEquals(1, actual.size());
		assertEquals(account, actual.get(0));
	}
}
