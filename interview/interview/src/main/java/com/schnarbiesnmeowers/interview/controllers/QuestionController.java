package com.schnarbiesnmeowers.interview.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
//@CrossOrigin
@RestController
@RequestMapping(path="/question")
public class QuestionController {

	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");

	/**
	 * JPA Repository handle
	 */
	@Autowired
	private QuestionBusiness businessService;;

	/**
	 * get all Question records
	 * @return Iterable<Question>
	 */
	@GetMapping(path = "/all")
	public ResponseEntity<List<QuestionDTO>> getAllQuestion() throws Exception {
		List<QuestionDTO> question = businessService.getAllQuestion();
		return ResponseEntity.status(HttpStatus.OK).body(question);
	}

	/**
	 * get Question by primary key
	 * @param id
	 * @return Question
	 */
	@GetMapping(path = "/findById/{id}")
	@PreAuthorize("hasAnyAuthority('data:select')")
	public ResponseEntity<QuestionDTO> findQuestionById(@PathVariable int id) throws Exception {
		QuestionDTO results = businessService.findQuestionById(id);
		return ResponseEntity.status(HttpStatus.OK).body(results);
	}

	/**
	 * create a new Question
	 * @param QuestionDTO
	 * @return Question
	 */
	@PostMapping(path = "/create")
	@PreAuthorize("hasAnyAuthority('data:create')")
	public ResponseEntity<QuestionDTO> createQuestion(@Valid @RequestBody QuestionDTO data) throws Exception {
		try {
		    QuestionDTO createdData = businessService.createQuestion(data);
		    return ResponseEntity.status(HttpStatus.CREATED).body(createdData);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * create a new Question
	 * @param QuestionDTO
	 * @return Question
	 */
	@PostMapping(path = "/createpair")
	@PreAuthorize("hasAnyAuthority('data:create')")
	public ResponseEntity<QuestionAnswerItemDTO> createQuestionAnswerPair(@Valid @RequestBody QuestionAnswerItemDTO data) throws Exception {
		try {
			QuestionAnswerItemDTO createdData = businessService.createQuestionAnswerPair(data);
		    return ResponseEntity.status(HttpStatus.CREATED).body(createdData);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * update a Question
	 * @param QuestionDTO
	 * @return Question
	 */
	@PostMapping(path = "/update")
	@PreAuthorize("hasAnyAuthority('data:update')")
	public ResponseEntity<QuestionDTO> updateQuestion(@Valid @RequestBody QuestionDTO data) throws Exception {
		QuestionDTO updatedData = businessService.updateQuestion(data);
		return ResponseEntity.status(HttpStatus.OK).body(updatedData);
	}

	/**
	 * update a Question
	 * @param QuestionDTO
	 * @return Question
	 */
	@PostMapping(path = "/updatepair")
	@PreAuthorize("hasAnyAuthority('data:update')")
	public ResponseEntity<QuestionAnswerItemDTO> updateQuestionAnswerPair(@Valid @RequestBody QuestionAnswerItemDTO data) throws Exception {
		QuestionAnswerItemDTO updatedData = businessService.updateQuestionAnswerPair(data);
		return ResponseEntity.status(HttpStatus.OK).body(updatedData);
	}
	
	/**
	 * delete a Question by primary key
	 * @param id
	 */
	@DeleteMapping(path = "/delete/{id}")
	@PreAuthorize("hasAnyAuthority('data:delete')")
	public ResponseEntity<ResponseMessage> deleteQuestion(@PathVariable int id) throws Exception {
		businessService.deleteQuestion(id);
		ResponseMessage rb = new ResponseMessage("successfully deleted");
		return ResponseEntity.status(HttpStatus.OK).body(rb);
	}

	/**
	 * get List<QuestionDTO> by foreign key : questionCategoryId
	 * @param questionCategoryId
	 * @return List<Question>
	 * @throws Exception
	*/
	@GetMapping(path = "/findByQuestionCategoryId/{id}")
	@PreAuthorize("hasAnyAuthority('data:select')")
	public ResponseEntity<List<QuestionDTO>> findQuestionByQuestionCategoryId(@PathVariable int id) throws Exception {
		List<QuestionDTO> results = businessService.findQuestionByQuestionCategoryId(id);
		return ResponseEntity.status(HttpStatus.OK).body(results);
	}

	/**
	 * get List<QuestionDTO> by foreign key : questionLevelId
	 * @param questionLevelId
	 * @return List<Question>
	 * @throws Exception
	*/
	@GetMapping(path = "/findByQuestionLevelId/{id}")
	@PreAuthorize("hasAnyAuthority('data:select')")
	public ResponseEntity<List<QuestionDTO>> findQuestionByQuestionLevelId(@PathVariable int id) throws Exception {
		List<QuestionDTO> results = businessService.findQuestionByQuestionLevelId(id);
		return ResponseEntity.status(HttpStatus.OK).body(results);
	}

	/**
	 * get List<QuestionDTO> by foreign key : answerId
	 * @param answerId
	 * @return List<Question>
	 * @throws Exception
	*/
	@GetMapping(path = "/findByAnswerId/{id}")
	@PreAuthorize("hasAnyAuthority('data:select')")
	public ResponseEntity<List<QuestionDTO>> findQuestionByAnswerId(@PathVariable int id) throws Exception {
		List<QuestionDTO> results = businessService.findQuestionByAnswerId(id);
		return ResponseEntity.status(HttpStatus.OK).body(results);
	}

	/**
	 * get List<QuestionDTO> by foreign key : QuestionCategoryIdAndQuestionLevelIdAndAnswerId
	 * @param QuestionCategoryIdAndQuestionLevelIdAndAnswerId
	 * @return List<Question>
	 * @throws Exception
	*/
	@GetMapping(path = "/findByQuestionCategoryIdAndQuestionLevelIdAndAnswerId/{id0}/{id1}/{id2}")
	@PreAuthorize("hasAnyAuthority('data:select')")
	public ResponseEntity<List<QuestionDTO>> findQuestionByQuestionCategoryIdAndQuestionLevelIdAndAnswerId(@PathVariable int id0, @PathVariable int id1, @PathVariable int id2) throws Exception {
		List<QuestionDTO> results = businessService.findQuestionByQuestionCategoryIdAndQuestionLevelIdAndAnswerId(id0, id1, id2);
		return ResponseEntity.status(HttpStatus.OK).body(results);
	}
	
	/**
	 * get all Question records
	 * @return Iterable<Question>
	 */
	@GetMapping(path = "/totals")
	public ResponseEntity<List<QuestionTotalsDTO>> getAllQuestionTotals() throws Exception {
		List<QuestionTotalsDTO> question = businessService.getAllQuestionTotals();
		return ResponseEntity.status(HttpStatus.OK).body(question);
	}
	

	private static void logAction(String message) {
    	System.out.println(message);
    	applicationLogger.debug(message);
    }
}
