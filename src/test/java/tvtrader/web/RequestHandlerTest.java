package tvtrader.web;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.logging.LogManager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import test.logger.Logger;

class RequestHandlerTest {
	private RequestHandler handler;
	
	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}
	
	@BeforeEach
	void setup() {
		handler = new RequestHandler();
	}
	
	@Test
	public void sendRequest_whenEndpointIsNonExisting_shouldThrowIOException() throws IOException {
		Url corruptUrl = new Url("http://invalid.123456");
		assertThrows(IOException.class, () -> handler.sendRequest(corruptUrl));

	}
	
	@Test
	void sendRequest_whenProvidedWithUrl_shouldSendRequestWithProperHeaders() throws IOException, InterruptedException {
		String expected = "Received call";
		MockWebServer server = new MockWebServer();
		server.enqueue(new MockResponse().setBody(expected));
		server.start();
		LogManager.getLogManager().reset();
		
		HttpUrl baseUrl = server.url("/handlerTest");
		Url url = new Url(baseUrl.toString());
		url.addHeader("foo", "bar");
		
		String actual = handler.sendRequest(url);
		RecordedRequest request = server.takeRequest();
		String actualHeader = request.getHeader("foo");
		
		assertEquals(expected, actual, "Response does not equal expected!");
		assertNotNull(actualHeader);
		assertEquals("bar", actualHeader);
		
		server.shutdown();
	}
}
