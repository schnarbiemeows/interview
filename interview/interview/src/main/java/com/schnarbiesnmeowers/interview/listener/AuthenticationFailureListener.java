package com.schnarbiesnmeowers.interview.listener;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import com.schnarbiesnmeowers.interview.services.impl.LoginAttemptService;

/**
 * 
 * @author dylan
 *
 */
@Component
public class AuthenticationFailureListener {

	private LoginAttemptService loginAttemptService;

	@Autowired
	public AuthenticationFailureListener(LoginAttemptService loginAttemptService) {
		super();
		this.loginAttemptService = loginAttemptService;
	}
	
	@EventListener
	public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
		Object principal = event.getAuthentication().getPrincipal();
		if(principal instanceof String) {
			String username = (String) event.getAuthentication().getPrincipal();
			this.loginAttemptService.addUserToLoginCache(username);
		}
	}
	
}
