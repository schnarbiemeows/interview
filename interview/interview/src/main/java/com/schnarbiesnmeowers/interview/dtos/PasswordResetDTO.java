package com.schnarbiesnmeowers.interview.dtos;

import java.io.Serializable;

public class PasswordResetDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String password;
	
	private String uniqueId;
	
	private String emailAddress;

	public PasswordResetDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PasswordResetDTO(String password, String uniqueId, String emailAddress) {
		super();
		this.password = password;
		this.uniqueId = uniqueId;
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
}
