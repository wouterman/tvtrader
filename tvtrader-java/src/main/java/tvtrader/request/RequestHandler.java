package tvtrader.request;

import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

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
	 * @return The response.
	 * @throws IOException
	 *             If anything goes wrong while contacting the server.
	 */
	public Response sendRequest(Url endpoint) throws IOException {
		log.debug("Building request with endpoint: {}", endpoint.getUrl());
		Builder builder = setupBuilder(endpoint.getUrl());
		addHeaders(endpoint.getHeaders(), builder);
		builder.method(endpoint.getMethod(), null);
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

	private Response sendRequest(Request request) throws IOException {
		log.debug("Sending request...");
		return CLIENT.newCall(request).execute();
	}
}
