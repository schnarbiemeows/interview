package com.schnarbiesnmeowers.interview.email;

public class ForgotPasswordEmailTemplate extends EmailTemplate {

	private static final String SUBJECT = "Interview Program - Password Reset";
	
	public ForgotPasswordEmailTemplate(String emailAddress, String successUrl, String failureUrl, String uniqueId,
			int expirationTime) {
		super();
		this.setEmailAddress(emailAddress);
		this.setSuccessUrl(successUrl);
		this.setFailureUrl(failureUrl);
		this.setUniqueId(uniqueId);
		this.setExpirationTime(expirationTime);
		this.setSubject(SUBJECT);
	}

	@Override
	public String getBody() {
		String content = "<!DOCTYPE html>";
		content += "<h1 style=\"text-align:center;\">Hi there!</h1>";
		content += "<p style=\"text-align:center;\">We received a request to reset the password for the account</p>";
		content += "<p style=\"text-align:center;\">associated with : " + this.getEmailAddress() + "</p>";
		content += "<p style=\"text-align:center;\">No changes have been made to your account yet</p>";
		content += "<p>click the following link below to reset your password:</p>";
		content += "<a href='" + this.getSuccessUrl() + "/" + this.getUniqueId() + "'>Please click here to reset your password...</a>";
		content += "<p>Note: this link will expire in " + this.getExpirationTime() + " minutes</p>";
		content += "<p>If you did not request a password reset, consider logging into our site below and changing your password!</p>";
//		content += "<a href='" + this.getFailureUrl() + "/" + this.getUniqueId() + "'>Please click here to change your password...</a>";
		content += "<a href='" + this.getFailureUrl() + "'>Please click here to Login...</a>";
		content += "<br/>";
		content += "<p>Sincerely:</p><br/>";
		content += "<p>The Schnarbies-n-meowers team</p>";
		content += "</html>";
		return content;
	}

}
