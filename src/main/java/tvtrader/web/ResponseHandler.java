package tvtrader.web;

import lombok.extern.log4j.Log4j2;
import okhttp3.Response;

@Log4j2
public class ResponseHandler {
	
	public boolean checkResponse(Response response) {
		return response.isSuccessful();
	}
	
	public String getMessage(Response response) {
		return response.message();
	}
	
	public String getResponseBody(Response response) {
		String responseBody = response.body().toString();
		log.debug("Received response: {}", responseBody);
		
		return responseBody;
		
	}

}
