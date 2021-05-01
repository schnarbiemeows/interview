package com.schnarbiesnmeowers.interview.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schnarbiesnmeowers.interview.dtos.QuestionLevelDTO;
import com.schnarbiesnmeowers.interview.exceptions.ResourceNotFoundException;
import com.schnarbiesnmeowers.interview.pojos.QuestionLevel;
import com.schnarbiesnmeowers.interview.services.QuestionLevelRepository;
/**
 * this class retrieves data from the controller class
 * most business logic should be put in this class
 * @author Dylan I. Kessler
 *
 */
@Component
public class QuestionLevelBusiness {

	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");
    public static final String ID_EQUALS = "id = ";
    public static final String NOT_FOUND = " not found";
	/**
	 * JPA Repository handle
	 */
	@Autowired
	private QuestionLevelRepository service;

	/**
	 * get all QuestionLevel records
	 * @return
	 * @throws Exception
	 */
	public List<QuestionLevelDTO> getAllQuestionLevel() throws Exception {
		Iterable<QuestionLevel> questionlevel = service.findAll();
		Iterator<QuestionLevel> questionlevels = questionlevel.iterator();
		List<QuestionLevelDTO> questionleveldto = new ArrayList();
		while(questionlevels.hasNext()) {
			QuestionLevel item = questionlevels.next();
			questionleveldto.add(item.toDTO());
		}
		return questionleveldto;
	}

	/**
	 * get QuestionLevel by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public QuestionLevelDTO findQuestionLevelById(int id) throws Exception {
		Optional<QuestionLevel> questionlevelOptional = service.findById(id);
		if(questionlevelOptional.isPresent()) {
			QuestionLevel results = questionlevelOptional.get();
			return results.toDTO();
		} else {
			throw new ResourceNotFoundException(ID_EQUALS + id + NOT_FOUND);
		}
	}

	/**
	 * create a new QuestionLevel
	 * @param data
	 * @return
	 */
	public QuestionLevelDTO createQuestionLevel(QuestionLevelDTO data) {
		try {
		    QuestionLevel createdData = data.toEntity();
		    createdData.setEvntTmestmp(new Date());
		    createdData = service.save(createdData);
		    return createdData.toDTO();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * update a QuestionLevel
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public QuestionLevelDTO updateQuestionLevel(QuestionLevelDTO data) throws Exception {
		Optional<QuestionLevel> questionlevelOptional = service.findById(data.getQuestionLevelId());
		if(questionlevelOptional.isPresent()) {
		    QuestionLevel updatedData = data.toEntity();
		    updatedData.setEvntTmestmp(new Date());
			updatedData = service.save(updatedData);
			return updatedData.toDTO();
		} else {
			throw new ResourceNotFoundException(ID_EQUALS + data.getQuestionLevelId() + NOT_FOUND);
		}
	}

	/**
	 * delete a QuestionLevel by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String deleteQuestionLevel(int id) throws Exception {
		Optional<QuestionLevel> questionlevelOptional = service.findById(id);
		if(questionlevelOptional.isPresent()) {
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
