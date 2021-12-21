package com.schnarbiesnmeowers.interview.dtos;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
public class CheckPasswordResetResponseDTOTest {

	public static final String A = "a";
	
	/**
	 * test both constructors, getters and setters
	 */
	@Test
	public void testClass() {
		CheckPasswordResetResponseDTO dto = new CheckPasswordResetResponseDTO(true, A, A);
		assertTrue(dto.getEmailAddress().equals(A));
		assertTrue(dto.getUniqueId().equals(A));
		assertTrue(true==dto.isFoundRecord());
	}
}
