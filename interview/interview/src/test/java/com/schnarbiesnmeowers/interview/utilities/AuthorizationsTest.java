package com.schnarbiesnmeowers.interview.utilities;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
public class AuthorizationsTest {

	@Test
	public void testMethods() {
		assertTrue(null!=Authorizations.ADMIN_AUTH);
		assertTrue(null!=Authorizations.ADV_USER_AUTH);
		assertTrue(null!=Authorizations.BASIC_USER_AUTH);
		assertTrue(null!=Authorizations.PREMIUM_USER_AUTH);
		assertTrue(null!=Authorizations.SUPER_ADMIN_AUTH);
		
		assertTrue(12==Authorizations.ADMIN_AUTH.length);
		assertTrue(3==Authorizations.ADV_USER_AUTH.length);
		assertTrue(2==Authorizations.BASIC_USER_AUTH.length);
		assertTrue(4==Authorizations.PREMIUM_USER_AUTH.length);
		assertTrue(15==Authorizations.SUPER_ADMIN_AUTH.length);
		
	}
}
