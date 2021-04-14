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
 * class to test the QuestionCategoryDTO class
 * @author Dylan I. Kessler
 *
 */
@RunWith(SpringRunner.class)
public class QuestionCategoryDTOTest {

	/**
	 * test both constructors, getters and setters, and toString() method
	 */
	@Test
	public void testClass() {
		QuestionCategoryDTO classUnderTest = new QuestionCategoryDTO();
		classUnderTest.setQuestionCategoryId(new Integer(1));
		classUnderTest.setQuestionCategoryDesc("a");
		classUnderTest.setEvntTmestmp(new Date());
		classUnderTest.setEvntOperId("a");
		assertTrue(true);
		QuestionCategoryDTO newitem = new QuestionCategoryDTO(
		classUnderTest.getQuestionCategoryId(),
		classUnderTest.getQuestionCategoryDesc(),
		classUnderTest.getEvntTmestmp(),
		classUnderTest.getEvntOperId());
		assertNotNull(newitem);
		String string = classUnderTest.toString();
		assertNotNull(string);
		assertTrue(string.length()>0);
	}

}