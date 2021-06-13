package com.schnarbiesnmeowers.interview.email;

public class ManagementEmailTemplate extends EmailTemplate {

	private String message ;
	
	private static final String SUBJECT = "";
	
	public ManagementEmailTemplate(String subject, String body) {
		super();
		this.message = body;
		this.setSubject(subject);
	}
	
	@Override
	public String getBody() {
		return message;
	}

}
