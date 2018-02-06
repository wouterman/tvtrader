package tvtrader.web;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

/**
 * Handles the requests to the endpoint.<br>
 * <br>
 * Basically a wrapper class for the OkHttp3 library.<br>
 * 
 * @author Wouter
 *
 */
@Log4j2
@Component
public class RequestHandler {
	private static final OkHttpClient CLIENT = new OkHttpClient();

	/**
	 * Sends a requests to the url.
	 * 
	 * @return The response as a string.
	 * @throws IOException
	 *             If anything goes wrong while contacting the server.
	 */
	public String sendRequest(Url endpoint) throws IOException {
		log.debug("Building request with endpoint: {}", endpoint.getUrl());
		Builder builder = setupBuilder(endpoint.getUrl());
		addHeaders(endpoint.getHeaders(), builder);
		Request request = builder.build();
		return sendRequest(request);
	}

	private Builder setupBuilder(String url) {
		Builder builder = new Request.Builder();
		builder = builder.url(url);
		return builder;
	}

	private void addHeaders(Map<String, String> headers, Builder builder) {
		log.debug("Adding headers: {}", headers);
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			builder.addHeader(entry.getKey(), entry.getValue());
		}
	}

	private String sendRequest(Request request) throws IOException {
		log.debug("Sending request...");
		try (Response response = CLIENT.newCall(request).execute();) {
			String responseBody = response.body().string();
			log.debug("Received response: {}", responseBody);
			return responseBody;
		}
	}
}
