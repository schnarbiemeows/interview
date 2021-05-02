package com.schnarbiesnmeowers.interview.controllers;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schnarbiesnmeowers.interview.business.QuestionCategoryBusiness;
import com.schnarbiesnmeowers.interview.dtos.QuestionCategoryDTO;
import com.schnarbiesnmeowers.interview.exceptions.handler.QuestionCategoryExceptionHandling;
import com.schnarbiesnmeowers.interview.pojos.ResponseMessage;

/**
 * this class is the main REST controller
 * @author Dylan I. Kessler
 *
 */
//@CrossOrigin
@RestController
@RequestMapping(path="/questioncategory")
public class QuestionCategoryController extends QuestionCategoryExceptionHandling {

	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");

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
	@PreAuthorize("hasAnyAuthority('data:select')")
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
	@PreAuthorize("hasAnyAuthority('data:select')")
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
	@PreAuthorize("hasAnyAuthority('data:create')")
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
	@PreAuthorize("hasAnyAuthority('data:update')")
	public ResponseEntity<QuestionCategoryDTO> updateQuestionCategory(@Valid @RequestBody QuestionCategoryDTO data) throws Exception {
		QuestionCategoryDTO updatedData = businessService.updateQuestionCategory(data);
		return ResponseEntity.status(HttpStatus.OK).body(updatedData);
	}

	/**
	 * delete a QuestionCategory by primary key
	 * @param id
	 */
	@DeleteMapping(path = "/delete/{id}")
	@PreAuthorize("hasAnyAuthority('data:delete')")
	public ResponseEntity<ResponseMessage> deleteQuestionCategory(@PathVariable int id) throws Exception {
		businessService.deleteQuestionCategory(id);
		ResponseMessage rb = new ResponseMessage("successfully deleted");
		return ResponseEntity.status(HttpStatus.OK).body(rb);
	}

	private static void logAction(String message) {
    	System.out.println(message);
    	applicationLogger.debug(message);
    }
}
