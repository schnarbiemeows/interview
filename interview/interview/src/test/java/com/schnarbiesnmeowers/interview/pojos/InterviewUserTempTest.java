package com.schnarbiesnmeowers.interview.pojos;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.schnarbiesnmeowers.interview.utilities.Randomizer;

@RunWith(SpringRunner.class)
public class InterviewUserTempTest {

	public static final String A = "a";
	public static final int ONE = 1;
	
	/**
	 * test both constructors, getters and setters, and toString() method
	 */
	@Test
	public void testClass() {
		InterviewUserTemp classUnderTest = new InterviewUserTemp();
		classUnderTest.setUserTempId(ONE);
		classUnderTest.setUniqueId(A);
		String[] stringarray = new String[1];
		stringarray[0] = Randomizer.randomString(3);
		classUnderTest.setAuthorizations(stringarray);
		classUnderTest.setEmailAddr(A);
		classUnderTest.setFirstName(A);
		classUnderTest.setUserActive(true);
		classUnderTest.setUserNotLocked(true);
		Date today = new Date();
		classUnderTest.setJoinDate(today);
		classUnderTest.setLastLoginDate(today);
		classUnderTest.setLastLoginDateDisplay(today);
		classUnderTest.setLastName(A);
		classUnderTest.setPassword(A);
		classUnderTest.setProfileImage(A);
		classUnderTest.setRoles(A);
		classUnderTest.setUserIdentifier(A);
		classUnderTest.setUserName(A);
		classUnderTest.setCreatedDate(today);
		InterviewUserTemp newitem = new InterviewUserTemp(
		classUnderTest.getUserTempId(),
		classUnderTest.getUniqueId(),
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
		classUnderTest.getUserName(),
		classUnderTest.getCreatedDate());
		assertTrue(newitem.getUserTempId()==classUnderTest.getUserTempId());
		assertTrue(newitem.getUniqueId().equals(classUnderTest.getUniqueId()));
		assertTrue(newitem.getAuthorizations().toString().equals(classUnderTest.getAuthorizations().toString()));
		assertTrue(newitem.getEmailAddr().equals(classUnderTest.getEmailAddr()));
		assertTrue(newitem.getFirstName().equals(classUnderTest.getFirstName()));
		assertTrue(newitem.isUserActive()==classUnderTest.isUserActive());
		assertTrue(newitem.isUserNotLocked()==classUnderTest.isUserNotLocked());
		assertTrue(newitem.getJoinDate().compareTo(classUnderTest.getJoinDate())==0);
		assertTrue(newitem.getLastLoginDate().compareTo(classUnderTest.getLastLoginDate())==0);
		assertTrue(newitem.getLastLoginDateDisplay().compareTo(classUnderTest.getLastLoginDateDisplay())==0);
		assertTrue(newitem.getLastName().equals(classUnderTest.getLastName()));
		assertTrue(newitem.getPassword().equals(classUnderTest.getPassword()));
		assertTrue(newitem.getProfileImage().equals(classUnderTest.getProfileImage()));
		assertTrue(newitem.getRoles().equals(classUnderTest.getRoles()));
		assertTrue(newitem.getUserIdentifier().equals(classUnderTest.getUserIdentifier()));
		assertTrue(newitem.getUserName().equals(classUnderTest.getUserName()));
		assertTrue(newitem.getCreatedDate().compareTo(classUnderTest.getCreatedDate())==0);
		String string = classUnderTest.toString();
		assertNotNull(string);
		assertTrue(string.length()>0);
	}
}
