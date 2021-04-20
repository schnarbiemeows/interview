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

import com.schnarbiesnmeowers.interview.business.AnswerBusiness;
import com.schnarbiesnmeowers.interview.dtos.AnswerDTO;
import com.schnarbiesnmeowers.interview.exceptions.handler.AnswerExceptionHandling;
import com.schnarbiesnmeowers.interview.pojos.ResponseMessage;

/**
 * this class is the main REST controller
 * @author Dylan I. Kessler
 *
 */
@CrossOrigin
@RestController
@RequestMapping(path="/answer")
public class AnswerController extends AnswerExceptionHandling {

	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");

	/**
	 * JPA Repository handle
	 */
	@Autowired
	private AnswerBusiness businessService;;

	/**
	 * get all Answer records
	 * @return Iterable<Answer>
	 */
	@GetMapping(path = "/all")
	@PreAuthorize("hasAnyAuthority('data:select')")
	public ResponseEntity<List<AnswerDTO>> getAllAnswer() throws Exception {
		List<AnswerDTO> answer = businessService.getAllAnswer();
		return ResponseEntity.status(HttpStatus.OK).body(answer);
	}

	/**
	 * get Answer by primary key
	 * @param id
	 * @return Answer
	 */
	@GetMapping(path = "/findById/{id}")
	@PreAuthorize("hasAnyAuthority('data:select')")
	public ResponseEntity<AnswerDTO> findAnswerById(@PathVariable int id) throws Exception {
		AnswerDTO results = businessService.findAnswerById(id);
		return ResponseEntity.status(HttpStatus.OK).body(results);
	}

	/**
	 * create a new Answer
	 * @param AnswerDTO
	 * @return Answer
	 */
	@PostMapping(path = "/create")
	@PreAuthorize("hasAnyAuthority('data:create')")
	public ResponseEntity<AnswerDTO> createAnswer(@Valid @RequestBody AnswerDTO data) throws Exception {
		try {
		    AnswerDTO createdData = businessService.createAnswer(data);
		    return ResponseEntity.status(HttpStatus.CREATED).body(createdData);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * update a Answer
	 * @param AnswerDTO
	 * @return Answer
	 */
	@PostMapping(path = "/update")
	@PreAuthorize("hasAnyAuthority('data:update')")
	public ResponseEntity<AnswerDTO> updateAnswer(@Valid @RequestBody AnswerDTO data) throws Exception {
		AnswerDTO updatedData = businessService.updateAnswer(data);
		return ResponseEntity.status(HttpStatus.OK).body(updatedData);
	}

	/**
	 * delete a Answer by primary key
	 * @param id
	 */
	@DeleteMapping(path = "/delete/{id}")
	@PreAuthorize("hasAnyAuthority('data:delete')")
	public ResponseEntity<ResponseMessage> deleteAnswer(@PathVariable int id) throws Exception {
		businessService.deleteAnswer(id);
		ResponseMessage rb = new ResponseMessage("successfully deleted");
		return ResponseEntity.status(HttpStatus.OK).body(rb);
	}

	private static void logAction(String message) {
    	System.out.println(message);
    	applicationLogger.debug(message);
    }
}
