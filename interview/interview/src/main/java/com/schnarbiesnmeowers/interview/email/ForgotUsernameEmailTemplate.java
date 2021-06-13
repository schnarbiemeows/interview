package com.schnarbiesnmeowers.interview.email;

public class ForgotUsernameEmailTemplate extends EmailTemplate {

	private static final String SUBJECT = "Interview Program - Forgot Username Request";
	
	public ForgotUsernameEmailTemplate(String emailAddress, String username,String successUrl) {
		super();
		this.setUsername(username);
		this.setEmailAddress(emailAddress);
		this.setSuccessUrl(successUrl);
		this.setSubject(SUBJECT);
	}

	@Override
	public String getBody() {
		String content = "<!DOCTYPE html>";
		content += "<h1 style=\"text-align:center;\">Hi there!</h1>";
		content += "<p style=\"text-align:center;\">We received a request for the user name for the account</p>";
		content += "<p style=\"text-align:center;\">associated with : " + this.getEmailAddress() + "</p>";
		content += "<p style=\"text-align:center;\">Your user name is : <strong>" + this.getUsername() + "</strong></p>";
		content += "<p>Click here to login:</p>";
		content += "<a href='" + this.getSuccessUrl() + "'>Login</a>";
		content += "<p>If you did not request a password reset, consider logging into our site above and changing your password!</p>";
		content += "<br/>";
		content += "<p>Sincerely:</p><br/>";
		content += "<p>The Schnarbies-n-meowers team</p>";
		content += "</html>";
		return content;
	}


}
