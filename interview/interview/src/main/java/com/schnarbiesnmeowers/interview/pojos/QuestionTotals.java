package com.schnarbiesnmeowers.interview.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.Table;

import com.google.gson.Gson;
import com.schnarbiesnmeowers.interview.dtos.QuestionTotalsDTO;

/**
*
* @author Dylan I. Kessler
*
*/
@Entity
@Table(name = "question_totals")
/*
 * @NamedStoredProcedureQuery(name = "getQuestionTotals", procedureName =
 * "GetQuestionTotals", resultClasses = QuestionTotals.class, parameters = {})
 */
public class QuestionTotals implements Serializable {
	// default serial version id, required for serializable classes
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@Id
	@Column(name = "category_description")
	private String categoryDescription;
	
	@Column(name = "EASY")
	private Integer easyDifficulty;
	
	@Column(name = "MEDIUM")
	private Integer mediumDifficulty;
	
	@Column(name = "HARD")
	private Integer hardDifficulty;
	
	@Column(name = "TOTAL_QUESTIONS")
	private Integer totalQuestions;

	public QuestionTotals() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuestionTotals(String categoryDescription, Integer easyDifficulty, Integer mediumDifficulty,
			Integer hardDifficulty, Integer totalQuestions) {
		super();
		this.categoryDescription = categoryDescription;
		this.easyDifficulty = easyDifficulty;
		this.mediumDifficulty = mediumDifficulty;
		this.hardDifficulty = hardDifficulty;
		this.totalQuestions = totalQuestions;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public Integer getEasyDifficulty() {
		return easyDifficulty;
	}

	public void setEasyDifficulty(Integer easyDifficulty) {
		this.easyDifficulty = easyDifficulty;
	}

	public Integer getMediumDifficulty() {
		return mediumDifficulty;
	}

	public void setMediumDifficulty(Integer mediumDifficulty) {
		this.mediumDifficulty = mediumDifficulty;
	}

	public Integer getHardDifficulty() {
		return hardDifficulty;
	}

	public void setHardDifficulty(Integer hardDifficulty) {
		this.hardDifficulty = hardDifficulty;
	}

	
	public Integer getTotalQuestions() {
		return totalQuestions;
	}

	public void setTotalQuestions(Integer totalQuestions) {
		this.totalQuestions = totalQuestions;
	}

	@Override
	public String toString() {
		return "QuestionTotals [categoryDescription=" + categoryDescription + ", easyDifficulty=" + easyDifficulty
				+ ", mediumDifficulty=" + mediumDifficulty + ", hardDifficulty=" + hardDifficulty + ", totalQuestions="
				+ totalQuestions + "]";
	}

	public static QuestionTotals fromJson(String input) {
		Gson gson = new Gson();
		return gson.fromJson(input, QuestionTotals.class );
	}
	public QuestionTotalsDTO toDTO() {
		return new QuestionTotalsDTO(this.getCategoryDescription(),this.getEasyDifficulty(),this.getMediumDifficulty(),this.getHardDifficulty(),this.getTotalQuestions());
	}
	
}
