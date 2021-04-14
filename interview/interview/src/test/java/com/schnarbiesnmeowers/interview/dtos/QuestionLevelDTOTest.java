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
 * class to test the QuestionLevelDTO class
 * @author Dylan I. Kessler
 *
 */
@RunWith(SpringRunner.class)
public class QuestionLevelDTOTest {

	/**
	 * test both constructors, getters and setters, and toString() method
	 */
	@Test
	public void testClass() {
		QuestionLevelDTO classUnderTest = new QuestionLevelDTO();
		classUnderTest.setQuestionLevelId(new Integer(1));
		classUnderTest.setQuestionLevelDesc("a");
		classUnderTest.setEvntTmestmp(new Date());
		classUnderTest.setEvntOperId("a");
		assertTrue(true);
		QuestionLevelDTO newitem = new QuestionLevelDTO(
		classUnderTest.getQuestionLevelId(),
		classUnderTest.getQuestionLevelDesc(),
		classUnderTest.getEvntTmestmp(),
		classUnderTest.getEvntOperId());
		assertNotNull(newitem);
		String string = classUnderTest.toString();
		assertNotNull(string);
		assertTrue(string.length()>0);
	}

}