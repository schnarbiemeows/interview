package com.schnarbiesnmeowers.interview.dtos;

import com.schnarbiesnmeowers.interview.pojos.QuestionCategory;
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
public class QuestionCategoryDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Integer questionCategoryId;

	/**
	 * 
	 */
	private String questionCategoryDesc;

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
	public QuestionCategoryDTO() {
		super();
	}

	public QuestionCategoryDTO(Integer questionCategoryId, String questionCategoryDesc, Date evntTmestmp, String evntOperId) {
		super();
		this.questionCategoryId = questionCategoryId;
		this.questionCategoryDesc = questionCategoryDesc;
		this.evntTmestmp = evntTmestmp;
		this.evntOperId = evntOperId;
	}

	public Integer getQuestionCategoryId() {
		return questionCategoryId;
	}

	public void setQuestionCategoryId(Integer questionCategoryId) {
		this.questionCategoryId=questionCategoryId;
	}

	public String getQuestionCategoryDesc() {
		return questionCategoryDesc;
	}

	public void setQuestionCategoryDesc(String questionCategoryDesc) {
		this.questionCategoryDesc=questionCategoryDesc;
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
		return "QuestionCategoryDTO [questionCategoryId=" + questionCategoryId + ", questionCategoryDesc=" + questionCategoryDesc + ", evntTmestmp=" + evntTmestmp + ", evntOperId=" + evntOperId + "]";
	}

	public static QuestionCategoryDTO fromJson(String input) {
		Gson gson = new Gson();
		return gson.fromJson(input, QuestionCategoryDTO.class );
	}
	public QuestionCategory toEntity() {
		return new QuestionCategory(this.getQuestionCategoryId(),this.getQuestionCategoryDesc(),this.getEvntTmestmp(),this.getEvntOperId());
	}
}
