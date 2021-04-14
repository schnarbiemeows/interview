package com.schnarbiesnmeowers.interview.pojos;

import com.schnarbiesnmeowers.interview.dtos.QuestionDTO;
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
@Table(name = "question")
public class Question implements Serializable {
	// default serial version id, required for serializable classes
	private static final long serialVersionUID = 1L;

	//private static final Logger applicationLogger = LogManager.getLogger("FileAppender");

	/**
	 * 
	 */
	@Column(name = "question_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer questionId;

	/**
	 * 
	 */
	@Column(name = "question_category_id")
	private Integer questionCategoryId;

	/**
	 * 
	 */
	@Column(name = "question_level_id")
	private Integer questionLevelId;

	/**
	 * 
	 */
	@Column(name = "answer_id")
	private Integer answerId;

	/**
	 * 
	 */
	@Column(name = "question_txt")
	private String questionTxt;

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
	public Question() {
		super();
	}

	public Question(Integer questionId, Integer questionCategoryId, Integer questionLevelId, Integer answerId, String questionTxt, Date evntTmestmp, String evntOperId) {
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
		return "Question [questionId=" + questionId + ", questionCategoryId=" + questionCategoryId + ", questionLevelId=" + questionLevelId + ", answerId=" + answerId + ", questionTxt=" + questionTxt + ", evntTmestmp=" + evntTmestmp + ", evntOperId=" + evntOperId + "]";
	}

	public static Question fromJson(String input) {
		Gson gson = new Gson();
		return gson.fromJson(input, Question.class );
	}
	public QuestionDTO toDTO() {
		return new QuestionDTO(this.getQuestionId(),this.getQuestionCategoryId(),this.getQuestionLevelId(),this.getAnswerId(),this.getQuestionTxt(),this.getEvntTmestmp(),this.getEvntOperId());
	}
}
