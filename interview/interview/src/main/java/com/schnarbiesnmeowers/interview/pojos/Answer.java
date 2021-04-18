package com.schnarbiesnmeowers.interview.pojos;

import com.schnarbiesnmeowers.interview.dtos.AnswerDTO;
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
@Table(name = "answer")
public class Answer implements Serializable {
	// default serial version id, required for serializable classes
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Column(name = "answer_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer answerId;

	/**
	 * 
	 */
	@Column(name = "answer_txt")
	private String answerTxt;

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
	public Answer() {
		super();
	}

	public Answer(Integer answerId, String answerTxt, Date evntTmestmp, String evntOperId) {
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
		return "Answer [answerId=" + answerId + ", answerTxt=" + answerTxt + ", evntTmestmp=" + evntTmestmp + ", evntOperId=" + evntOperId + "]";
	}

	public static Answer fromJson(String input) {
		Gson gson = new Gson();
		return gson.fromJson(input, Answer.class );
	}
	public AnswerDTO toDTO() {
		return new AnswerDTO(this.getAnswerId(),this.getAnswerTxt(),this.getEvntTmestmp(),this.getEvntOperId());
	}
}
