package com.schnarbiesnmeowers.interview.dtos;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
public class GoogleRequestDTOTest {

	public static final String A = "a";
	
	/**
	 * test both constructors, getters and setters
	 */
	@Test
	public void testClass() {
		GoogleRequestDTO dto1 = new GoogleRequestDTO();
		dto1.setResponse(A);
		dto1.setSecret(A);
		assertTrue(dto1.getResponse().equals(A));
		assertTrue(dto1.getSecret().equals(A));
		GoogleRequestDTO dto2 = new GoogleRequestDTO(A,A);
		assertTrue(dto2.getResponse().equals(A));
		assertTrue(dto2.getSecret().equals(A));
	}
}
