package com.schnarbiesnmeowers.interview.business;

import java.util.Optional;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.schnarbiesnmeowers.interview.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import com.schnarbiesnmeowers.interview.dtos.QuestionCategoryDTO;
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


	/**
	 * get all QuestionCategory records
	 * @return
	 * @throws Exception
	 */
	public List<QuestionCategoryDTO> getAllQuestionCategory() throws Exception {
	    System.out.println("Inside Mock Business Class");
		List<QuestionCategoryDTO> questioncategoryDTO = new ArrayList<QuestionCategoryDTO>();
		return questioncategoryDTO;
	}

	/**
	 * get QuestionCategory by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public QuestionCategoryDTO findQuestionCategoryById(int id) throws Exception {
		return new QuestionCategoryDTO();
	}

	/**
	 * create a new QuestionCategory
	 * @param data
	 * @return
	 */
	public QuestionCategoryDTO createQuestionCategory(QuestionCategoryDTO data) {
        data.setQuestionCategoryId(1);
        return data;
	}

	/**
	 * update a QuestionCategory
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public QuestionCategoryDTO updateQuestionCategory(QuestionCategoryDTO data) throws Exception {
		return data;
	}

	/**
	 * delete a QuestionCategory by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String deleteQuestionCategory(int id) throws Exception {
		return "Successfully Deleted";
	}

}
