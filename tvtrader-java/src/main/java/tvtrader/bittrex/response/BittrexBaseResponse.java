package tvtrader.bittrex.response;

import java.util.List;

public abstract class BittrexBaseResponse<T> {
	private boolean success;
	private String message;
	
	public boolean isSuccess() {
		return success;
	}
	
	public String getMessage() {
		return message;
	}
	
	public abstract List<T> getResult();
}
