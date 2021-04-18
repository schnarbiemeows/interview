package com.schnarbiesnmeowers.interview.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schnarbiesnmeowers.interview.pojos.QuestionCategory;
/**
 *
 * @author Dylan I. Kessler
 *
 */
public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Integer>{


}
