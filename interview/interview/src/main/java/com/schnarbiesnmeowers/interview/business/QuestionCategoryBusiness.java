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

import com.schnarbiesnmeowers.interview.dtos.QuestionCategoryDTO;
import com.schnarbiesnmeowers.interview.exceptions.ResourceNotFoundException;
import com.schnarbiesnmeowers.interview.pojos.QuestionCategory;
import com.schnarbiesnmeowers.interview.services.QuestionCategoryRepository;
/**
 * this class retrieves data from the controller class
 * most business logic should be put in this class
 * @author Dylan I. Kessler
 *
 */
@Component
public class QuestionCategoryBusiness {

	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");
    public static final String ID_EQUALS = "id = ";
    public static final String NOT_FOUND = " not found";
	/**
	 * JPA Repository handle
	 */
	@Autowired
	private QuestionCategoryRepository service;

	/**
	 * get all QuestionCategory records
	 * @return
	 * @throws Exception
	 */
	public List<QuestionCategoryDTO> getAllQuestionCategory() throws Exception {
		Iterable<QuestionCategory> questioncategory = service.findAll();
		Iterator<QuestionCategory> questioncategorys = questioncategory.iterator();
		List<QuestionCategoryDTO> questioncategorydto = new ArrayList();
		while(questioncategorys.hasNext()) {
			QuestionCategory item = questioncategorys.next();
			questioncategorydto.add(item.toDTO());
		}
		return questioncategorydto;
	}

	/**
	 * get QuestionCategory by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public QuestionCategoryDTO findQuestionCategoryById(int id) throws Exception {
		Optional<QuestionCategory> questioncategoryOptional = service.findById(id);
		if(questioncategoryOptional.isPresent()) {
			QuestionCategory results = questioncategoryOptional.get();
			return results.toDTO();
		} else {
			throw new ResourceNotFoundException(ID_EQUALS + id + NOT_FOUND);
		}
	}

	/**
	 * create a new QuestionCategory
	 * @param data
	 * @return
	 */
	public QuestionCategoryDTO createQuestionCategory(QuestionCategoryDTO data) {
		try {
		    QuestionCategory createdData = data.toEntity();
		    createdData.setEvntTmestmp(new Date());
		    createdData = service.save(createdData);
		    return createdData.toDTO();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * update a QuestionCategory
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public QuestionCategoryDTO updateQuestionCategory(QuestionCategoryDTO data) throws Exception {
		Optional<QuestionCategory> questioncategoryOptional = service.findById(data.getQuestionCategoryId());
		if(questioncategoryOptional.isPresent()) {
		    QuestionCategory updatedData = data.toEntity();
		    updatedData.setEvntTmestmp(new Date());
			updatedData = service.save(updatedData);
			return updatedData.toDTO();
		} else {
			throw new ResourceNotFoundException(ID_EQUALS + data.getQuestionCategoryId() + NOT_FOUND);
		}
	}

	/**
	 * delete a QuestionCategory by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String deleteQuestionCategory(int id) throws Exception {
		Optional<QuestionCategory> questioncategoryOptional = service.findById(id);
		if(questioncategoryOptional.isPresent()) {
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
