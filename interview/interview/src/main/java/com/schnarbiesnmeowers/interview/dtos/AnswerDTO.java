package com.schnarbiesnmeowers.interview.dtos;

import com.schnarbiesnmeowers.interview.pojos.Answer;
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
public class AnswerDTO implements Serializable {
	// default serial version id, required for serializable classes
	private static final long serialVersionUID = 1L;

	//private static final Logger applicationLogger = LogManager.getLogger("FileAppender");

	/**
	 * 
	 */
	private Integer answerId;

	/**
	 * 
	 */
	private String answerTxt;

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
	public AnswerDTO() {
		super();
	}

	public AnswerDTO(Integer answerId, String answerTxt, Date evntTmestmp, String evntOperId) {
		super();
		this.answerId = answerId;
		this.answerTxt = answerTxt;
		this.evntTmestmp = evntTmestmp;
		this.evntOperId = evntOperId;
	}

	public Integer getAnswerId() {
		return answerId;
	}

	public void setAnswerId(Integer answerId) {
		this.answerId=answerId;
	}

	public String getAnswerTxt() {
		return answerTxt;
	}

	public void setAnswerTxt(String answerTxt) {
		this.answerTxt=answerTxt;
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
		return "AnswerDTO [answerId=" + answerId + ", answerTxt=" + answerTxt + ", evntTmestmp=" + evntTmestmp + ", evntOperId=" + evntOperId + "]";
	}

	public static AnswerDTO fromJson(String input) {
		Gson gson = new Gson();
		return gson.fromJson(input, AnswerDTO.class );
	}
	public Answer toEntity() {
		return new Answer(this.getAnswerId(),this.getAnswerTxt(),this.getEvntTmestmp(),this.getEvntOperId());
	}
}
