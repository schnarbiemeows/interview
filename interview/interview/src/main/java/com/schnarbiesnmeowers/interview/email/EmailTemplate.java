package com.schnarbiesnmeowers.interview.email;

public abstract class EmailTemplate {

	private String subject;
	private String emailAddress;
	private String username = null;
	private String uniqueId = null;
	private String successUrl = null;
	private String failureUrl = null;
	private int expirationTime;
	private String header = "";
	private String body = "";
	private String footer = "";
	
	
	public EmailTemplate() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmailTemplate(String emailAddress, String username, String successUrl, String failureUrl, String uniqueId, int expirationTime) {
		this.emailAddress = emailAddress;
		this.username = username;
		this.successUrl = successUrl;
		this.failureUrl = failureUrl;
		this.uniqueId = uniqueId;
		this.expirationTime = expirationTime;
	}
	
	public String getHeader() {
		return header;
	}
	
	public abstract String getBody();
	
	public String getFooter() {
		return footer;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getFailureUrl() {
		return failureUrl;
	}

	public void setFailureUrl(String failureUrl) {
		this.failureUrl = failureUrl;
	}

	public int getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(int expirationTime) {
		this.expirationTime = expirationTime;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}
	
	
}
