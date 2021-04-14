package com.schnarbiesnmeowers.interview.business;

import java.util.Optional;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.schnarbiesnmeowers.interview.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTO;
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


	/**
	 * get all InterviewUser records
	 * @return
	 * @throws Exception
	 */
	public List<InterviewUserDTO> getAllInterviewUser() throws Exception {
	    System.out.println("Inside Mock Business Class");
		List<InterviewUserDTO> interviewuserDTO = new ArrayList<InterviewUserDTO>();
		return interviewuserDTO;
	}

	/**
	 * get InterviewUser by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public InterviewUserDTO findInterviewUserById(int id) throws Exception {
		return new InterviewUserDTO();
	}

	/**
	 * create a new InterviewUser
	 * @param data
	 * @return
	 */
	public InterviewUserDTO createInterviewUser(InterviewUserDTO data) {
        data.setUserId(1);
        return data;
	}

	/**
	 * update a InterviewUser
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public InterviewUserDTO updateInterviewUser(InterviewUserDTO data) throws Exception {
		return data;
	}

	/**
	 * delete a InterviewUser by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String deleteInterviewUser(int id) throws Exception {
		return "Successfully Deleted";
	}

}
