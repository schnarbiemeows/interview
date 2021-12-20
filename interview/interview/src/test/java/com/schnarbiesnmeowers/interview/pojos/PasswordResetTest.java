package com.schnarbiesnmeowers.interview.pojos;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class PasswordResetTest {

	public static final String A = "a";
	public static final int ONE = 1;
	
	/**
	 * test both constructors, getters and setters, and toString() method
	 */
	@Test
	public void testClass() {
		PasswordReset pojo1 = new PasswordReset();
		Date today = new Date();
		pojo1.setCreatedDate(today);
		pojo1.setEmailAddr(A);
		pojo1.setPasswordResetId(ONE);
		pojo1.setUniqueId(A);
		assertTrue(pojo1.getEmailAddr().equals(A));
		assertTrue(pojo1.getUniqueId().equals(A));
		assertTrue(pojo1.getCreatedDate().compareTo(today)==0);
		assertTrue(pojo1.getPasswordResetId()==ONE);
		PasswordReset pojo2 = new PasswordReset(ONE,A,A,today);
		assertTrue(pojo2.getEmailAddr().equals(A));
		assertTrue(pojo2.getUniqueId().equals(A));
		assertTrue(pojo2.getCreatedDate().compareTo(today)==0);
		assertTrue(pojo2.getPasswordResetId()==ONE);
	}
}
