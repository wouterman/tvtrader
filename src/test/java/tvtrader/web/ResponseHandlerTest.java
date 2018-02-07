package tvtrader.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import test.logger.Logger;

public class ResponseHandlerTest {
	private static final int INVALID_RESPONSE = 503;
	private static final int HTTP_OK = 200;
	private static final String RESPONSE_MESSAGE = "RESPONSE MESSAGE";

	private Response.Builder builder;
	private ResponseHandler handler;
	
	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}
	
	@BeforeEach
	void setup() {
		handler = new ResponseHandler();

		Request.Builder requestBuilder = new Request.Builder();
		requestBuilder.url("http://example.com");
		Request request = requestBuilder.build();
		
		builder = new Response.Builder();
		builder.protocol(Protocol.HTTP_2);
		builder.message(RESPONSE_MESSAGE);
		builder.request(request);
	}
	
	@Test
	void checkResponse_whenResponseIsSuccessful_shouldReturnTrue() {
		builder.code(HTTP_OK);
		
		Response response = builder.build();
		
		assertTrue(handler.checkResponse(response));		
	}
	
	@Test
	void checkResponse_whenResponseIsNotSuccessful_shouldReturnFalse() {
		builder.code(INVALID_RESPONSE);
		
		Response response = builder.build();
				
		assertFalse(handler.checkResponse(response));
	}
	
	@Test
	void getMessage_whenResponseIsSuccessful_shouldReturnHttpMessage() {
		builder.code(HTTP_OK);
		Response response = builder.build();
				
		assertEquals(RESPONSE_MESSAGE, handler.getMessage(response));
	}
	
	@Test
	void getMessage_whenResponseIsNotSuccessful_shouldReturnHttpMessage() {
		builder.code(INVALID_RESPONSE);
		Response response = builder.build();
				
		assertEquals(RESPONSE_MESSAGE, handler.getMessage(response));
	}
}
