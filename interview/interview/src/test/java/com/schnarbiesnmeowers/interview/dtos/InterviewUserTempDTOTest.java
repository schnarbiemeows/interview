package com.schnarbiesnmeowers.interview.dtos;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.schnarbiesnmeowers.interview.pojos.InterviewUser;
import com.schnarbiesnmeowers.interview.pojos.InterviewUserTemp;
import com.schnarbiesnmeowers.interview.utilities.Randomizer;

//@RunWith(SpringRunner.class)
public class InterviewUserTempDTOTest {

	/**
	 * test both constructors, getters and setters, and toString() method
	 */
	@Test
	public void testClass() {
		InterviewUserTempDTO classUnderTest = new InterviewUserTempDTO();
		classUnderTest.setUserTempId(new Integer(1));
		classUnderTest.setUniqueId("a");
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
		InterviewUserTempDTO newitem = new InterviewUserTempDTO(
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
		classUnderTest.getUserName(), null);
		assertNotNull(newitem);
		String string = classUnderTest.toString();
		assertNotNull(string);
		assertTrue(string.length()>0);
		InterviewUserTemp interviewUser = classUnderTest.toEntity();
		assertTrue(interviewUser.getUserName().equals(classUnderTest.getUserName()));
		Gson gson = new Gson();
		String userStr = gson.toJson(classUnderTest);
		InterviewUserTempDTO newDTO = InterviewUserTempDTO.fromJson(userStr);
		assertTrue(newDTO.getUserName().equals(classUnderTest.getUserName()));
	}
}
