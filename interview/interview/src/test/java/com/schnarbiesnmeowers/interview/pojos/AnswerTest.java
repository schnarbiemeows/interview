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
 * class to test the Answer class
 * @author Dylan I. Kessler
 *
 */
@RunWith(SpringRunner.class)
public class AnswerTest {

	public static final String A = "a";
	/**
	 * test both constructors, getters and setters, and toString() method
	 */
	@Test
	public void testClass() {
		Answer classUnderTest = new Answer();
		classUnderTest.setAnswerId(new Integer(1));
		classUnderTest.setAnswerTxt(A);
		classUnderTest.setEvntTmestmp(new Date());
		classUnderTest.setEvntOperId(A);
		assertTrue(true);
		Answer newitem = new Answer(
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