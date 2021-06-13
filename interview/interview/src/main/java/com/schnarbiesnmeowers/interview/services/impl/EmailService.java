package com.schnarbiesnmeowers.interview.services.impl;

import static com.schnarbiesnmeowers.interview.utilities.Constants.DEFAULT_PORT;
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
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.schnarbiesnmeowers.interview.email.EmailTemplate;
import com.schnarbiesnmeowers.interview.email.ForgotPasswordEmailTemplate;
import com.schnarbiesnmeowers.interview.email.ForgotUsernameEmailTemplate;
import com.schnarbiesnmeowers.interview.email.ManagementEmailTemplate;
import com.schnarbiesnmeowers.interview.email.NoAddressFoundPREmailTemplate;
import com.schnarbiesnmeowers.interview.email.NoAddressFoundUREmailTemplate;
import com.schnarbiesnmeowers.interview.email.VerifyRegistrationEmailTemplate;
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
	
	@Value("${success.email.link}")
	private String successEmailUrl;
	
	@Value("${success.email.expiration.minutes}")
	private int linkExpirationTime;
	
	static final String FROM = "interviewmailer76@gmail.com";
	static final String TO = "dylanikessler@yahoo.com";
	static final String SUBJECT = "Amazon SES test (AWS SDK for Java)";
	static final String HTMLBODY = "<h1>Amazon SES test (AWS SDK for Java)</h1>"
		      + "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
		      + "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>" 
		      + "AWS SDK for Java</a>";
	static final String TEXTBODY = "This email was sent through Amazon SES "
		      + "using the AWS SDK for Java.";
	
	
	/**
	 * method to send an email to the user with a new password in it
	 * @param username
	 * @param password
	 * @param email
	 * @throws MessagingException 
	 * @throws AddressException 
	 */
	public void sendConfirmEmailEmail(String emailAddress,String uniqueId) throws AddressException, MessagingException {
		//successEmailUrl = "http://127.0.0.1:4200";
		String pageLink = successEmailUrl + "/confirmemail";
		EmailTemplate template = new VerifyRegistrationEmailTemplate(emailAddress,username,pageLink,uniqueId,linkExpirationTime);
		createAndSendEmail(template);		
	}

	public void sendForgotPasswordEmail(String emailAddress,String uniqueId) throws AddressException, NoSuchProviderException, SendFailedException, MessagingException {
		//successEmailUrl = "http://127.0.0.1:4200";
		String pageLink = successEmailUrl + "/passwordreset";
		String loginUrl = successEmailUrl + "/login";
		System.out.println(uniqueId);
		EmailTemplate template = new ForgotPasswordEmailTemplate(emailAddress,pageLink,loginUrl,uniqueId,linkExpirationTime);
		createAndSendEmail(template);
	}

	/**
	 * method to send an email to the user with their username in it
	 * @param firstName
	 * @param username
	 * @param email
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void sendEmailWithUsername(String emailAddress,String username) throws AddressException, MessagingException {
		//successEmailUrl = "http://127.0.0.1:4200";
		String loginUrl = successEmailUrl + "/login";
		EmailTemplate template = new ForgotUsernameEmailTemplate(emailAddress, username, loginUrl);
		createAndSendEmail(template);	
	}

	/**
	 * method to send an email to the user informing them that no account was found for this email
	 * this is used for both requesting a password reset or a username reminder
	 * @param emailAddress
	 * @throws MessagingException 
	 * @throws SendFailedException 
	 * @throws NoSuchProviderException 
	 * @throws AddressException 
	 */
	public void sendNoAddressFoundEmail(String emailAddress, boolean reset) throws AddressException, NoSuchProviderException, SendFailedException, MessagingException {
		EmailTemplate template = null;
		if(reset) {
			template = new NoAddressFoundPREmailTemplate(emailAddress);
		} else {
			template = new NoAddressFoundUREmailTemplate(emailAddress);
		}
		createAndSendEmail(template);
	}

	/**
	 * 
	 * @param subject
	 * @param body
	 * @throws AddressException
	 * @throws NoSuchProviderException
	 * @throws SendFailedException
	 * @throws MessagingException
	 */
	public void sendManagementEmail(String subject, String body) throws AddressException, NoSuchProviderException, SendFailedException, MessagingException {
		EmailTemplate template = new ManagementEmailTemplate(subject, body);
		template.setEmailAddress(cc);
		createAndSendEmail(template);	
	}

	private void createAndSendEmail(EmailTemplate template)
			throws AddressException, MessagingException, NoSuchProviderException, SendFailedException {
		Message message = createEmail(template);
		SMTPTransport transport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
		transport.connect(GMAIL_SMTP_SERVER, username, waffle);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}

	/**
	 * method to create the Message email object
	 * @param template
	 * @return
	 * @throws AddressException
	 * @throws MessagingException
	 */
	private Message createEmail(EmailTemplate template) throws AddressException, MessagingException {
		Message message = new MimeMessage(getEmailSession());
		message.setFrom(new InternetAddress(FROM_EMAIL));
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(template.getEmailAddress()));
		/** 
		 * TODO - remove
		 */
		message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(cc,false));
		message.setSubject(template.getSubject());
		Multipart multipart = new MimeMultipart();
		MimeBodyPart messageBodyPart = new MimeBodyPart();       
		messageBodyPart.setContent(template.getBody(), "text/html");
		multipart.addBodyPart(messageBodyPart);
	    message.setContent(multipart,"text/html");
		message.setSentDate(new Date());
		message.saveChanges();
		return message;
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
	
	public void testEmail() {
		logAction("entering testEmail()");
		String emailAddress = "dylanikessler@yahoo.com";
		try {
			sendConfirmEmailEmail(emailAddress,"XXX");
			sendForgotPasswordEmail(emailAddress,"XXX");
			sendEmailWithUsername(emailAddress,"XXX");
			sendNoAddressFoundEmail(emailAddress,true);
			sendNoAddressFoundEmail(emailAddress,false);
			sendManagementEmail("SUBJECT","BODY");
//		Message message = createTestEmail();
//		SMTPTransport transport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
//		transport.connect(GMAIL_SMTP_SERVER, username, waffle);
//		transport.sendMessage(message, message.getAllRecipients());
//		transport.close();	
		logAction("Sent test email to --> " + cc);
		} catch(AddressException ae) {
			logAction("AddressException sending test email --> " + ae.getMessage());
		} catch(MessagingException me) {
			logAction("MessagingException sending test email  --> " + me.getMessage());
		} catch(Exception ee) {
			logAction("General Exception sending test email  --> " + ee.getMessage());
		}
		logAction("leaving testEmail()");
	}
	
//	public void testEmail() {
//		try {
//		      AmazonSimpleEmailService client = 
//		          AmazonSimpleEmailServiceClientBuilder.standard()
//		          // Replace US_WEST_2 with the AWS Region you're using for
//		          // Amazon SES.
//		            .withRegion(Regions.US_EAST_1).build();
//		      SendEmailRequest request = new SendEmailRequest()
//		          .withDestination(
//		              new Destination().withToAddresses(TO))
//		          .withMessage(new com.amazonaws.services.simpleemail.model.Message()
//		              .withBody(new Body()
//		                  .withHtml(new Content()
//		                      .withCharset("UTF-8").withData(HTMLBODY))
//		                  .withText(new Content()
//		                      .withCharset("UTF-8").withData(TEXTBODY)))
//		              .withSubject(new Content()
//		                  .withCharset("UTF-8").withData(SUBJECT)))
//		          .withSource(FROM);
//		          // Comment or remove the next line if you are not using a
//		          // configuration set
//		          //.withConfigurationSetName(CONFIGSET);
//		      client.sendEmail(request);
//		      System.out.println("Email sent!");
//		    } catch (Exception ex) {
//		      System.out.println("The email was not sent. Error message: " 
//		          + ex.getMessage());
//		    }
//	}
	
//	public void testEmail() {
//		String templateName = "interviewmailer76verifyemailtemplate_1";
//		String fromEmailAddress = "interviewmailer76@gmail.com";
//		String templateSubject = "testing our verification email";
//		String templateContent = "<h1>click below to verify email!</h1>";
//		String successUrl = "https://d1fd2sjari3xmm.cloudfront.net/success";
//		String failureUrl = "https://d1fd2sjari3xmm.cloudfront.net/failure";
//		try {
//			SendCustomVerificationEmailRequest request = new SendCustomVerificationEmailRequest()
//					.withEmailAddress(TO)
//					.withTemplateName(templateName);
//			AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
//			          // Replace US_WEST_2 with the AWS Region you're using for
//			          // Amazon SES.
//			            .withRegion(Regions.US_EAST_1).build();
//			client.sendCustomVerificationEmail(request);
//			System.out.println("Check");
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		      System.out.println("The email was not sent. Error message: " 
//			          + ex.getMessage());
//			    }
//	}
	
//	public void testEmail() {
//		String templateName = "interviewmailer76verifyemailtemplate_1";
//		String fromEmailAddress = "interviewmailer76@gmail.com";
//		String templateSubject = "testing our verification email";
//		String templateContent = "<h1>click below to verify email!</h1>";
//		String successUrl = "https://d1fd2sjari3xmm.cloudfront.net/success";
//		String failureUrl = "https://d1fd2sjari3xmm.cloudfront.net/failure";
//		try {
//			CreateCustomVerificationEmailTemplateRequest createTemplate = new CreateCustomVerificationEmailTemplateRequest()
//					.withFromEmailAddress(fromEmailAddress)
//					.withTemplateName(templateName)
//					.withTemplateSubject(templateSubject)
//					.withTemplateContent(templateContent)
//					.withFailureRedirectionURL(failureUrl)
//					.withSuccessRedirectionURL(successUrl);
//			AmazonSimpleEmailService client = 
//			          AmazonSimpleEmailServiceClientBuilder.standard()
//			          // Replace US_WEST_2 with the AWS Region you're using for
//			          // Amazon SES.
//			            .withRegion(Regions.US_EAST_1).build();
//			CreateCustomVerificationEmailTemplateResult result = client.createCustomVerificationEmailTemplate(createTemplate);
//			System.out.println("Check");
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		      System.out.println("The email was not sent. Error message: " 
//			          + ex.getMessage());
//			    }
//	}
	
	/**
	 * 
	 * @param message
	 */
	private static void logAction(String message) {
    	System.out.println(message);
    	applicationLogger.debug(message);
    }
}
