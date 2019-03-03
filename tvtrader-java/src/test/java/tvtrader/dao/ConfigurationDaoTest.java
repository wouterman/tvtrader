package tvtrader.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tvtrader.model.Configuration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ConfigurationDaoTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ConfigurationDao configurationDao;

	@Test
	void findByNameShouldReturnConfigurationWhenNameIsKnown() {
		Configuration configuration = new Configuration();
		entityManager.persist(configuration);
		entityManager.flush();

		Optional<Configuration> actual = configurationDao.findByName(configuration.getName());

		assertEquals(configuration, actual.get());
	}
}
