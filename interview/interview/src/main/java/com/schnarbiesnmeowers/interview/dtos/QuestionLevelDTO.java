package com.schnarbiesnmeowers.interview.dtos;

import com.schnarbiesnmeowers.interview.pojos.QuestionLevel;
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
public class QuestionLevelDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Integer questionLevelId;

	/**
	 * 
	 */
	private String questionLevelDesc;

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
	public QuestionLevelDTO() {
		super();
	}

	public QuestionLevelDTO(Integer questionLevelId, String questionLevelDesc, Date evntTmestmp, String evntOperId) {
		super();
		this.questionLevelId = questionLevelId;
		this.questionLevelDesc = questionLevelDesc;
		this.evntTmestmp = evntTmestmp;
		this.evntOperId = evntOperId;
	}

	public Integer getQuestionLevelId() {
		return questionLevelId;
	}

	public void setQuestionLevelId(Integer questionLevelId) {
		this.questionLevelId=questionLevelId;
	}

	public String getQuestionLevelDesc() {
		return questionLevelDesc;
	}

	public void setQuestionLevelDesc(String questionLevelDesc) {
		this.questionLevelDesc=questionLevelDesc;
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
		return "QuestionLevelDTO [questionLevelId=" + questionLevelId + ", questionLevelDesc=" + questionLevelDesc + ", evntTmestmp=" + evntTmestmp + ", evntOperId=" + evntOperId + "]";
	}

	public static QuestionLevelDTO fromJson(String input) {
		Gson gson = new Gson();
		return gson.fromJson(input, QuestionLevelDTO.class );
	}
	public QuestionLevel toEntity() {
		return new QuestionLevel(this.getQuestionLevelId(),this.getQuestionLevelDesc(),this.getEvntTmestmp(),this.getEvntOperId());
	}
}
