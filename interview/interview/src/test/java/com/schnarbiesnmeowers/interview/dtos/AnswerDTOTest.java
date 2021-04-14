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
 * class to test the AnswerDTO class
 * @author Dylan I. Kessler
 *
 */
@RunWith(SpringRunner.class)
public class AnswerDTOTest {

	/**
	 * test both constructors, getters and setters, and toString() method
	 */
	@Test
	public void testClass() {
		AnswerDTO classUnderTest = new AnswerDTO();
		classUnderTest.setAnswerId(new Integer(1));
		classUnderTest.setAnswerTxt("a");
		classUnderTest.setEvntTmestmp(new Date());
		classUnderTest.setEvntOperId("a");
		assertTrue(true);
		AnswerDTO newitem = new AnswerDTO(
		classUnderTest.getAnswerId(),
		classUnderTest.getAnswerTxt(),
		classUnderTest.getEvntTmestmp(),
		classUnderTest.getEvntOperId());
		assertNotNull(newitem);
		String string = classUnderTest.toString();
		assertNotNull(string);
		assertTrue(string.length()>0);
	}

}