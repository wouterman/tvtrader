package tvtrader.utils;

import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import lombok.NonNull;
import lombok.SneakyThrows;

/**
 * Responsible for calculating the hash signature required for account specific
 * communication with the exchanges. <br>
 * When instantiating this class also call setEncoding(String)!<br>
 * 
 * @author Wouter
 *
 */
public class HashingUtility {
	private String encoding;

	public HashingUtility(Encoding encoding) {
		this.encoding = encoding.getEncoding();
	}

	@SneakyThrows
	public String calculateSignature(@NonNull String data, @NonNull String key) {
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), encoding);

		Mac mac = Mac.getInstance(encoding);
		mac.init(signingKey);

		return toHexString(mac.doFinal(data.getBytes()));
	}

	private String toHexString(byte[] bytes) {
		try (Formatter formatter = new Formatter();) {
			for (byte b : bytes) {
				formatter.format("%02x", b);
			}

			return formatter.toString();
		}
	}
}
