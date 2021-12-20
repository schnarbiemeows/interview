package com.schnarbiesnmeowers.interview.security;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTO;
import com.schnarbiesnmeowers.interview.utilities.HelperUtility;
import com.schnarbiesnmeowers.interview.utilities.Roles;

/**
 * 
 * @author dylan
 *
 */
@RunWith(SpringRunner.class)
public class UserPrincipalTest {

	public static final String A = "a";
	public static final String B = "b";
	public static final int ONE = 1;
	
	@Test
	public void testClass() {
		InterviewUserDTO user = HelperUtility.generateRandomInterviewUserDTO();
		user.setUserActive(true);
		user.setUserNotLocked(true);
		HelperUtility.addRolesAndAuthorizationsToUser(Roles.ROLE_BASIC_USER,user);
		UserPrincipal principal = new UserPrincipal(user);
		assertTrue(null!=principal.getPassword());
		assertTrue(null!=principal.getUsername());
		assertTrue(null!=principal.getAuthorities());
		assertTrue(true==principal.isAccountNonExpired());
		assertTrue(true==principal.isAccountNonLocked());
		assertTrue(true==principal.isCredentialsNonExpired());
		assertTrue(true==principal.isEnabled());
	}
}
