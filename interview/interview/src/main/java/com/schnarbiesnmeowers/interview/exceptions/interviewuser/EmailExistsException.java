package com.schnarbiesnmeowers.interview.exceptions.interviewuser;

/**
 * 
 * @author Dylan I. Kessler
 *
 */
public class EmailExistsException extends Exception {

	/**
	 * 
	 * @param message
	 */
	public EmailExistsException(String message) {
		super(message);
	}
}
