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

import com.schnarbiesnmeowers.interview.business.QuestionLevelBusiness;
import com.schnarbiesnmeowers.interview.dtos.QuestionLevelDTO;
import com.schnarbiesnmeowers.interview.exceptions.handler.QuestionLevelExceptionHandling;
import com.schnarbiesnmeowers.interview.pojos.ResponseMessage;

/**
 * this class is the main REST controller
 * @author Dylan I. Kessler
 *
 */
@CrossOrigin
@RestController
@RequestMapping(path="/questionlevel")
public class QuestionLevelController extends QuestionLevelExceptionHandling {

	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");

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
	@PreAuthorize("hasAnyAuthority('data:select')")
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
	@PreAuthorize("hasAnyAuthority('data:select')")
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
	@PreAuthorize("hasAnyAuthority('data:create')")
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
	@PreAuthorize("hasAnyAuthority('data:update')")
	public ResponseEntity<QuestionLevelDTO> updateQuestionLevel(@Valid @RequestBody QuestionLevelDTO data) throws Exception {
		QuestionLevelDTO updatedData = businessService.updateQuestionLevel(data);
		return ResponseEntity.status(HttpStatus.OK).body(updatedData);
	}

	/**
	 * delete a QuestionLevel by primary key
	 * @param id
	 */
	@DeleteMapping(path = "/delete/{id}")
	@PreAuthorize("hasAnyAuthority('data:delete')")
	public ResponseEntity<ResponseMessage> deleteQuestionLevel(@PathVariable int id) throws Exception {
		businessService.deleteQuestionLevel(id);
		ResponseMessage rb = new ResponseMessage("successfully deleted");
		return ResponseEntity.status(HttpStatus.OK).body(rb);
	}

	private static void logAction(String message) {
    	System.out.println(message);
    	applicationLogger.debug(message);
    }
}
