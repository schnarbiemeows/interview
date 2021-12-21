package com.schnarbiesnmeowers.interview.dtos;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.schnarbiesnmeowers.interview.pojos.InterviewUser;

//@RunWith(SpringRunner.class)
public class InterviewUserDTOWrapperTest {

	public static final String A = "a";
	public static final int ONE = 1;
	
	/**
	 * test both constructors, getters and setters
	 */
	@Test
	public void testClass() {
		// first constructor
		InterviewUserDTOWrapper dto1 = new InterviewUserDTOWrapper();
		dto1.setAuthorizations(null);
		dto1.setEmailAddr(A);
		dto1.setFirstName(A);
		Date today = new Date();
		dto1.setJoinDate(today);
		dto1.setLastLoginDate(today);
		dto1.setLastLoginDateDisplay(today);
		dto1.setLastName(A);
		dto1.setNewEmailAddr(A);
		dto1.setNewFirstName(A);
		dto1.setNewLastName(A);
		dto1.setNewPassword(A);
		dto1.setNewUserName(A);
		dto1.setPassword(A);
		dto1.setProfileImage(A);
		dto1.setRoles(A);
		dto1.setUserActive(true);
		dto1.setUserId(ONE);
		dto1.setUserIdentifier(A);
		dto1.setUserName(A);
		dto1.setUserNotLocked(true);
		assertTrue(null==dto1.getAuthorizations());
		assertTrue(dto1.getEmailAddr().equals(A));
		assertTrue(dto1.getFirstName().equals(A));
		assertTrue(dto1.getJoinDate().compareTo(today)==0);
		assertTrue(dto1.getLastLoginDate().compareTo(today)==0);
		assertTrue(dto1.getLastLoginDateDisplay().compareTo(today)==0);
		assertTrue(dto1.getLastName().equals(A));
		assertTrue(dto1.getNewEmailAddr().equals(A));
		assertTrue(dto1.getNewFirstName().equals(A));
		assertTrue(dto1.getNewLastName().equals(A));
		assertTrue(dto1.getNewPassword().equals(A));
		assertTrue(dto1.getNewUserName().equals(A));
		assertTrue(dto1.getPassword().equals(A));
		assertTrue(dto1.getProfileImage().equals(A));
		assertTrue(dto1.getRoles().equals(A));
		assertTrue(dto1.isUserActive()==true);
		assertTrue(dto1.isUserNotLocked()==true);
		assertTrue(dto1.getUserIdentifier().equals(A));
		assertTrue(dto1.getUserName().equals(A));
		// second constructor
		InterviewUserDTO dto2 = new InterviewUserDTO();
		dto2.setAuthorizations(null);
		dto2.setEmailAddr(A);
		dto2.setFirstName(A);
		today = new Date();
		dto2.setJoinDate(today);
		dto2.setLastLoginDate(today);
		dto2.setLastLoginDateDisplay(today);
		dto2.setLastName(A);
		dto2.setPassword(A);
		dto2.setProfileImage(A);
		dto2.setRoles(A);
		dto2.setUserActive(true);
		dto2.setUserId(ONE);
		dto2.setUserIdentifier(A);
		dto2.setUserName(A);
		dto2.setUserNotLocked(true);
		InterviewUserDTOWrapper dto3 = new InterviewUserDTOWrapper(dto2);
		dto3.setNewEmailAddr(A);
		dto3.setNewFirstName(A);
		dto3.setNewLastName(A);
		dto3.setNewPassword(A);
		dto3.setNewUserName(A);
		assertTrue(null==dto3.getAuthorizations());
		assertTrue(dto3.getEmailAddr().equals(A));
		assertTrue(dto3.getFirstName().equals(A));
		assertTrue(dto3.getJoinDate().compareTo(today)==0);
		assertTrue(dto3.getLastLoginDate().compareTo(today)==0);
		assertTrue(dto3.getLastLoginDateDisplay().compareTo(today)==0);
		assertTrue(dto3.getLastName().equals(A));
		assertTrue(dto3.getNewEmailAddr().equals(A));
		assertTrue(dto3.getNewFirstName().equals(A));
		assertTrue(dto3.getNewLastName().equals(A));
		assertTrue(dto3.getNewPassword().equals(A));
		assertTrue(dto3.getNewUserName().equals(A));
		assertTrue(dto3.getPassword().equals(A));
		assertTrue(dto3.getProfileImage().equals(A));
		assertTrue(dto3.getRoles().equals(A));
		assertTrue(dto3.isUserActive()==true);
		assertTrue(dto3.getUserIdentifier().equals(A));
		assertTrue(dto3.getUserName().equals(A));
		assertTrue(dto3.isUserNotLocked()==true);
		// third constructor
		InterviewUserDTOWrapper dto4 = new InterviewUserDTOWrapper(1,null,A,A,true,true,today,today,today,A,A,A,A,A,A);
		assertTrue(null==dto4.getAuthorizations());
		assertTrue(dto4.getEmailAddr().equals(A));
		assertTrue(dto4.getFirstName().equals(A));
		assertTrue(dto4.getJoinDate().compareTo(today)==0);
		assertTrue(dto4.getLastLoginDate().compareTo(today)==0);
		assertTrue(dto4.getLastLoginDateDisplay().compareTo(today)==0);
		assertTrue(dto4.getLastName().equals(A));
		assertTrue(dto4.getPassword().equals(A));
		assertTrue(dto4.getProfileImage().equals(A));
		assertTrue(dto4.getRoles().equals(A));
		assertTrue(dto4.isUserActive()==true);
		assertTrue(dto4.isUserNotLocked()==true);
		assertTrue(dto4.getUserIdentifier().equals(A));
		assertTrue(dto4.getUserName().equals(A));
	}
}
