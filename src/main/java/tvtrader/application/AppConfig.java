package tvtrader.application;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import tvtrader.utils.Encoding;
import tvtrader.utils.HashingUtility;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@ComponentScan(basePackages= {"tvtrader"})
@Configuration
public class AppConfig {

	@Bean
	public ScheduledExecutorService scheduledExecutorService() {
		return Executors.newSingleThreadScheduledExecutor();
	}

	@Bean
	public Gson gson() {
		return new Gson();
	}

	@Bean(name = "BittrexHasher")
	public HashingUtility bittrexHasher() {
		return new HashingUtility(Encoding.SHA512);
	}

}
