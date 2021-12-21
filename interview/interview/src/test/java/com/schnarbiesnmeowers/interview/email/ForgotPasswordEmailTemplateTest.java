package com.schnarbiesnmeowers.interview.email;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
public class ForgotPasswordEmailTemplateTest {

	public static final String A = "a";
	
	/**
	 * test both constructors, getters and setters, and toString() method
	 */
	@Test
	public void testClass() {
		ForgotPasswordEmailTemplate template = new ForgotPasswordEmailTemplate(A,A,A,A,0);
		template.setFooter(A);
		template.setHeader(A);
		template.setUsername(A);
		assertTrue(null!=template.getBody());
		assertTrue(A.equals(template.getEmailAddress()));
		assertTrue(0==template.getExpirationTime());
		assertTrue(A.equals(template.getFailureUrl()));
		assertTrue(A.equals(template.getFooter()));
		assertTrue(A.equals(template.getHeader()));
		assertTrue(null!=template.getSubject());
		assertTrue(A.equals(template.getSuccessUrl()));
		assertTrue(A.equals(template.getUniqueId()));
		assertTrue(A.equals(template.getUsername()));
	}
}
