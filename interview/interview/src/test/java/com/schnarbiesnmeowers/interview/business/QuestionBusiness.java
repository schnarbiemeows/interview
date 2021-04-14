package com.schnarbiesnmeowers.interview.business;

import java.util.Optional;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.schnarbiesnmeowers.interview.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import com.schnarbiesnmeowers.interview.dtos.QuestionDTO;
import com.schnarbiesnmeowers.interview.pojos.Question;
import com.schnarbiesnmeowers.interview.services.QuestionRepository;

/**
 * this class retrieves data from the controller class
 * most business logic should be put in this class
 * @author Dylan I. Kessler
 *
 */
@Component
public class QuestionBusiness {


	/**
	 * get all Question records
	 * @return
	 * @throws Exception
	 */
	public List<QuestionDTO> getAllQuestion() throws Exception {
	    System.out.println("Inside Mock Business Class");
		List<QuestionDTO> questionDTO = new ArrayList<QuestionDTO>();
		return questionDTO;
	}

	/**
	 * get Question by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public QuestionDTO findQuestionById(int id) throws Exception {
		return new QuestionDTO();
	}

	/**
	 * create a new Question
	 * @param data
	 * @return
	 */
	public QuestionDTO createQuestion(QuestionDTO data) {
        data.setQuestionId(1);
        return data;
	}

	/**
	 * update a Question
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public QuestionDTO updateQuestion(QuestionDTO data) throws Exception {
		return data;
	}

	/**
	 * delete a Question by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String deleteQuestion(int id) throws Exception {
		return "Successfully Deleted";
	}

	/**
	 * get List<QuestionDTO> by foreign key : questionCategoryId
	 * @param questionCategoryId
	 * @return List<Question>
	 * @throws Exception
	*/
	public List<QuestionDTO> findQuestionByQuestionCategoryId(int id) throws Exception {
		List<QuestionDTO> resultsdto = new ArrayList();
		return resultsdto;
	}

	/**
	 * get List<QuestionDTO> by foreign key : questionLevelId
	 * @param questionLevelId
	 * @return List<Question>
	 * @throws Exception
	*/
	public List<QuestionDTO> findQuestionByQuestionLevelId(int id) throws Exception {
		List<QuestionDTO> resultsdto = new ArrayList();
		return resultsdto;
	}

	/**
	 * get List<QuestionDTO> by foreign key : answerId
	 * @param answerId
	 * @return List<Question>
	 * @throws Exception
	*/
	public List<QuestionDTO> findQuestionByAnswerId(int id) throws Exception {
		List<QuestionDTO> resultsdto = new ArrayList();
		return resultsdto;
	}

	/**
	 * get List<QuestionDTO> by foreign key : QuestionCategoryIdAndQuestionLevelIdAndAnswerId
	 * @param QuestionCategoryIdAndQuestionLevelIdAndAnswerId
	 * @return List<Question>
	 * @throws Exception
	*/
	public List<QuestionDTO> findQuestionByQuestionCategoryIdAndQuestionLevelIdAndAnswerId(@PathVariable int id0,@PathVariable int id1,@PathVariable int id2) throws Exception {
		List<QuestionDTO> resultsdto = new ArrayList();
		return resultsdto;
	}

}
