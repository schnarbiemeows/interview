package com.schnarbiesnmeowers.interview.business;

import java.util.Optional;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.schnarbiesnmeowers.interview.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import com.schnarbiesnmeowers.interview.dtos.AnswerDTO;
import com.schnarbiesnmeowers.interview.pojos.Answer;
import com.schnarbiesnmeowers.interview.services.AnswerRepository;

/**
 * this class retrieves data from the controller class
 * most business logic should be put in this class
 * @author Dylan I. Kessler
 *
 */
@Component
public class AnswerBusiness {


	/**
	 * get all Answer records
	 * @return
	 * @throws Exception
	 */
	public List<AnswerDTO> getAllAnswer() throws Exception {
	    System.out.println("Inside Mock Business Class");
		List<AnswerDTO> answerDTO = new ArrayList<AnswerDTO>();
		return answerDTO;
	}

	/**
	 * get Answer by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public AnswerDTO findAnswerById(int id) throws Exception {
		return new AnswerDTO();
	}

	/**
	 * create a new Answer
	 * @param data
	 * @return
	 */
	public AnswerDTO createAnswer(AnswerDTO data) {
        data.setAnswerId(1);
        return data;
	}

	/**
	 * update a Answer
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public AnswerDTO updateAnswer(AnswerDTO data) throws Exception {
		return data;
	}

	/**
	 * delete a Answer by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String deleteAnswer(int id) throws Exception {
		return "Successfully Deleted";
	}

}
