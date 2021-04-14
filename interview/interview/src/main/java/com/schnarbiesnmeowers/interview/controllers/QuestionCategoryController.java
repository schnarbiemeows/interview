package com.schnarbiesnmeowers.interview.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import javax.validation.Valid;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.schnarbiesnmeowers.interview.business.*;
import com.schnarbiesnmeowers.interview.dtos.*;
import com.schnarbiesnmeowers.interview.pojos.*;

/**
 * this class is the main REST controller
 * @author Dylan I. Kessler
 *
 */
@CrossOrigin
@RestController
@RequestMapping(path="/questioncategory")
public class QuestionCategoryController {

	//private static final Logger applicationLogger = LogManager.getLogger("FileAppender");

	/**
	 * JPA Repository handle
	 */
	@Autowired
	private QuestionCategoryBusiness businessService;;

	/**
	 * get all QuestionCategory records
	 * @return Iterable<QuestionCategory>
	 */
	@GetMapping(path = "/all")
	public ResponseEntity<List<QuestionCategoryDTO>> getAllQuestionCategory() throws Exception {
		List<QuestionCategoryDTO> questioncategory = businessService.getAllQuestionCategory();
		return ResponseEntity.status(HttpStatus.OK).body(questioncategory);
	}

	/**
	 * get QuestionCategory by primary key
	 * @param id
	 * @return QuestionCategory
	 */
	@GetMapping(path = "/findById/{id}")
	public ResponseEntity<QuestionCategoryDTO> findQuestionCategoryById(@PathVariable int id) throws Exception {
		QuestionCategoryDTO results = businessService.findQuestionCategoryById(id);
		return ResponseEntity.status(HttpStatus.OK).body(results);
	}

	/**
	 * create a new QuestionCategory
	 * @param QuestionCategoryDTO
	 * @return QuestionCategory
	 */
	@PostMapping(path = "/create")
	public ResponseEntity<QuestionCategoryDTO> createQuestionCategory(@Valid @RequestBody QuestionCategoryDTO data) throws Exception {
		try {
		    QuestionCategoryDTO createdData = businessService.createQuestionCategory(data);
		    return ResponseEntity.status(HttpStatus.CREATED).body(createdData);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * update a QuestionCategory
	 * @param QuestionCategoryDTO
	 * @return QuestionCategory
	 */
	@PostMapping(path = "/update")
	public ResponseEntity<QuestionCategoryDTO> updateQuestionCategory(@Valid @RequestBody QuestionCategoryDTO data) throws Exception {
		QuestionCategoryDTO updatedData = businessService.updateQuestionCategory(data);
		return ResponseEntity.status(HttpStatus.OK).body(updatedData);
	}

	/**
	 * delete a QuestionCategory by primary key
	 * @param id
	 */
	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<ResponseMessage> deleteQuestionCategory(@PathVariable int id) throws Exception {
		businessService.deleteQuestionCategory(id);
		ResponseMessage rb = new ResponseMessage("successfully deleted");
		return ResponseEntity.status(HttpStatus.OK).body(rb);
	}

}
