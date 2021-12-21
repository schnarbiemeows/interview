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
 * class to test the QuestionLevel class
 * @author Dylan I. Kessler
 *
 */
//@RunWith(SpringRunner.class)
public class QuestionLevelTest {

	/**
	 * test both constructors, getters and setters, and toString() method
	 */
	@Test
	public void testClass() {
		QuestionLevel classUnderTest = new QuestionLevel();
		classUnderTest.setQuestionLevelId(new Integer(1));
		classUnderTest.setQuestionLevelDesc("a");
		classUnderTest.setEvntTmestmp(new Date());
		classUnderTest.setEvntOperId("a");
		assertTrue(true);
		QuestionLevel newitem = new QuestionLevel(
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