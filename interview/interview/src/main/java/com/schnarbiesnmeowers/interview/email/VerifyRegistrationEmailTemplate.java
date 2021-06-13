package com.schnarbiesnmeowers.interview.email;

public class VerifyRegistrationEmailTemplate extends EmailTemplate {
	
	private static final String SUBJECT = "Interview Program - Confirm Email";
	
	public VerifyRegistrationEmailTemplate(String emailAddress, String username, String successUrl, String uniqueId, int expirationTime) {
		super();
		this.setEmailAddress(emailAddress);
		this.setUsername(username);
		this.setSuccessUrl(successUrl);
		this.setUniqueId(uniqueId);
		this.setExpirationTime(expirationTime);
		this.setSubject(SUBJECT);
	}

	@Override
	public String getBody() {
		String content = "<!DOCTYPE html>";
		content += "<h1 style=\"text-align:center;\">Welcome Aboard " + this.getUsername() + "!</h1>";
		content += "<p style=\"text-align:center;\">We are glad that you have chosen to register with us.</p>";
		content += "<p style=\"text-align:center;\">We hope that our site can help you become a better coder!</p>";
		content += "<p>click the following link below to confirm your email address:</p>";
		content += "<a href='" + this.getSuccessUrl() + "/" + this.getUniqueId() + "'>Please click here to confirm your email address...</a>";
		content += "<p>Note: this link will expire in " + this.getExpirationTime() + " minutes</p>";
		content += "<br/>";
		content += "<p>Sincerely:</p><br/>";
		content += "<p>The Schnarbies-n-meowers team</p>";
		content += "</html>";
		return content;
	}

}
