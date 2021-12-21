package com.schnarbiesnmeowers.interview.services.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
public class LoginAttemptServiceTest {

	public static final String TEST_USER = "testuser";
	
	LoginAttemptService loginAttemptService = new LoginAttemptService();
	
	@Test
	public void testEvictUserFromLoginCache() {
		LoginAttemptService loginAttemptService = new LoginAttemptService();
		loginAttemptService.evictUserFromLoginCache(TEST_USER);
		assertTrue(true);
	}
	
	@Test
	public void testAddUserToLoginCache() {
		LoginAttemptService loginAttemptService = new LoginAttemptService();
		loginAttemptService.addUserToLoginCache(TEST_USER);
		assertTrue(true);
	}
	
	@Test
	public void testHasExceededMaxAttempts() {
		LoginAttemptService loginAttemptService = new LoginAttemptService();
		boolean hasExceeded = loginAttemptService.hasExceededMaxAttempts(TEST_USER);
		assertTrue(hasExceeded==false);
		loginAttemptService.addUserToLoginCache(TEST_USER);
		loginAttemptService.addUserToLoginCache(TEST_USER);
		loginAttemptService.addUserToLoginCache(TEST_USER);
		loginAttemptService.addUserToLoginCache(TEST_USER);
		hasExceeded = loginAttemptService.hasExceededMaxAttempts(TEST_USER);
		assertTrue(hasExceeded==false);
		loginAttemptService.addUserToLoginCache(TEST_USER);
		hasExceeded = loginAttemptService.hasExceededMaxAttempts(TEST_USER);
		assertTrue(hasExceeded==true);
	}
}
