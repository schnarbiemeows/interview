package com.schnarbiesnmeowers.interview.utilities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
public class RolesTest {

	@Test
	public void test() {
		Roles testRoles = Roles.ROLE_BASIC_USER;
		String[] auths = testRoles.getAuthorizations();
		assertEquals(auths.length,2);
		testRoles = Roles.ROLE_ADV_USER;
		auths = testRoles.getAuthorizations();
		assertEquals(auths.length,3);
		testRoles = Roles.ROLE_PREMIUM_USER;
		auths = testRoles.getAuthorizations();
		assertEquals(auths.length,4);
		testRoles = Roles.ROLE_ADMIN;
		auths = testRoles.getAuthorizations();
		assertEquals(auths.length,12);
		testRoles = Roles.ROLE_SUPER;
		auths = testRoles.getAuthorizations();
		assertEquals(auths.length,15);
	}
}
