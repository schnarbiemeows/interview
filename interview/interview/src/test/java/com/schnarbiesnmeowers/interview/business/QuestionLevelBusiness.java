package com.schnarbiesnmeowers.interview.business;

import java.util.Optional;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.schnarbiesnmeowers.interview.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import com.schnarbiesnmeowers.interview.dtos.QuestionLevelDTO;
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


	/**
	 * get all QuestionLevel records
	 * @return
	 * @throws Exception
	 */
	public List<QuestionLevelDTO> getAllQuestionLevel() throws Exception {
	    System.out.println("Inside Mock Business Class");
		List<QuestionLevelDTO> questionlevelDTO = new ArrayList<QuestionLevelDTO>();
		return questionlevelDTO;
	}

	/**
	 * get QuestionLevel by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public QuestionLevelDTO findQuestionLevelById(int id) throws Exception {
		return new QuestionLevelDTO();
	}

	/**
	 * create a new QuestionLevel
	 * @param data
	 * @return
	 */
	public QuestionLevelDTO createQuestionLevel(QuestionLevelDTO data) {
        data.setQuestionLevelId(1);
        return data;
	}

	/**
	 * update a QuestionLevel
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public QuestionLevelDTO updateQuestionLevel(QuestionLevelDTO data) throws Exception {
		return data;
	}

	/**
	 * delete a QuestionLevel by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String deleteQuestionLevel(int id) throws Exception {
		return "Successfully Deleted";
	}

}
