package tvtrader.services;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.logging.LogManager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import test.logger.Logger;
import tvtrader.exchange.ExchangeException;
import tvtrader.web.RequestHandler;
import tvtrader.web.Url;

public class WebServiceTest {
	private static final int INVALID_RESPONSE = 503;
	private static final String RESPONSE_MESSAGE = "RESPONSE MESSAGE";

	private Response.Builder builder;
	
	@Mock
	private RequestHandler requestHandler;
	
	@InjectMocks
	private WebService service;
	
	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}
	
	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);

		Request.Builder requestBuilder = new Request.Builder();
		requestBuilder.url("http://example.com");
		Request request = requestBuilder.build();
		
		builder = new Response.Builder();
		builder.protocol(Protocol.HTTP_2);
		builder.message(RESPONSE_MESSAGE);
		builder.request(request);
	}
	
	@Test
	void sendRequest_whenResponseIsSuccessful_shouldReturnMessageBody() throws Exception {
		String expected = "Received call";
		MockWebServer server = new MockWebServer();
		server.enqueue(new MockResponse().setBody(expected));
		server.start();
		LogManager.getLogManager().reset();
		
		HttpUrl baseUrl = server.url("/handlerTest");
		Url url = new Url(baseUrl.toString());
		
		Mockito.when(requestHandler.sendRequest(url)).thenCallRealMethod();
		
		String response = service.sendRequest(url);
		
		assertEquals(expected, response);
		
		server.shutdown();	
	}
	
	@Test
	void sendRequest_whenResponseIsNotSuccessful_shouldThrowExchangeException() throws Exception {
		Url url = new Url("");
		builder.code(INVALID_RESPONSE);
		
		Response response = builder.build();
		
		Mockito.when(requestHandler.sendRequest(url)).thenReturn(response);
		
		assertThrows(ExchangeException.class, () -> service.sendRequest(url));
	}
	
	@Test
	void sendRequest_whenRequestFails_shouldThrowExchangeException() throws Exception {
		Url url = new Url("");
		
		Mockito.when(requestHandler.sendRequest(url)).thenThrow(IOException.class);
		
		assertThrows(ExchangeException.class, () -> service.sendRequest(url));
	}
	
}
