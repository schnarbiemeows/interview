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
@RequestMapping(path="/interviewuser")
public class InterviewUserController {

	//private static final Logger applicationLogger = LogManager.getLogger("FileAppender");

	/**
	 * JPA Repository handle
	 */
	@Autowired
	private InterviewUserBusiness businessService;;

	/**
	 * get all InterviewUser records
	 * @return Iterable<InterviewUser>
	 */
	@GetMapping(path = "/all")
	public ResponseEntity<List<InterviewUserDTO>> getAllInterviewUser() throws Exception {
		List<InterviewUserDTO> interviewuser = businessService.getAllInterviewUser();
		return ResponseEntity.status(HttpStatus.OK).body(interviewuser);
	}

	/**
	 * get InterviewUser by primary key
	 * @param id
	 * @return InterviewUser
	 */
	@GetMapping(path = "/findById/{id}")
	public ResponseEntity<InterviewUserDTO> findInterviewUserById(@PathVariable int id) throws Exception {
		InterviewUserDTO results = businessService.findInterviewUserById(id);
		return ResponseEntity.status(HttpStatus.OK).body(results);
	}

	/**
	 * create a new InterviewUser
	 * @param InterviewUserDTO
	 * @return InterviewUser
	 */
	@PostMapping(path = "/create")
	public ResponseEntity<InterviewUserDTO> createInterviewUser(@Valid @RequestBody InterviewUserDTO data) throws Exception {
		try {
		    InterviewUserDTO createdData = businessService.createInterviewUser(data);
		    return ResponseEntity.status(HttpStatus.CREATED).body(createdData);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * update a InterviewUser
	 * @param InterviewUserDTO
	 * @return InterviewUser
	 */
	@PostMapping(path = "/update")
	public ResponseEntity<InterviewUserDTO> updateInterviewUser(@Valid @RequestBody InterviewUserDTO data) throws Exception {
		InterviewUserDTO updatedData = businessService.updateInterviewUser(data);
		return ResponseEntity.status(HttpStatus.OK).body(updatedData);
	}

	/**
	 * delete a InterviewUser by primary key
	 * @param id
	 */
	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<ResponseMessage> deleteInterviewUser(@PathVariable int id) throws Exception {
		businessService.deleteInterviewUser(id);
		ResponseMessage rb = new ResponseMessage("successfully deleted");
		return ResponseEntity.status(HttpStatus.OK).body(rb);
	}

}
