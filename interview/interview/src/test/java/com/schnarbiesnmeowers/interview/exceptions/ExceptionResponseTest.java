package com.schnarbiesnmeowers.interview.exceptions;

import static org.junit.Assert.*;
import java.util.Date;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * class to test the ExceptionResponse class
 * @author Dylan I. Kessler
 *
 */
//@RunWith(SpringRunner.class)
public class ExceptionResponseTest {

	/**
	 * test the constructor and the getters
	 */
	@Test
	public void testClass() {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "message", "details");
		assertNotNull(exceptionResponse.getTimestamp());
		assertNotNull(exceptionResponse.getMessage());
		assertNotNull(exceptionResponse.getDetails());
	}
}