package com.schnarbiesnmeowers.interview.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.schnarbiesnmeowers.interview.pojos.QuestionTotals;


public interface QuestionTotalsRepository extends JpaRepository<QuestionTotals, String> {

	/**
	 * get Iterable<QuestionTotals> using a stored procedure
	 * @return
	 */
	//@Procedure(name = "getQuestionTotals")
	@Query(value = "call getQuestionTotals();", nativeQuery = true)
	public Iterable<QuestionTotals> getQuestionTotals();
}
