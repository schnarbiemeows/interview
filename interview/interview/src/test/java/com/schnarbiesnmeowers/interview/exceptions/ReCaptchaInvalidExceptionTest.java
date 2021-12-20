package com.schnarbiesnmeowers.interview.exceptions;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ReCaptchaInvalidExceptionTest {

	public static final String A = "a";
	/**
	 * test both constructors, getters and setters, and toString() method
	 */
	@Test
	public void testClass() {
		ResourceNotFoundException test = new ResourceNotFoundException(A);
		assertTrue(A.equals(test.getMessage()));
	}
}
