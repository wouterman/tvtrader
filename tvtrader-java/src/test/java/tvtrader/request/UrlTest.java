package tvtrader.request;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UrlTest {
	private static final String THIRD_PARAMETER = "mixedParam=abc123";
	private static final String SECOND_PARAMETER = "numberParam=123";
	private static final String DEFAULT_PARAMETER = "letterParam=abc";
	private static final String BASE_URL = "www.example.com/service?";
	private static final String SECOND_HEADER_VALUE = "123";
	private static final String SECOND_HEADER_KEY = "numbers";
	private static final String DEFAULT_HEADER_KEY = "letters";
	private static final String DEFAULT_HEADER_VALUE = "abc";
	private Url url;
	
	@BeforeAll
	synchronized static void startup() {

	}
	
	@BeforeEach
	void setup() {
		url = new Url(BASE_URL);
	}

	@Test
	void addHeader_whenOneHeaderIsAdded_shouldHaveOneHeader() {
		url.addHeader(DEFAULT_HEADER_KEY, DEFAULT_HEADER_VALUE);
		
		Map<String, String> headers = url.getHeaders();
		
		assertEquals(1, headers.size());
		assertTrue(headers.containsKey(DEFAULT_HEADER_KEY), "Headers should have contained the default header key.");
	}

	@Test
	void addHeader_whenTwoHeadersAreAdded_shouldHaveTwoHeaders() {
		url.addHeader(DEFAULT_HEADER_KEY, DEFAULT_HEADER_VALUE);
		url.addHeader(SECOND_HEADER_KEY, SECOND_HEADER_VALUE);
		
		Map<String, String> headers = url.getHeaders();
		
		assertEquals(2, headers.size());
		assertTrue(headers.containsKey(DEFAULT_HEADER_KEY), "Headers should have contained the default header key.");
		assertTrue(headers.containsKey(SECOND_HEADER_KEY), "Headers should have contained the second header key.");
	}
	
	@Test
	void getParameterizedUrl_whenParameterIsAdded_shouldHaveParameterAddedToEndOfBaseUrl() {
		url.addParameters(DEFAULT_PARAMETER);
		String expected = BASE_URL + DEFAULT_PARAMETER;
		
		String actual = url.getUrl();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void getParameterizedUrl_whenMultipleParametersAreAdded_shouldHaveThoseParametersInOrder() {
		url.addParameters(DEFAULT_PARAMETER, SECOND_PARAMETER, THIRD_PARAMETER);
		String expected = BASE_URL + DEFAULT_PARAMETER + "&" + SECOND_PARAMETER + "&" + THIRD_PARAMETER;
		
		String actual = url.getUrl();
		
		assertEquals(expected, actual);
	}
}
