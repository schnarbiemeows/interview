package com.schnarbiesnmeowers.interview.dtos;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.schnarbiesnmeowers.interview.dtos.GoogleResponseDTO.ErrorCode;

//@RunWith(SpringRunner.class)
public class GoogleResponseDTOTest {

	public static final String A = "a";
	
	/**
	 * test both constructors, getters and setters
	 */
	@Test
	public void testClass() {
		GoogleResponseDTO dto = new GoogleResponseDTO();
		dto.setSuccess(true);
		dto.setHostname(A);
		dto.setErrorCodes(null);
		dto.setChallengeTs(A);
		assertTrue(false==dto.hasClientError());
		assertTrue(true==dto.isSuccess());
		assertTrue(dto.getHostname().equals(A));
		assertTrue(dto.getChallengeTs().equals(A));
		assertTrue(null==dto.getErrorCodes());
		ErrorCode[] errorCodes = new ErrorCode[1];
		errorCodes[0] = GoogleResponseDTO.ErrorCode.MissingResponse;
		dto.setErrorCodes(errorCodes);
		assertTrue(true==dto.hasClientError());
	}
}
