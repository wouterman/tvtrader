package tvtrader.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import tvtrader.model.Configuration;

@Repository
public class ConfigurationDao {
		
	@PersistenceContext
	private EntityManager entityManager;

	public void create(Configuration configuration) {
		entityManager.persist(configuration);
	}

	public void update(Configuration configuration) {
		entityManager.merge(configuration);
	}

	public Configuration getConfigurationById(long id) {
		return entityManager.find(Configuration.class, id);
	}

	public void delete(long id) {
		Configuration apiRequest = getConfigurationById(id);
		if (apiRequest != null) {
			entityManager.remove(apiRequest);
		}
	}
}
