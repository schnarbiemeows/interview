package com.schnarbiesnmeowers.interview.services.impl;

import static com.schnarbiesnmeowers.interview.utilities.Constants.DEFAULT_PORT;
import static com.schnarbiesnmeowers.interview.utilities.Constants.EMAIL_SUBJECT;
import static com.schnarbiesnmeowers.interview.utilities.Constants.EMAIL_SUBJECT_LOCKED;
import static com.schnarbiesnmeowers.interview.utilities.Constants.EMAIL_TESTING;
import static com.schnarbiesnmeowers.interview.utilities.Constants.FROM_EMAIL;
import static com.schnarbiesnmeowers.interview.utilities.Constants.GMAIL_SMTP_SERVER;
import static com.schnarbiesnmeowers.interview.utilities.Constants.SIMPLE_MAIL_TRANSFER_PROTOCOL;
import static com.schnarbiesnmeowers.interview.utilities.Constants.SMTP_AUTH;
import static com.schnarbiesnmeowers.interview.utilities.Constants.SMTP_HOST;
import static com.schnarbiesnmeowers.interview.utilities.Constants.SMTP_PORT;
import static com.schnarbiesnmeowers.interview.utilities.Constants.SMTP_STARTTLS_ENABLE;
import static com.schnarbiesnmeowers.interview.utilities.Constants.SMTP_STARTTLS_REQUIRED;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sun.mail.smtp.SMTPTransport;

/**
 * Email service class
 * sends emails for:
 * sending the user a new password
 * sending the user their username
 * informing the admins when a user locks themselves out of their account
 * @author Dylan I. Kessler
 *
 */
@Service
public class EmailService {

	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");
	
	@Value("${email.username}")
	private String username;
	
	@Value("${email.waffle}")
	private String waffle;
	
	@Value("${email.cc}")
	private String cc;
	
	/**
	 * method to send an email to the user with a new password in it
	 * @param firstName
	 * @param password
	 * @param email
	 * @throws MessagingException 
	 * @throws AddressException 
	 */
	public void sendNewPasswordEmail(String firstName, String password, String email) throws AddressException, MessagingException {
		String messageToSend = "Hello " + firstName + ", \n \n Your new account password is: " + password + " \n \n The Support Team";
		Message message = createEmail(firstName,password,email,messageToSend);
		SMTPTransport transport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
		transport.connect(GMAIL_SMTP_SERVER, username, waffle);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();		
	}
	
	/**
	 * method to send an email to the user with their username in it
	 * @param firstName
	 * @param username
	 * @param email
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void sendEmailWithUsername(String firstName, String username, String email) throws AddressException, MessagingException {
		String messageToSend = "Hello " + firstName + ", \n \n Your username is: " + username + " \n \n The Support Team";
		Message message = createEmail(firstName,username,email,messageToSend);
		SMTPTransport transport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
		transport.connect(GMAIL_SMTP_SERVER, username, waffle);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();		
	}
	
	/**
	 * method to send an email to the admins when a user locks themselves out of their account
	 * @param user
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void sendEmailForLockedAccount(String user) throws AddressException, MessagingException {
		Message message = createLockedAccountEmail(user);
		SMTPTransport transport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
		transport.connect(GMAIL_SMTP_SERVER, username, waffle);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();		
	}
	
	/**
	 * method to get the email section
	 * @return
	 */
	private Session getEmailSession() {
		Properties properties = System.getProperties();
		properties.put(SMTP_HOST ,GMAIL_SMTP_SERVER );
		properties.put(SMTP_PORT , DEFAULT_PORT);
		properties.put(SMTP_AUTH , true);
		properties.put(SMTP_STARTTLS_ENABLE , true);
		properties.put(SMTP_STARTTLS_REQUIRED , true);
		return Session.getInstance(properties,null);
	}
	
	/**
	 * method to create the Message email object
	 * @param firstName
	 * @param password
	 * @param email
	 * @param messageToSend
	 * @return
	 * @throws AddressException
	 * @throws MessagingException
	 */
	private Message createEmail(String firstName, String password, String email,String messageToSend) throws AddressException, MessagingException {
		Message message = new MimeMessage(getEmailSession());
		message.setFrom(new InternetAddress(FROM_EMAIL));
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email,false));
		message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(cc,false));
		message.setSubject(EMAIL_SUBJECT);
		message.setText(messageToSend);
		message.setSentDate(new Date());
		message.saveChanges();
		return message;
	}
	
	/**
	 * method to create the Message email object for a "Locked account" email
	 * @param username
	 * @return
	 * @throws AddressException
	 * @throws MessagingException
	 */
	private Message createLockedAccountEmail(String username) throws AddressException, MessagingException {
		Message message = new MimeMessage(getEmailSession());
		message.setFrom(new InternetAddress(FROM_EMAIL));
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(cc,false));
		message.setSubject(EMAIL_SUBJECT_LOCKED);
		message.setText("User --> " + username + " <-- has been locked out of their account");
		message.setSentDate(new Date());
		message.saveChanges();
		return message;
	}
	
	public void testEmail() {
		try {
			Message message = createTestEmail();
			SMTPTransport transport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
			transport.connect(GMAIL_SMTP_SERVER, username, waffle);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();	
		} catch(AddressException ae) {
			logAction("AddressException sending test email --> " + ae.getMessage());
			
		} catch(MessagingException me) {
			logAction("MessagingException sending test email  --> " + me.getMessage());
		} catch(Exception ee) {
			logAction("General Exception sending test email  --> " + ee.getMessage());
		}
	}
	
	public Message createTestEmail() throws AddressException, MessagingException {
		Message message = new MimeMessage(getEmailSession());
		message.setFrom(new InternetAddress(FROM_EMAIL));
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(cc,false));
		message.setSubject(EMAIL_TESTING);
		message.setText("testing this email");
		message.setSentDate(new Date());
		message.saveChanges();
		return message;
	}
	/**
	 * 
	 * @param message
	 */
	private static void logAction(String message) {
    	System.out.println(message);
    	applicationLogger.debug(message);
    }
}
