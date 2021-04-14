package com.schnarbiesnmeowers.interview.pojos;

import com.schnarbiesnmeowers.interview.dtos.QuestionLevelDTO;
import javax.persistence.*;
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
@Entity
@Table(name = "question_level")
public class QuestionLevel implements Serializable {
	// default serial version id, required for serializable classes
	private static final long serialVersionUID = 1L;

	//private static final Logger applicationLogger = LogManager.getLogger("FileAppender");

	/**
	 * 
	 */
	@Column(name = "question_level_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer questionLevelId;

	/**
	 * 
	 */
	@Column(name = "question_level_desc")
	private String questionLevelDesc;

	/**
	 * 
	 */
	@Column(name = "evnt_tmestmp")
	private Date evntTmestmp;

	/**
	 * 
	 */
	@Column(name = "evnt_oper_id")
	private String evntOperId;

	/**
	 * default constructor
	 */
	public QuestionLevel() {
		super();
	}

	public QuestionLevel(Integer questionLevelId, String questionLevelDesc, Date evntTmestmp, String evntOperId) {
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
		return "QuestionLevel [questionLevelId=" + questionLevelId + ", questionLevelDesc=" + questionLevelDesc + ", evntTmestmp=" + evntTmestmp + ", evntOperId=" + evntOperId + "]";
	}

	public static QuestionLevel fromJson(String input) {
		Gson gson = new Gson();
		return gson.fromJson(input, QuestionLevel.class );
	}
	public QuestionLevelDTO toDTO() {
		return new QuestionLevelDTO(this.getQuestionLevelId(),this.getQuestionLevelDesc(),this.getEvntTmestmp(),this.getEvntOperId());
	}
}
