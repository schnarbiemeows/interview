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
import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTO;
import com.schnarbiesnmeowers.interview.pojos.InterviewUser;
import com.schnarbiesnmeowers.interview.services.InterviewUserRepository;
import java.util.List;
/**
 * this class retrieves data from the controller class
 * most business logic should be put in this class
 * @author Dylan I. Kessler
 *
 */
@Component
public class InterviewUserBusiness {

	//private static final Logger applicationLogger = LogManager.getLogger("FileAppender");
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

}
