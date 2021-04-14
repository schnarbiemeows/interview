package com.schnarbiesnmeowers.interview.exceptions;

import java.util.Date;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Dylan I. Kessler
 *
 */
public class ExceptionResponse extends ResponseEntityExceptionHandler {

	//private static final Logger applicationLogger = LogManager.getLogger("FileAppender");

	/**
	 * time the exception occurred
	 */
	private Date timestamp;
	/**
	 * exception message
	 */
	private String message;
	/**
	 * detailed message
	 */
	private String details;

	/**
	 *
	 * @param timestamp
	 * @param message
	 * @param details
	 */
	public ExceptionResponse(Date timestamp, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}

	/**
	 *
	 * @return Date
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 *
	 * @return String
	 */
	public String getMessage() {
		return message;
	}

	/**
	 *
	 * @return String
	 */
	public String getDetails() {
		return details;
	}
}
