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
@RequestMapping(path="/answer")
public class AnswerController {

	//private static final Logger applicationLogger = LogManager.getLogger("FileAppender");

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
	public ResponseEntity<AnswerDTO> updateAnswer(@Valid @RequestBody AnswerDTO data) throws Exception {
		AnswerDTO updatedData = businessService.updateAnswer(data);
		return ResponseEntity.status(HttpStatus.OK).body(updatedData);
	}

	/**
	 * delete a Answer by primary key
	 * @param id
	 */
	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<ResponseMessage> deleteAnswer(@PathVariable int id) throws Exception {
		businessService.deleteAnswer(id);
		ResponseMessage rb = new ResponseMessage("successfully deleted");
		return ResponseEntity.status(HttpStatus.OK).body(rb);
	}

}
