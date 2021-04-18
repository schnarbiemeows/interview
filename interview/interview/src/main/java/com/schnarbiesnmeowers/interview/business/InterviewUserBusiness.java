package com.schnarbiesnmeowers.interview.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTO;
import com.schnarbiesnmeowers.interview.exceptions.ResourceNotFoundException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UserFieldsNotValidException;
import com.schnarbiesnmeowers.interview.pojos.InterviewUser;
import com.schnarbiesnmeowers.interview.services.InterviewUserRepository;
/**
 * this class retrieves data from the controller class
 * most business logic should be put in this class
 * @author Dylan I. Kessler
 *
 */
@Component
public class InterviewUserBusiness {

	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");
    public static final String ID_EQUALS = "id = ";
    public static final String NOT_FOUND = " not found";
	/**
	 * JPA Repository handle
	 */
	@Autowired
	private InterviewUserRepository service;

	/**
	 * get all InterviewUser records
	 * @return
	 * @throws Exception
	 */
	public List<InterviewUserDTO> getAllInterviewUser() throws Exception {
		Iterable<InterviewUser> interviewuser = service.findAll();
		Iterator<InterviewUser> interviewusers = interviewuser.iterator();
		List<InterviewUserDTO> interviewuserdto = new ArrayList();
		while(interviewusers.hasNext()) {
			InterviewUser item = interviewusers.next();
			interviewuserdto.add(item.toDTO());
		}
		return interviewuserdto;
	}

	/**
	 * get InterviewUser by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public InterviewUserDTO findInterviewUserById(int id) throws Exception {
		Optional<InterviewUser> interviewuserOptional = service.findById(id);
		if(interviewuserOptional.isPresent()) {
			InterviewUser results = interviewuserOptional.get();
			return results.toDTO();
		} else {
			throw new ResourceNotFoundException(ID_EQUALS + id + NOT_FOUND);
		}
	}

	/**
	 * create a new InterviewUser
	 * @param data
	 * @return
	 */
	public InterviewUserDTO createInterviewUser(InterviewUserDTO data) {
		try {
		    InterviewUser createdData = data.toEntity();
		    createdData = service.save(createdData);
		    return createdData.toDTO();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * update a InterviewUser
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public InterviewUserDTO updateInterviewUser(InterviewUserDTO data) throws Exception {
		Optional<InterviewUser> interviewuserOptional = service.findById(data.getUserId());
		if(interviewuserOptional.isPresent()) {
		    InterviewUser updatedData = data.toEntity();
			updatedData = service.save(updatedData);
			return updatedData.toDTO();
		} else {
			throw new ResourceNotFoundException(ID_EQUALS + data.getUserId() + NOT_FOUND);
		}
	}

	/**
	 * delete a InterviewUser by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String deleteInterviewUser(int id) throws Exception {
		Optional<InterviewUser> interviewuserOptional = service.findById(id);
		if(interviewuserOptional.isPresent()) {
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

	/**
	 * this method checks to make sure the restration data that a new user has entered is valid
	 * @param user
	 * @throws UserFieldsNotValidException
	 */
	public void validateFields(InterviewUserDTO user) throws UserFieldsNotValidException {
		if(user.getUserName()==null||user.getUserName().isEmpty()) {
			throw new UserFieldsNotValidException("username must have a value");
		}
		if(user.getEmailAddr()==null||user.getEmailAddr().isEmpty()) {
			throw new UserFieldsNotValidException("email must have a value");
		}
		if(user.getFirstName()==null||user.getFirstName().isEmpty()) {
			throw new UserFieldsNotValidException("first name must have a value");
		}
		if(user.getLastName()==null||user.getLastName().isEmpty()) {
			throw new UserFieldsNotValidException("last name must have a value");
		}
	}

}
