package com.schnarbiesnmeowers.interview.email;

public class NoAddressFoundUREmailTemplate extends EmailTemplate {

	private static final String SUBJECT = "Interview Program - Forgot Username Request";
	
	public NoAddressFoundUREmailTemplate(String emailAddress) {
		super();
		this.setEmailAddress(emailAddress);
		this.setSubject(SUBJECT);
	}

	@Override
	public String getBody() {
		String content = "<!DOCTYPE html>";
		content += "<h1 style=\"text-align:center;\">Hi there!</h1>";
		content += "<p style=\"text-align:center;\">We received a request for the user name for the account</p>";
		content += "<p style=\"text-align:center;\">associated with : " + this.getEmailAddress() + " ,</p>";
		content += "<p>However, we do not have any account associated with that address</p>";
		content += "<p>Perhaps you registered an account with a different address? </p>";
		content += "<br/>";
		content += "<p>Sincerely:</p><br/>";
		content += "<p>The Schnarbies-n-meowers team</p>";
		content += "</html>";
		return content;
	}

}
