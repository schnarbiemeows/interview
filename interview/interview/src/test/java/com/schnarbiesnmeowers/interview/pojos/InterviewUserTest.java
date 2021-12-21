package com.schnarbiesnmeowers.interview.pojos;

import com.schnarbiesnmeowers.interview.utilities.Randomizer;
import static org.junit.Assert.*;
import java.util.*;
import java.sql.Timestamp;
import org.junit.Test;
import java.math.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * class to test the InterviewUser class
 * @author Dylan I. Kessler
 *
 */
//@RunWith(SpringRunner.class)
public class InterviewUserTest {

	/**
	 * test both constructors, getters and setters, and toString() method
	 */
	@Test
	public void testClass() {
		InterviewUser classUnderTest = new InterviewUser();
		classUnderTest.setUserId(new Integer(1));
		String[] stringarray = new String[1];
		stringarray[0] = Randomizer.randomString(3);
		classUnderTest.setAuthorizations(stringarray);
		classUnderTest.setEmailAddr("a");
		classUnderTest.setFirstName("a");
		classUnderTest.setUserActive(true);
		classUnderTest.setUserNotLocked(true);
		classUnderTest.setJoinDate(new Date());
		classUnderTest.setLastLoginDate(new Date());
		classUnderTest.setLastLoginDateDisplay(new Date());
		classUnderTest.setLastName("a");
		classUnderTest.setPassword("a");
		classUnderTest.setProfileImage("a");
		classUnderTest.setRoles("a");
		classUnderTest.setUserIdentifier("a");
		classUnderTest.setUserName("a");
		assertTrue(true);
		InterviewUser newitem = new InterviewUser(
		classUnderTest.getUserId(),
		classUnderTest.getAuthorizations(),
		classUnderTest.getEmailAddr(),
		classUnderTest.getFirstName(),
		classUnderTest.isUserActive(),
		classUnderTest.isUserNotLocked(),
		classUnderTest.getJoinDate(),
		classUnderTest.getLastLoginDate(),
		classUnderTest.getLastLoginDateDisplay(),
		classUnderTest.getLastName(),
		classUnderTest.getPassword(),
		classUnderTest.getProfileImage(),
		classUnderTest.getRoles(),
		classUnderTest.getUserIdentifier(),
		classUnderTest.getUserName());
		assertNotNull(newitem);
		String string = classUnderTest.toString();
		assertNotNull(string);
		assertTrue(string.length()>0);
	}

}