package tvtrader.application;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;

import tvtrader.utils.Encoding;
import tvtrader.utils.HashingUtility;

@ComponentScan(basePackages = "tvtrader")
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
