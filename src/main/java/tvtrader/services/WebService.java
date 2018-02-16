package tvtrader.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import okhttp3.Response;
import tvtrader.exchange.ExchangeException;
import tvtrader.web.RequestHandler;
import tvtrader.web.Url;

@Component
public class WebService {
	@Autowired 
	private RequestHandler requestHandler;
	
	public String sendRequest(Url url) throws ExchangeException {
		try {
			Response response = requestHandler.sendRequest(url);
			
			if (response.isSuccessful()) {
				return response.body().string();
			}  else {
				throw checkError(response);
			}
			
		} catch (IOException e) {
			throw new ExchangeException("Something went wrong while sending a request.", e);
		}
		
	}

	private ExchangeException checkError(Response response) throws ExchangeException {
		int httpCode = response.code();
		String message = response.message();
		
		throw new ExchangeException("Something went wrong while sending a request. Received the following error code " + httpCode + " with message: " + message);
	}
	
}
