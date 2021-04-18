package com.schnarbiesnmeowers.interview.services.impl;

import static com.schnarbiesnmeowers.interview.utilities.Constants.DEFAULT_PORT;
import static com.schnarbiesnmeowers.interview.utilities.Constants.EMAIL_SUBJECT;
import static com.schnarbiesnmeowers.interview.utilities.Constants.EMAIL_SUBJECT_LOCKED;
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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sun.mail.smtp.SMTPTransport;

/**
 * 
 * @author dylan
 *
 */
@Service
public class EmailService {

	@Value("${email.username}")
	private String username;
	
	@Value("${email.waffle}")
	private String waffle;
	
	@Value("${email.cc}")
	private String cc;
	
	/**
	 * 
	 * @param firstName
	 * @param password
	 * @param email
	 * @throws MessagingException 
	 * @throws AddressException 
	 */
	public void sendNewPasswordEmail(String firstName, String password, String email) throws AddressException, MessagingException {
		Message message = createEmail(firstName,password,email);
		SMTPTransport transport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
		transport.connect(GMAIL_SMTP_SERVER, username, waffle);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();		
	}
	
	public void sendEmailForLockedAccount(String user) throws AddressException, MessagingException {
		Message message = createLockedAccountEmail(user);
		SMTPTransport transport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
		transport.connect(GMAIL_SMTP_SERVER, username, waffle);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();		
	}
	/**
	 * 
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
	 * 
	 * @param firstName
	 * @param password
	 * @param email
	 * @return
	 * @throws AddressException
	 * @throws MessagingException
	 */
	private Message createEmail(String firstName, String password, String email) throws AddressException, MessagingException {
		Message message = new MimeMessage(getEmailSession());
		message.setFrom(new InternetAddress(FROM_EMAIL));
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email,false));
		message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(cc,false));
		message.setSubject(EMAIL_SUBJECT);
		message.setText("Hello " + firstName + ", \n \n Your new account password is: " + password + " \n \n The Support Team");
		message.setSentDate(new Date());
		message.saveChanges();
		return message;
	}
	
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
}
