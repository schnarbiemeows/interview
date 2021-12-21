package com.schnarbiesnmeowers.interview.email;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
public class VerifyRegistrationEmailTemplateTest {

	public static final String A = "a";
	
	/**
	 * test both constructors, getters and setters, and toString() method
	 */
	@Test
	public void testClass() {
		VerifyRegistrationEmailTemplate template = new VerifyRegistrationEmailTemplate(null, null, null, null, 0);
		template.setBody(A);
		template.setEmailAddress(A);
		template.setExpirationTime(0);
		template.setFailureUrl(A);
		template.setFooter(A);
		template.setHeader(A);
		template.setSubject(A);
		template.setSuccessUrl(A);
		template.setUniqueId(A);
		template.setUsername(A);
		assertTrue(A.equals(template.getEmailAddress()));
		assertTrue(0==template.getExpirationTime());
		assertTrue(A.equals(template.getFailureUrl()));
		assertTrue(A.equals(template.getFooter()));
		assertTrue(A.equals(template.getHeader()));
		assertTrue(A.equals(template.getSubject()));
		assertTrue(A.equals(template.getSuccessUrl()));
		assertTrue(A.equals(template.getUniqueId()));
		assertTrue(A.equals(template.getUsername()));
	}
}
