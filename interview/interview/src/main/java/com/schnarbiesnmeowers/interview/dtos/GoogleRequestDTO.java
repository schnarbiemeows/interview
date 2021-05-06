package com.schnarbiesnmeowers.interview.dtos;

import java.io.Serializable;

/**
 * class for transmitting the backend validation response to google
 * @author Dylan I. Kessler
 *
 */
public class GoogleRequestDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private String secret;
	
	/**
	 * 
	 */
	private String response;

	public GoogleRequestDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param secret
	 * @param response
	 */
	public GoogleRequestDTO(String secret, String response) {
		super();
		this.secret = secret;
		this.response = response;
	}

	/**
	 * @return the secret
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * @param secret the secret to set
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * @return the response
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(String response) {
		this.response = response;
	}
	
	
	
}
