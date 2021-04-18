package com.schnarbiesnmeowers.interview.business;

import java.util.Optional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.schnarbiesnmeowers.interview.exceptions.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.schnarbiesnmeowers.interview.dtos.AnswerDTO;
import com.schnarbiesnmeowers.interview.pojos.Answer;
import com.schnarbiesnmeowers.interview.services.AnswerRepository;
import java.util.List;
/**
 * this class retrieves data from the controller class
 * most business logic should be put in this class
 * @author Dylan I. Kessler
 *
 */
@Component
public class AnswerBusiness {

	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");
    public static final String ID_EQUALS = "id = ";
    public static final String NOT_FOUND = " not found";
	/**
	 * JPA Repository handle
	 */
	@Autowired
	private AnswerRepository service;

	/**
	 * get all Answer records
	 * @return
	 * @throws Exception
	 */
	public List<AnswerDTO> getAllAnswer() throws Exception {
		Iterable<Answer> answer = service.findAll();
		Iterator<Answer> answers = answer.iterator();
		List<AnswerDTO> answerdto = new ArrayList();
		while(answers.hasNext()) {
			Answer item = answers.next();
			answerdto.add(item.toDTO());
		}
		return answerdto;
	}

	/**
	 * get Answer by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public AnswerDTO findAnswerById(int id) throws Exception {
		Optional<Answer> answerOptional = service.findById(id);
		if(answerOptional.isPresent()) {
			Answer results = answerOptional.get();
			return results.toDTO();
		} else {
			throw new ResourceNotFoundException(ID_EQUALS + id + NOT_FOUND);
		}
	}

	/**
	 * create a new Answer
	 * @param data
	 * @return
	 */
	public AnswerDTO createAnswer(AnswerDTO data) {
		try {
		    Answer createdData = data.toEntity();
		    createdData = service.save(createdData);
		    return createdData.toDTO();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * update a Answer
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public AnswerDTO updateAnswer(AnswerDTO data) throws Exception {
		Optional<Answer> answerOptional = service.findById(data.getAnswerId());
		if(answerOptional.isPresent()) {
		    Answer updatedData = data.toEntity();
			updatedData = service.save(updatedData);
			return updatedData.toDTO();
		} else {
			throw new ResourceNotFoundException(ID_EQUALS + data.getAnswerId() + NOT_FOUND);
		}
	}

	/**
	 * delete a Answer by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String deleteAnswer(int id) throws Exception {
		Optional<Answer> answerOptional = service.findById(id);
		if(answerOptional.isPresent()) {
			service.deleteById(id);
			return "Successfully Deleted";
		} else {
			throw new ResourceNotFoundException(ID_EQUALS + id + NOT_FOUND);
		}
	}

	private static void logAction(String message) {
    	System.out.println(message);
    	applicationLogger.debug(message);
    }
}
