package tvtrader.mail;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tvtrader.exceptionlogger.UnverifiedException;
import tvtrader.services.ConfigurationService;

import javax.mail.*;
import javax.mail.Flags.Flag;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Models an IMAPS mail client.<br>
 * Fetches the subject lines from mails within the timelimit as set with
 * setTimeLimit(int) and with the sender as set with setFromSender(String).<br>
 * If the expected sender is not set the client will default to the TradingView
 * Alert mailaddress. (TradingView {@code <noreply@tradingview.com>})
 * 
 * @author Wouter
 *
 */
@Log4j2
@Component
public class MailClient {

	// Mails older than this will be ignored.
	private static final int DEFAULT_TIME_LIMIT_IN_SECONDS = 300;

	// Default timeout for mail server operations in ms.
	private static final String DEFAULT_TIMEOUT = "10000";

	private ConnectionListenerImpl listener = new ConnectionListenerImpl();
	private int timeLimit = DEFAULT_TIME_LIMIT_IN_SECONDS;

	private ConfigurationService configurationService;

	private Folder folder;
	private Store store;

	@Autowired
	public MailClient(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	/**
	 * Verifies if the connection is accepted by the server, if it even exists.<br>
	 * 
	 * @throws UnverifiedException
	 *             Thrown if the connection could not be verified.
	 */
	public void verify() throws UnverifiedException {
		log.info("Verifying mail server credentials.");
		try {
			openConnection();
			log.info("Connection verified.");
		} catch (MailClientException e) {
			throw new UnverifiedException(
					"Couldn't verify the connection to the mail server. Check the connection settings!", e);
		} finally {
			closeConnection();
		}
	}

	/**
	 * Fetches the subject lines of all valid mails from the server.<br>
	 * If there are no new valid mails or there was a problem fetching mails returns
	 * an empty list.<br>
	 * 
	 * @return List of subject lines.
	 */
	public List<String> fetchSubjectLines() {
		try {
			openConnection();
			folder = store.getFolder(configurationService.getInbox());
			folder.open(Folder.READ_WRITE);

			log.info("Checking for new messages...");
			Message[] messages = folder.getMessages();

			log.debug("Messages: {}", messages.length);

			return checkMails(messages);
		} catch (IllegalStateException | MessagingException | MailClientException e) {
			log.debug("Exception: ", e);
			log.info("Something went wrong while fetching mails from the server. Received the following message: {}",
					e.getMessage());
		} finally {
			closeConnection();
		}

		return Collections.emptyList();
	}

	/**
	 * Opens the connection to the mail server.
	 * 
	 * @return true if connected.
	 * @throws MailClientException
	 *             If something goes wrong while connecting to the server.
	 */
	private boolean openConnection() throws MailClientException {
		Properties mailProps = configurationService.getMailConfigurationAsProperties();
		addTimeOuts(mailProps);

		log.debug("Connecting to the mail server.");
		Session session = Session.getInstance(mailProps, null);

		try {
			store = session.getStore();
			store.addConnectionListener(listener);
			store.connect(configurationService.getUsername(), configurationService.getPassword());
		} catch (IllegalStateException | MessagingException e) {
			log.debug("Exception: ", e);
			throw new MailClientException("Couldn't connect to the mail server!", e);
		}

		sleepForBroadcast();
		return listener.isConnected();
	}

	/**
	 * By default, Javamail uses infinite timeouts which can cause the application
	 * to 'freeze' indefinitely on rare occasions.<br>
	 * <br>
	 * This method sets the timeouts for all operations to 10 seconds.<br>
	 * <br>
	 * See also:
	 * https://www.javacodegeeks.com/2014/06/javamail-can-be-evil-and-force-you-to-restart-your-app-server.html<br>
	 *
	 * @param mailProperties
	 */
	private void addTimeOuts(Properties mailProperties) {
		log.debug("Setting mail client timeouts.");
		String mail = "mail.";
		String protocol = configurationService.getProtocol();
		mailProperties.put(mail + protocol + ".connectiontimeout", DEFAULT_TIMEOUT);
		mailProperties.put(mail + protocol + ".timeout", DEFAULT_TIMEOUT);
		mailProperties.put(mail + protocol + ".writetimeout", DEFAULT_TIMEOUT);
	}

	/**
	 * Filters out all the mails that:<br>
	 * - Aren't from the expected sender.<br>
	 * - Are outside of the time limit.<br>
	 * <br>
	 * Returns a List<String> with the subjects of the remaining messages.<br>
	 * 
	 * @param messages
	 * @return
	 * @throws MessagingException
	 *             If something goes wrong while fetching.
	 */
	private List<String> checkMails(Message[] messages) throws MessagingException {
		List<String> subjectLines = new ArrayList<>();

		log.debug("Processing messages.");
		for (Message msg : messages) {
			if (unread(msg)) {
				LocalDateTime date = convertToLocalDateTime(msg);

				log.debug("Checking if mail is within timelimit.");
				if (withinTimeLimit(date)) {

					Address[] from = msg.getFrom();

					log.debug("Checking if sender is valid.");
					if (addressPresent(from) && fromExpectedSender(from)) {

						log.debug("Fetching subject");
						String subject = msg.getSubject();

						addSubjectLine(subjectLines, subject);
					}
				}

				// Reading the message
				msg.setFlag(Flag.SEEN, true);
			}
			// 'Deleting' the message.
			msg.setFlag(Flag.DELETED, true);
		}

		log.debug("Expunging folder.");
		folder.expunge();

		log.debug("Fetched subjectlines.");
		return subjectLines;
	}

	private boolean unread(Message msg) throws MessagingException {
		return !msg.isSet(Flag.SEEN);
	}

	/**
	 * Retrieves the sent date from the message and converts the old Java Date
	 * object, returned by JavaMail, to the new LocalDateTime API.<br>
	 * <br>
	 * Adapted from https://stackoverflow.com/a/23885950.<br>
	 * 
	 * @throws MessagingException
	 *             If the sentDate couldn't be retrieved from the message.
	 */
	private LocalDateTime convertToLocalDateTime(Message msg) throws MessagingException {
		return LocalDateTime.ofInstant(msg.getSentDate().toInstant(), ZoneId.systemDefault());
	}

	/**
	 * Checks if the received date is within the specified time limit.<br>
	 * 
	 */
	private boolean withinTimeLimit(LocalDateTime receivedDate) {
		return receivedDate.isAfter(LocalDateTime.now().minusSeconds(timeLimit));
	}

	/**
	 * Checks if any senders are present.<br>
	 * 
	 */
	private boolean addressPresent(Address[] from) {
		return from != null && from.length > 0;
	}

	/**
	 * Checks if the sender is the expected sender.<br>
	 * 
	 */
	private boolean fromExpectedSender(Address[] from) {
		return from[0].toString().contains(configurationService.getExpectedSender());
	}

	/**
	 * Add the subject to the list of subject lines.<br>
	 * Duplicate lines will be ignored.<br>
	 * 
	 */
	private void addSubjectLine(List<String> subjectLines, String subject) {
		if (!subjectLines.contains(subject)) {
			log.debug("Adding subject: {}", subject);
			subjectLines.add(subject);
		}
	}

	/**
	 * Closes the connection to the mail server.
	 * 
	 * @return true if successful.
	 */
	protected boolean closeConnection() {
		if (folder != null && folder.isOpen()) {
			try {
				folder.close(false);
			} catch (MessagingException e) {
				log.debug("Failed to close the connection to the mail folder.\nReceived the following exception:\n", e);
			}
		}

		if (store != null) {
			try {
				store.close();
			} catch (MessagingException e) {
				log.debug("Failed to close the connection to the mail store.\nReceived the following exception:\n", e);
			}
		}

		sleepForBroadcast();

		return listener.isClosed();
	}

	/**
	 * Sets the time limit for emails in seconds.<br>
	 * Emails older than this limit will be ignored.<br>
	 * 
	 */
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;

	}

	/**
	 * Sleep for 20 ms<br>
	 * The connection event isn't broadcasted immediately so we wait a short
	 * time.<br>
	 * 
	 */
	private void sleepForBroadcast() {
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			log.error("Connection event sleep got interrupted!");
			Thread.currentThread().interrupt();
		}
	}

}
