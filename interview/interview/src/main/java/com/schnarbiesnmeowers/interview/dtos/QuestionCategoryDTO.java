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
public class QuestionCategoryDTO implements Serializable, Comparable<QuestionCategoryDTO> {
	
	private static final String[] sortingItems = {"Java - ","Python","Scala","SQL","Linux","front end","Spark -"}; // 7 items
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
	 * 
	 */
	private String displayCde;
	
	/**
	 * default constructor
	 */
	public QuestionCategoryDTO() {
		super();
	}

	public QuestionCategoryDTO(Integer questionCategoryId, String questionCategoryDesc, Date evntTmestmp, String evntOperId, String displayCde) {
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
		return "QuestionCategoryDTO [questionCategoryId=" + questionCategoryId + ", questionCategoryDesc=" + questionCategoryDesc + ", evntTmestmp=" + evntTmestmp + ", evntOperId=" + evntOperId + ", displayCde=" + displayCde + "]";
	}

	public static QuestionCategoryDTO fromJson(String input) {
		Gson gson = new Gson();
		return gson.fromJson(input, QuestionCategoryDTO.class );
	}
	public QuestionCategory toEntity() {
		return new QuestionCategory(this.getQuestionCategoryId(),this.getQuestionCategoryDesc(),this.getEvntTmestmp(),this.getEvntOperId(),this.getDisplayCde());
	}

	@Override
	public int compareTo(QuestionCategoryDTO o) {
		/**
		 * the order that I want the categories in:
		 * 1. Java first
		 * 2. Python then Scala
		 * 3. SQL stuff
		 * 4. linux
		 * 5. front end
		 * 6. Spark
		 * 7. other stuff
		 * 
		 */
		// compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than 
        // other and 0 if they are supposed to be equal
		return innerCompareTo(o,0);
	}
	
	private int innerCompareTo(QuestionCategoryDTO that, int index) {
		if(index<sortingItems.length) {
			String wordToSearchFor = sortingItems[index];
			if(this.questionCategoryDesc.toUpperCase().contains(wordToSearchFor.toUpperCase()) &&
					!that.questionCategoryDesc.toUpperCase().contains(wordToSearchFor.toUpperCase())) {
				return -1;
			} else if(!this.questionCategoryDesc.toUpperCase().contains(wordToSearchFor.toUpperCase()) &&
					that.questionCategoryDesc.toUpperCase().contains(wordToSearchFor.toUpperCase())) {
				return 1;
			} else return innerCompareTo(that,index+1);
		} else {
			return this.questionCategoryDesc.compareTo(that.getQuestionCategoryDesc());
		}
	}
}
