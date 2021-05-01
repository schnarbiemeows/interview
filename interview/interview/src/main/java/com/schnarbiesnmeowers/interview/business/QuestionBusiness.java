package com.schnarbiesnmeowers.interview.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.schnarbiesnmeowers.interview.dtos.QuestionAnswerItemDTO;
import com.schnarbiesnmeowers.interview.dtos.QuestionDTO;
import com.schnarbiesnmeowers.interview.exceptions.ResourceNotFoundException;
import com.schnarbiesnmeowers.interview.pojos.Answer;
import com.schnarbiesnmeowers.interview.pojos.Question;
import com.schnarbiesnmeowers.interview.services.AnswerRepository;
import com.schnarbiesnmeowers.interview.services.QuestionRepository;
/**
 * this class retrieves data from the controller class
 * most business logic should be put in this class
 * @author Dylan I. Kessler
 *
 */
@Component
public class QuestionBusiness {

	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");
    public static final String ID_EQUALS = "id = ";
    public static final String NOT_FOUND = " not found";
	/**
	 * JPA Repository handle
	 */
	@Autowired
	private QuestionRepository service;
	
	/**
	 * JPA Repository handle
	 */
	@Autowired
	private AnswerRepository answerService;

	/**
	 * get all Question records
	 * @return
	 * @throws Exception
	 */
	public List<QuestionDTO> getAllQuestion() throws Exception {
		Iterable<Question> question = service.findAll();
		Iterator<Question> questions = question.iterator();
		List<QuestionDTO> questiondto = new ArrayList();
		while(questions.hasNext()) {
			Question item = questions.next();
			questiondto.add(item.toDTO());
		}
		return questiondto;
	}

	/**
	 * get Question by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public QuestionDTO findQuestionById(int id) throws Exception {
		Optional<Question> questionOptional = service.findById(id);
		if(questionOptional.isPresent()) {
			Question results = questionOptional.get();
			return results.toDTO();
		} else {
			throw new ResourceNotFoundException(ID_EQUALS + id + NOT_FOUND);
		}
	}

	/**
	 * create a new Question
	 * @param data
	 * @return
	 */
	public QuestionDTO createQuestion(QuestionDTO data) {
		try {
		    Question createdData = data.toEntity();
		    createdData = service.save(createdData);
		    return createdData.toDTO();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * update a Question
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public QuestionDTO updateQuestion(QuestionDTO data) throws Exception {
		Optional<Question> questionOptional = service.findById(data.getQuestionId());
		if(questionOptional.isPresent()) {
		    Question updatedData = data.toEntity();
			updatedData = service.save(updatedData);
			return updatedData.toDTO();
		} else {
			throw new ResourceNotFoundException(ID_EQUALS + data.getQuestionId() + NOT_FOUND);
		}
	}

	/**
	 * delete a Question by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String deleteQuestion(int id) throws Exception {
		Optional<Question> questionOptional = service.findById(id);
		if(questionOptional.isPresent()) {
			service.deleteById(id);
			return "Successfully Deleted";
		} else {
			throw new ResourceNotFoundException(ID_EQUALS + id + NOT_FOUND);
		}
	}

	/**
	 * get List<QuestionDTO> by foreign key : questionCategoryId
	 * @param questionCategoryId
	 * @return List<Question>
	 * @throws Exception
	*/
	public List<QuestionDTO> findQuestionByQuestionCategoryId(int id) throws Exception {
		Iterable<Question> results = service.findQuestionByQuestionCategoryId(id);
		Iterator<Question> iter = results.iterator();
		List<QuestionDTO> resultsdto = new ArrayList();
		while(iter.hasNext()) {
			Question item = iter.next();
			resultsdto.add(item.toDTO());
		}
		return resultsdto;
	}

	/**
	 * get List<QuestionDTO> by foreign key : questionLevelId
	 * @param questionLevelId
	 * @return List<Question>
	 * @throws Exception
	*/
	public List<QuestionDTO> findQuestionByQuestionLevelId(int id) throws Exception {
		Iterable<Question> results = service.findQuestionByQuestionLevelId(id);
		Iterator<Question> iter = results.iterator();
		List<QuestionDTO> resultsdto = new ArrayList();
		while(iter.hasNext()) {
			Question item = iter.next();
			resultsdto.add(item.toDTO());
		}
		return resultsdto;
	}

	/**
	 * get List<QuestionDTO> by foreign key : answerId
	 * @param answerId
	 * @return List<Question>
	 * @throws Exception
	*/
	public List<QuestionDTO> findQuestionByAnswerId(int id) throws Exception {
		Iterable<Question> results = service.findQuestionByAnswerId(id);
		Iterator<Question> iter = results.iterator();
		List<QuestionDTO> resultsdto = new ArrayList();
		while(iter.hasNext()) {
			Question item = iter.next();
			resultsdto.add(item.toDTO());
		}
		return resultsdto;
	}

	/**
	 * get List<QuestionDTO> by foreign key : QuestionCategoryIdAndQuestionLevelIdAndAnswerId
	 * @param QuestionCategoryIdAndQuestionLevelIdAndAnswerId
	 * @return List<Question>
	 * @throws Exception
	*/
	public List<QuestionDTO> findQuestionByQuestionCategoryIdAndQuestionLevelIdAndAnswerId(@PathVariable int id0,@PathVariable int id1,@PathVariable int id2) throws Exception {
		Iterable<Question> results = service.findQuestionByQuestionCategoryIdAndQuestionLevelIdAndAnswerId(id0, id1, id2);
		Iterator<Question> iter = results.iterator();
		List<QuestionDTO> resultsdto = new ArrayList();
		while(iter.hasNext()) {
			Question item = iter.next();
			resultsdto.add(item.toDTO());
		}
		return resultsdto;
	}

	private static void logAction(String message) {
    	System.out.println(message);
    	applicationLogger.debug(message);
    }

	/**
	 * 
	 * @param data
	 * @return
	 */
	public QuestionAnswerItemDTO createQuestionAnswerPair(@Valid QuestionAnswerItemDTO data) throws Exception {
		Answer answer = new Answer();
		answer.setAnswerTxt(data.getAnswerTxt());
		answer.setEvntOperId(data.getEvntOperId());
		answer.setEvntTmestmp(new Date());
		answer = answerService.save(answer);
	    Question question = new Question();
	    question.setAnswerId(answer.getAnswerId());
	    question.setEvntOperId(data.getEvntOperId());
	    question.setEvntTmestmp(new Date());
	    question.setQuestionCategoryId(data.getQuestionCategoryId());
	    question.setQuestionLevelId(data.getQuestionLevelId());
	    question.setQuestionTxt(data.getQuestionTxt());
	    question = service.save(question);
	    
	    data.setAnswerId(answer.getAnswerId());
	    data.setQuestionId(question.getQuestionId());
	    return data;
	}

	public QuestionAnswerItemDTO updateQuestionAnswerPair(@Valid QuestionAnswerItemDTO data) throws Exception {
		Answer answer = answerService.getOne(data.getAnswerId());
		answer.setAnswerTxt(data.getAnswerTxt());
		answer.setEvntTmestmp(new Date());
		answer.setEvntOperId(data.getEvntOperId());
		answer = answerService.save(answer);
		Question question = service.getOne(data.getQuestionId());
		question.setQuestionCategoryId(data.getQuestionCategoryId());
	    question.setQuestionLevelId(data.getQuestionLevelId());
	    question.setQuestionTxt(data.getQuestionTxt());
	    question.setAnswerId(answer.getAnswerId());
	    question.setEvntOperId(data.getEvntOperId());
	    question.setEvntTmestmp(new Date());
	    question = service.save(question);
		return data;
	}
}
