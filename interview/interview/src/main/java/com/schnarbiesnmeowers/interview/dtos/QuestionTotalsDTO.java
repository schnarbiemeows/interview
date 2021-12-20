package com.schnarbiesnmeowers.interview.dtos;

import java.io.Serializable;

import javax.persistence.Column;

import com.google.gson.Gson;
import com.schnarbiesnmeowers.interview.pojos.QuestionTotals;

/**
 * 
 * @author dylan
 *
 */
public class QuestionTotalsDTO implements Serializable {
	// default serial version id, required for serializable classes
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String categoryDescription;
	
	/**
	 * 
	 */
	private Integer easyDifficulty;
	
	/**
	 * 
	 */
	private Integer mediumDifficulty;
	
	/**
	 * 
	 */
	private Integer hardDifficulty;

	private Integer totalQuestions;
	
	/**
	 * 
	 */
	public QuestionTotalsDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuestionTotalsDTO(String categoryDescription, Integer easyDifficulty, Integer mediumDifficulty,
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
		return "QuestionTotalsDTO [categoryDescription=" + categoryDescription + ", easyDifficulty=" + easyDifficulty
				+ ", mediumDifficulty=" + mediumDifficulty + ", hardDifficulty=" + hardDifficulty + ", totalQuestions="
				+ totalQuestions + "]";
	}

	public static QuestionTotalsDTO fromJson(String input) {
		Gson gson = new Gson();
		return gson.fromJson(input, QuestionTotalsDTO.class );
	}
	
	public QuestionTotals toEntity() {
		return new QuestionTotals(this.getCategoryDescription(),this.getEasyDifficulty(),this.getMediumDifficulty(),this.getHardDifficulty(),this.getTotalQuestions());
	}
	
}
