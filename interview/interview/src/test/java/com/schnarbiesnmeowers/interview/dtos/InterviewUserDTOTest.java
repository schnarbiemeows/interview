package com.schnarbiesnmeowers.interview.dtos;

import com.schnarbiesnmeowers.interview.utilities.Randomizer;
import static org.junit.Assert.*;
import java.util.*;
import java.sql.Timestamp;
import org.junit.Test;
import java.math.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * class to test the InterviewUserDTO class
 * @author Dylan I. Kessler
 *
 */
@RunWith(SpringRunner.class)
public class InterviewUserDTOTest {

	/**
	 * test both constructors, getters and setters, and toString() method
	 */
	@Test
	public void testClass() {
		InterviewUserDTO classUnderTest = new InterviewUserDTO();
		classUnderTest.setUserId(new Integer(1));
		String[] stringarray = new String[1];
		stringarray[0] = Randomizer.randomString(3);
		classUnderTest.setAuthorizations(stringarray);
		classUnderTest.setEmailaddr("a");
		classUnderTest.setFirstname("a");
		classUnderTest.setIsuseractive(true);
		classUnderTest.setIsusernotlocked(true);
		classUnderTest.setJoindate(new Date());
		classUnderTest.setLastlogindate(new Date());
		classUnderTest.setLastlogindatedisplay(new Date());
		classUnderTest.setLastname("a");
		classUnderTest.setPassword("a");
		classUnderTest.setProfileimage("a");
		classUnderTest.setRoles("a");
		classUnderTest.setUseridentifier("a");
		classUnderTest.setUsername("a");
		assertTrue(true);
		InterviewUserDTO newitem = new InterviewUserDTO(
		classUnderTest.getUserId(),
		classUnderTest.getAuthorizations(),
		classUnderTest.getEmailaddr(),
		classUnderTest.getFirstname(),
		classUnderTest.getIsuseractive(),
		classUnderTest.getIsusernotlocked(),
		classUnderTest.getJoindate(),
		classUnderTest.getLastlogindate(),
		classUnderTest.getLastlogindatedisplay(),
		classUnderTest.getLastname(),
		classUnderTest.getPassword(),
		classUnderTest.getProfileimage(),
		classUnderTest.getRoles(),
		classUnderTest.getUseridentifier(),
		classUnderTest.getUsername());
		assertNotNull(newitem);
		String string = classUnderTest.toString();
		assertNotNull(string);
		assertTrue(string.length()>0);
	}

}