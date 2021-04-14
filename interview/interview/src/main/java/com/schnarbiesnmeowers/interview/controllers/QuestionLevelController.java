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
@RequestMapping(path="/questionlevel")
public class QuestionLevelController {

	//private static final Logger applicationLogger = LogManager.getLogger("FileAppender");

	/**
	 * JPA Repository handle
	 */
	@Autowired
	private QuestionLevelBusiness businessService;;

	/**
	 * get all QuestionLevel records
	 * @return Iterable<QuestionLevel>
	 */
	@GetMapping(path = "/all")
	public ResponseEntity<List<QuestionLevelDTO>> getAllQuestionLevel() throws Exception {
		List<QuestionLevelDTO> questionlevel = businessService.getAllQuestionLevel();
		return ResponseEntity.status(HttpStatus.OK).body(questionlevel);
	}

	/**
	 * get QuestionLevel by primary key
	 * @param id
	 * @return QuestionLevel
	 */
	@GetMapping(path = "/findById/{id}")
	public ResponseEntity<QuestionLevelDTO> findQuestionLevelById(@PathVariable int id) throws Exception {
		QuestionLevelDTO results = businessService.findQuestionLevelById(id);
		return ResponseEntity.status(HttpStatus.OK).body(results);
	}

	/**
	 * create a new QuestionLevel
	 * @param QuestionLevelDTO
	 * @return QuestionLevel
	 */
	@PostMapping(path = "/create")
	public ResponseEntity<QuestionLevelDTO> createQuestionLevel(@Valid @RequestBody QuestionLevelDTO data) throws Exception {
		try {
		    QuestionLevelDTO createdData = businessService.createQuestionLevel(data);
		    return ResponseEntity.status(HttpStatus.CREATED).body(createdData);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * update a QuestionLevel
	 * @param QuestionLevelDTO
	 * @return QuestionLevel
	 */
	@PostMapping(path = "/update")
	public ResponseEntity<QuestionLevelDTO> updateQuestionLevel(@Valid @RequestBody QuestionLevelDTO data) throws Exception {
		QuestionLevelDTO updatedData = businessService.updateQuestionLevel(data);
		return ResponseEntity.status(HttpStatus.OK).body(updatedData);
	}

	/**
	 * delete a QuestionLevel by primary key
	 * @param id
	 */
	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<ResponseMessage> deleteQuestionLevel(@PathVariable int id) throws Exception {
		businessService.deleteQuestionLevel(id);
		ResponseMessage rb = new ResponseMessage("successfully deleted");
		return ResponseEntity.status(HttpStatus.OK).body(rb);
	}

}
