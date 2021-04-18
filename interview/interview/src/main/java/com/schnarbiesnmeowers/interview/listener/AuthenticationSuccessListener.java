package com.schnarbiesnmeowers.interview.listener;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import com.schnarbiesnmeowers.interview.security.UserPrincipal;
import com.schnarbiesnmeowers.interview.services.impl.LoginAttemptService;

/**
 * 
 * @author dylan
 *
 */
@Component
public class AuthenticationSuccessListener {

	private LoginAttemptService loginAttemptService;

	@Autowired
	public AuthenticationSuccessListener(LoginAttemptService loginAttemptService) {
		super();
		this.loginAttemptService = loginAttemptService;
	}
	
	@EventListener
	public void onAuthenticationSuccess(AuthenticationSuccessEvent event) throws ExecutionException {
		Object principal = event.getAuthentication().getPrincipal();
		if(principal instanceof UserPrincipal) {
			UserPrincipal user = (UserPrincipal) event.getAuthentication().getPrincipal();
			this.loginAttemptService.evictUserFromLoginCache(user.getUsername());
		}
	}
}
