package com.schnarbiesnmeowers.interview.pojos;

import com.schnarbiesnmeowers.interview.dtos.QuestionCategoryDTO;
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
@Table(name = "question_category")
public class QuestionCategory implements Serializable {
	// default serial version id, required for serializable classes
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Column(name = "question_category_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer questionCategoryId;

	/**
	 * 
	 */
	@Column(name = "question_category_desc")
	private String questionCategoryDesc;

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
	 * some categories we only want certain people(me) to see
	 * default is "Y", which means everyone can see
	 */
	@Column(name = "display_cde")
	private String displayCde;

	/**
	 * default constructor
	 */
	public QuestionCategory() {
		super();
	}

	public QuestionCategory(Integer questionCategoryId, String questionCategoryDesc, Date evntTmestmp, String evntOperId, String displayCde) {
		super();
		this.questionCategoryId = questionCategoryId;
		this.questionCategoryDesc = questionCategoryDesc;
		this.evntTmestmp = evntTmestmp;
		this.evntOperId = evntOperId;
		this.displayCde = displayCde;
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

	public String getDisplayCde() {
		return displayCde;
	}

	public void setDisplayCde(String displayCde) {
		this.displayCde = displayCde;
	}

	@Override
	public String toString() {
		return "QuestionCategory [questionCategoryId=" + questionCategoryId + ", questionCategoryDesc=" + questionCategoryDesc + ", evntTmestmp=" + evntTmestmp + ", evntOperId=" + evntOperId + ", displayCde=" + displayCde + "]";
	}

	public static QuestionCategory fromJson(String input) {
		Gson gson = new Gson();
		return gson.fromJson(input, QuestionCategory.class );
	}
	public QuestionCategoryDTO toDTO() {
		return new QuestionCategoryDTO(this.getQuestionCategoryId(),this.getQuestionCategoryDesc(),this.getEvntTmestmp(),this.getEvntOperId(),this.getDisplayCde());
	}
}
