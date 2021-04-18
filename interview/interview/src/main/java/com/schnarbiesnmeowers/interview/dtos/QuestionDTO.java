package com.schnarbiesnmeowers.interview.dtos;

import com.schnarbiesnmeowers.interview.pojos.Question;
import javax.validation.constraints.*;
import java.io.Serializable;
import com.google.gson.Gson;
import java.math.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 *
 * @author Dylan I. Kessler
 *
 */
public class QuestionDTO implements Serializable {
	
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
	private Date evntTmestmp;

	/**
	 * 
	 */
	private String evntOperId;

	/**
	 * default constructor
	 */
	public QuestionDTO() {
		super();
	}

	public QuestionDTO(Integer questionId, Integer questionCategoryId, Integer questionLevelId, Integer answerId, String questionTxt, Date evntTmestmp, String evntOperId) {
		super();
		this.questionId = questionId;
		this.questionCategoryId = questionCategoryId;
		this.questionLevelId = questionLevelId;
		this.answerId = answerId;
		this.questionTxt = questionTxt;
		this.evntTmestmp = evntTmestmp;
		this.evntOperId = evntOperId;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId=questionId;
	}

	public Integer getQuestionCategoryId() {
		return questionCategoryId;
	}

	public void setQuestionCategoryId(Integer questionCategoryId) {
		this.questionCategoryId=questionCategoryId;
	}

	public Integer getQuestionLevelId() {
		return questionLevelId;
	}

	public void setQuestionLevelId(Integer questionLevelId) {
		this.questionLevelId=questionLevelId;
	}

	public Integer getAnswerId() {
		return answerId;
	}

	public void setAnswerId(Integer answerId) {
		this.answerId=answerId;
	}

	public String getQuestionTxt() {
		return questionTxt;
	}

	public void setQuestionTxt(String questionTxt) {
		this.questionTxt=questionTxt;
	}

	public Date getEvntTmestmp() {
		return evntTmestmp;
	}

	public void setEvntTmestmp(Date evntTmestmp) {
		this.evntTmestmp=evntTmestmp;
	}

	public String getEvntOperId() {
		return evntOperId;
	}

	public void setEvntOperId(String evntOperId) {
		this.evntOperId=evntOperId;
	}

	@Override
	public String toString() {
		return "QuestionDTO [questionId=" + questionId + ", questionCategoryId=" + questionCategoryId + ", questionLevelId=" + questionLevelId + ", answerId=" + answerId + ", questionTxt=" + questionTxt + ", evntTmestmp=" + evntTmestmp + ", evntOperId=" + evntOperId + "]";
	}

	public static QuestionDTO fromJson(String input) {
		Gson gson = new Gson();
		return gson.fromJson(input, QuestionDTO.class );
	}
	public Question toEntity() {
		return new Question(this.getQuestionId(),this.getQuestionCategoryId(),this.getQuestionLevelId(),this.getAnswerId(),this.getQuestionTxt(),this.getEvntTmestmp(),this.getEvntOperId());
	}
}
