package com.schnarbiesnmeowers.interview.dtos;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
public class PasswordResetDTOTest {

	public static final String A = "a";
	
	/**
	 * test both constructors, getters and setters, and toString() method
	 */
	@Test
	public void testClass() {
		PasswordResetDTO dto1 = new PasswordResetDTO();
		dto1.setEmailAddress(A);
		dto1.setPassword(A);
		dto1.setUniqueId(A);
		assertTrue(dto1.getEmailAddress().equals(A));
		assertTrue(dto1.getPassword().equals(A));
		assertTrue(dto1.getUniqueId().equals(A));
		PasswordResetDTO dto2 = new PasswordResetDTO(A,A,A);
		assertTrue(dto2.getEmailAddress().equals(A));
		assertTrue(dto2.getPassword().equals(A));
		assertTrue(dto2.getUniqueId().equals(A));
	}
}
