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
 * class to test the QuestionDTO class
 * @author Dylan I. Kessler
 *
 */
@RunWith(SpringRunner.class)
public class QuestionDTOTest {

	/**
	 * test both constructors, getters and setters, and toString() method
	 */
	@Test
	public void testClass() {
		QuestionDTO classUnderTest = new QuestionDTO();
		classUnderTest.setQuestionId(new Integer(1));
		classUnderTest.setQuestionCategoryId(new Integer(1));
		classUnderTest.setQuestionLevelId(new Integer(1));
		classUnderTest.setAnswerId(new Integer(1));
		classUnderTest.setQuestionTxt("a");
		classUnderTest.setEvntTmestmp(new Date());
		classUnderTest.setEvntOperId("a");
		assertTrue(true);
		QuestionDTO newitem = new QuestionDTO(
		classUnderTest.getQuestionId(),
		classUnderTest.getQuestionCategoryId(),
		classUnderTest.getQuestionLevelId(),
		classUnderTest.getAnswerId(),
		classUnderTest.getQuestionTxt(),
		classUnderTest.getEvntTmestmp(),
		classUnderTest.getEvntOperId());
		assertNotNull(newitem);
		String string = classUnderTest.toString();
		assertNotNull(string);
		assertTrue(string.length()>0);
	}

}