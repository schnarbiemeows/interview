package com.schnarbiesnmeowers.interview.exceptions.interviewuser;

/**
 * when the user tries to change their password, but their old password is
 * incorrect, this is the exception that gets thrown
 * @author dylan
 *
 */
public class PasswordIncorrectException extends Exception {

	/**
	 * 
	 * @param message
	 */
	public PasswordIncorrectException(String message) {
		super(message);
	}
}
