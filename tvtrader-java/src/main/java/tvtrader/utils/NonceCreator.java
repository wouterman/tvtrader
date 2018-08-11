package tvtrader.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

/**
 * Nonce = Number only once.<br>
 * <br>
 * Most exchanges require that a nonce is added to ensure unique hashes with
 * each request.<br>
 * This number needs to increment with each request.<br>
 * <br>
 * 
 * @author Wouter
 *
 */
@Log4j2
@UtilityClass
public class NonceCreator {
	private long lastNonce = 0;

	/**
	 * This method creates a nonce with a unique number based off the time since
	 * epoch in milliseconds to ensure an incremental sequence.<br>
	 * If the current nonce is somehow the same as the last the method will
	 * wait for 1 ms and generate a new nonce.<br>
	 *
	 * @return Nonce as a long.
	 */
	public long getNonce() {
		long nonce = System.currentTimeMillis();

		if (nonce <= lastNonce) {
			try {
				Thread.sleep(1);
				nonce = getNonce();
			} catch (InterruptedException e) {
				log.debug("NonceCreator got interrupted while sleeping. Interrupting thread.");
				Thread.currentThread().interrupt();
			}
		}

		lastNonce = nonce;

		return nonce;
	}
}
