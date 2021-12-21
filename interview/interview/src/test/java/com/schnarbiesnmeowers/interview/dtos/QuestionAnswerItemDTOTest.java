package com.schnarbiesnmeowers.interview.dtos;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
public class QuestionAnswerItemDTOTest {

	public static final int ONE = 1;
	public static final String A = "a";
	/**
	 * test both constructors, getters and setters, and toString() method
	 */
	@Test
	public void testClass() {
		QuestionAnswerItemDTO dto = new QuestionAnswerItemDTO();
		dto.setAnswerId(ONE);
		dto.setAnswerTxt(A);
		dto.setEvntOperId(A);
		dto.setQuestionCategoryId(ONE);
		dto.setQuestionId(ONE);
		dto.setQuestionLevelId(ONE);
		dto.setQuestionTxt(A);
		assertTrue(dto.getAnswerId()==ONE);
		assertTrue(dto.getQuestionCategoryId()==ONE);
		assertTrue(dto.getQuestionId()==ONE);
		assertTrue(dto.getQuestionLevelId()==ONE);
		assertTrue(dto.getAnswerTxt().equals(A));
		assertTrue(dto.getQuestionTxt().equals(A));
		assertTrue(dto.getEvntOperId().equals(A));
	}
}
