package com.schnarbiesnmeowers.interview.pojos;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
public class ResponseMessageTest {

	public static final String A = "a";
	
	/**
	 * test both constructors, getters and setters, and toString() method
	 */
	@Test
	public void testClass() {
		ResponseMessage pojo1 = new ResponseMessage();
		pojo1.setMessage(A);
		assertTrue(pojo1.getMessage().equals(A));
		ResponseMessage pojo2 = new ResponseMessage(A);
		assertTrue(pojo2.getMessage().equals(A));
	}
}
