package com.schnarbiesnmeowers.interview.dtos;

import java.io.Serializable;

public class CheckPasswordResetResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * was a record found
	 */
	private boolean foundRecord;
	
	/**
	 * address associated with the record
	 */
	private String emailAddress;
	
	private String uniqueId;
	
	public CheckPasswordResetResponseDTO(boolean foundRecord, String emailAddress, String uniqueId) {
		super();
		this.foundRecord = foundRecord;
		this.emailAddress = emailAddress;
		this.uniqueId = uniqueId;
	}

	public boolean isFoundRecord() {
		return foundRecord;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	
}
