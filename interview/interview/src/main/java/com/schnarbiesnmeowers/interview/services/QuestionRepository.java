package com.schnarbiesnmeowers.interview.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.schnarbiesnmeowers.interview.pojos.Question;
/**
 *
 * @author Dylan I. Kessler
 *
 */
public interface QuestionRepository extends JpaRepository<Question, Integer>{


	/**
	 * get Iterable<Question> by foreign key : questionCategoryId
	 * @param questionCategoryId
	 * @return Iterable<Question>
	*/
	public Iterable<Question> findQuestionByQuestionCategoryId(int questionCategoryId);
	/**
	 * get Iterable<Question> by foreign key : questionLevelId
	 * @param questionLevelId
	 * @return Iterable<Question>
	*/
	public Iterable<Question> findQuestionByQuestionLevelId(int questionLevelId);
	/**
	 * get Iterable<Question> by foreign key : answerId
	 * @param answerId
	 * @return Iterable<Question>
	*/
	public Iterable<Question> findQuestionByAnswerId(int answerId);
	/**
	 * get Iterable<Question> by all foreign keys
	 * @return Iterable<Question>
	*/
	public Iterable<Question> findQuestionByQuestionCategoryIdAndQuestionLevelIdAndAnswerId(int questionCategoryId,int questionLevelId,int answerId);
	
}
