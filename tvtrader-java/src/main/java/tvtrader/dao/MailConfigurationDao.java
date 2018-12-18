package tvtrader.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tvtrader.model.Account;
import tvtrader.model.MailConfiguration;

import java.util.Optional;

@Transactional
@Repository
public interface MailConfigurationDao extends JpaRepository<MailConfiguration, String> {

	public Optional<MailConfiguration> findByName(String name);

	public MailConfiguration save(MailConfiguration configuration);

}
