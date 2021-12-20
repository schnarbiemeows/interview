package com.schnarbiesnmeowers.interview.email;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ForgotUsernameEmailTemplateTest {

	public static final String A = "a";
	/**
	 * test both constructors, getters and setters, and toString() method
	 */
	@Test
	public void testClass() {
		ForgotUsernameEmailTemplate template = new ForgotUsernameEmailTemplate(A,A,A);
		template.setExpirationTime(0);
		template.setFailureUrl(A);
		template.setFooter(A);
		template.setHeader(A);
		template.setFooter(A);
		template.setHeader(A);
		template.setUsername(A);
		template.setUniqueId(A);
		assertTrue(null!=template.getBody());
		assertTrue(0==template.getExpirationTime());
		assertTrue(A.equals(template.getEmailAddress()));
		assertTrue(0==template.getExpirationTime());
		assertTrue(A.equals(template.getFailureUrl()));
		assertTrue(A.equals(template.getFooter()));
		assertTrue(A.equals(template.getHeader()));
		assertTrue(A.equals(template.getSuccessUrl()));
		assertTrue(A.equals(template.getUniqueId()));
		assertTrue(A.equals(template.getUsername()));
	}
}
