package tvtrader.web;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wrapper for a url. Holds the base url, parameters (if any) and headers (if any).
 * 
 * @author Wouter
 *
 */
public class Url {
    public static final String GET = "GET";
    private UriComponentsBuilder builder;

    @Setter
	private String baseUrl;

    @Setter @Getter
	private String method;

    @Getter
	private List<String> parameters;
    @Getter
	private Map<String, String> headers;
	
	public Url(String url) {
		baseUrl = url;
		parameters = new ArrayList<>();
		headers = new HashMap<>();
		method = GET;
	}
	
	/**
	 * Returns the full endpoint url.
	 * 
	 * @return
	 */
	public String getUrl() {
        StringBuilder url = new StringBuilder(baseUrl);

        for (int i = 0; i < parameters.size(); i++) {
            if (i == 0) {
                url.append(parameters.get(i));
            } else {
                url.append("&" + parameters.get(i));
            }
        }

        return url.toString();
    }

	public void addHeader(String header, String value) {
		headers.put(header, value);
	}
	
	public void addParameters(String...params) {
		for (String p : params) {
			parameters.add(p);
		}
	}
	
	
}
