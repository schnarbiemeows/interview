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
 * class to test the QuestionCategory class
 * @author Dylan I. Kessler
 *
 */
//@RunWith(SpringRunner.class)
public class QuestionCategoryTest {

	/**
	 * test both constructors, getters and setters, and toString() method
	 */
	@Test
	public void testClass() {
		QuestionCategory classUnderTest = new QuestionCategory();
		classUnderTest.setQuestionCategoryId(new Integer(1));
		classUnderTest.setQuestionCategoryDesc("a");
		classUnderTest.setEvntTmestmp(new Date());
		classUnderTest.setEvntOperId("a");
		classUnderTest.setDisplayCde("Y");
		assertTrue(true);
		QuestionCategory newitem = new QuestionCategory(
		classUnderTest.getQuestionCategoryId(),
		classUnderTest.getQuestionCategoryDesc(),
		classUnderTest.getEvntTmestmp(),
		classUnderTest.getEvntOperId(),
		classUnderTest.getDisplayCde());
		assertNotNull(newitem);
		String string = classUnderTest.toString();
		assertNotNull(string);
		assertTrue(string.length()>0);
	}

}