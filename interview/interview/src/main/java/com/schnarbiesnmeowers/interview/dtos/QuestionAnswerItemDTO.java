package com.schnarbiesnmeowers.interview.dtos;

import java.io.Serializable;

public class QuestionAnswerItemDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private Integer questionId;

	/**
	 * 
	 */
	private Integer questionCategoryId;

	/**
	 * 
	 */
	private Integer questionLevelId;

	/**
	 * 
	 */
	private Integer answerId;

	/**
	 * 
	 */
	private String questionTxt;
	
	/**
	 * 
	 */
	private String answerTxt;
	
	/**
	 * 
	 */
	private String evntOperId;
	
	public QuestionAnswerItemDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public Integer getQuestionCategoryId() {
		return questionCategoryId;
	}

	public void setQuestionCategoryId(Integer questionCategoryId) {
		this.questionCategoryId = questionCategoryId;
	}

	public Integer getQuestionLevelId() {
		return questionLevelId;
	}

	public void setQuestionLevelId(Integer questionLevelId) {
		this.questionLevelId = questionLevelId;
	}

	public Integer getAnswerId() {
		return answerId;
	}

	public void setAnswerId(Integer answerId) {
		this.answerId = answerId;
	}

	public String getQuestionTxt() {
		return questionTxt;
	}

	public void setQuestionTxt(String questionTxt) {
		this.questionTxt = questionTxt;
	}

	public String getAnswerTxt() {
		return answerTxt;
	}

	public void setAnswerTxt(String answerTxt) {
		this.answerTxt = answerTxt;
	}

	public String getEvntOperId() {
		return evntOperId;
	}

	public void setEvntOperId(String evntOperId) {
		this.evntOperId = evntOperId;
	}
	
	
}
