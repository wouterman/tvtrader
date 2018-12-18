package tvtrader.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tvtrader.model.MailConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MailConfigurationDaoTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private MailConfigurationDao mailConfigurationDao;

	@Test
	void whenFindByName_thenReturnConfiguration() {
		MailConfiguration configuration = new MailConfiguration();
		entityManager.persist(configuration);
		entityManager.flush();

		Optional<MailConfiguration> actual = mailConfigurationDao.findByName(configuration.getName());

		assertEquals(configuration, actual.get());
	}
}
