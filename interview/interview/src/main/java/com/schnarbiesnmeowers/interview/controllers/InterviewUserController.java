package com.schnarbiesnmeowers.interview.controllers;

import static com.schnarbiesnmeowers.interview.utilities.Constants.JWT_TOKEN_HEADER;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schnarbiesnmeowers.interview.business.InterviewUserBusiness;
import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTO;
import com.schnarbiesnmeowers.interview.exceptions.handler.InterviewUserExceptionHandling;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.EmailExistsException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UserFieldsNotValidException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UserNotFoundException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UsernameExistsException;
import com.schnarbiesnmeowers.interview.pojos.InterviewUser;
import com.schnarbiesnmeowers.interview.pojos.ResponseMessage;
import com.schnarbiesnmeowers.interview.security.JwtTokenProvider;
import com.schnarbiesnmeowers.interview.security.UserPrincipal;
import com.schnarbiesnmeowers.interview.services.UserService;
import com.schnarbiesnmeowers.interview.utilities.Constants;

/**
 * this class is the main REST controller
 * @author Dylan I. Kessler
 *
 */
@CrossOrigin
@RestController
@RequestMapping(path="/interviewuser")
public class InterviewUserController extends InterviewUserExceptionHandling {

	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");

	
	
	private InterviewUserBusiness businessService;;
	private UserService userService;
	private AuthenticationManager authManager;
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	public InterviewUserController(InterviewUserBusiness businessService, UserService userService,
			AuthenticationManager authManager, JwtTokenProvider jwtTokenProvider) {
		super();
		this.businessService = businessService;
		this.userService = userService;
		this.authManager = authManager;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	/**
	 * TODO - there are a number of things wrong with this method:
	 * 1 - makes no check to see if the fields are null or blank; will put record in DB
	 * 2 - makes no email validation; will put record with invalid email in the DB, then fails when sending the email
	 */
	/**
	 * this method allows the user to register an account
	 * @param user
	 * @return ResponseEntity<InterviewUserDTO>
	 * @throws UserNotFoundException
	 * @throws UsernameExistsException
	 * @throws EmailExistsException
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws UserFieldsNotValidException 
	 */
	@PostMapping(path = "/register")
	public ResponseEntity<InterviewUserDTO> register(@RequestBody InterviewUserDTO user) throws UserNotFoundException, UsernameExistsException,
			EmailExistsException, AddressException, MessagingException, UserFieldsNotValidException {
		businessService.validateFields(user);
		InterviewUser newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUserName(),
				user.getEmailAddr());
		return new ResponseEntity<>(newUser.toDTO(), HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 * @throws UserNotFoundException
	 * @throws UsernameExistsException
	 * @throws EmailExistsException
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws UserFieldsNotValidException
	 */
	 @PostMapping(path = "/setpwd") public ResponseEntity<InterviewUserDTO> setPassword(@RequestBody InterviewUserDTO user) throws UserNotFoundException,
	 UsernameExistsException, EmailExistsException, AddressException,MessagingException, UserFieldsNotValidException {
		 userService.setPassword(user.getUserName(), user.getPassword()); 
		 return new ResponseEntity<>(user, HttpStatus.OK); 
	 }
	 
	
	/*
	 * @PostMapping(path = "/setrole") public ResponseEntity<InterviewUserDTO>
	 * setRole(@RequestBody InterviewUserDTO user) throws UserNotFoundException,
	 * UsernameExistsException, EmailExistsException, AddressException,
	 * MessagingException, UserFieldsNotValidException {
	 * userService.setRole(user.getUserName()); return new ResponseEntity<>(user,
	 * HttpStatus.OK); }
	 */
	/**
	 * 
	 * @param user
	 * @return
	 * @throws UserNotFoundException
	 * @throws UsernameExistsException
	 * @throws EmailExistsException
	 */
	@PostMapping(path = "/login")
	public ResponseEntity<InterviewUserDTO> login(@RequestBody InterviewUserDTO user)
			throws UserNotFoundException, UsernameExistsException, EmailExistsException {
		authenticate(user.getUserName(), user.getPassword());
		InterviewUserDTO loggedInUser = userService.findUserByUsername(user.getUserName()).toDTO();
		UserPrincipal loggedInUserPrincipal = new UserPrincipal(loggedInUser);
		HttpHeaders jwtHeader = getJwtHeader(loggedInUserPrincipal);
		return new ResponseEntity<>(loggedInUser, jwtHeader, HttpStatus.OK);
	}
	
	/**
	 * get all InterviewUser records
	 * @return Iterable<InterviewUser>
	 */
	@GetMapping(path = "/all")
	@PreAuthorize("hasAnyAuthority('admin:select')")
	public ResponseEntity<List<InterviewUserDTO>> getAllInterviewUser() throws Exception {
		List<InterviewUserDTO> interviewuser = businessService.getAllInterviewUser();
		return ResponseEntity.status(HttpStatus.OK).body(interviewuser);
	}

	/**
	 * get InterviewUser by primary key
	 * @param id
	 * @return InterviewUser
	 */
	@GetMapping(path = "/findById/{id}")
	@PreAuthorize("hasAnyAuthority('admin:select')")
	public ResponseEntity<InterviewUserDTO> findInterviewUserById(@PathVariable int id) throws Exception {
		InterviewUserDTO results = businessService.findInterviewUserById(id);
		return ResponseEntity.status(HttpStatus.OK).body(results);
	}

	/**
	 * create a new InterviewUser
	 * @param InterviewUserDTO
	 * @return InterviewUser
	 */
	@PostMapping(path = "/create")
	@PreAuthorize("hasAnyAuthority('admin:create')")
	public ResponseEntity<InterviewUserDTO> createInterviewUser(@Valid @RequestBody InterviewUserDTO data) throws Exception {
		try {
		    InterviewUserDTO createdData = businessService.createInterviewUser(data);
		    return ResponseEntity.status(HttpStatus.CREATED).body(createdData);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * update a InterviewUser
	 * @param InterviewUserDTO
	 * @return InterviewUser
	 */
	@PostMapping(path = "/update")
	@PreAuthorize("hasAnyAuthority('admin:update')")
	public ResponseEntity<InterviewUserDTO> updateInterviewUser(@Valid @RequestBody InterviewUserDTO data) throws Exception {
		InterviewUserDTO updatedData = businessService.updateInterviewUser(data);
		return ResponseEntity.status(HttpStatus.OK).body(updatedData);
	}

	/**
	 * delete a InterviewUser by primary key
	 * @param id
	 */
	@DeleteMapping(path = "/delete/{id}")
	@PreAuthorize("hasAnyAuthority('admin:delete')")
	public ResponseEntity<ResponseMessage> deleteInterviewUser(@PathVariable int id) throws Exception {
		businessService.deleteInterviewUser(id);
		ResponseMessage rb = new ResponseMessage("successfully deleted");
		return ResponseEntity.status(HttpStatus.OK).body(rb);
	}

	/**
	 * method to call the AuthenticationManager to authenticate the user's username/password against what is stored
	 * in the database
	 * @param userName
	 * @param password
	 */
	private void authenticate(String userName, String password) {
		this.authManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));

	}

	/**
	 * 
	 * @param loggedInUserPrincipal
	 * @return
	 */
	private HttpHeaders getJwtHeader(UserPrincipal loggedInUserPrincipal) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.JWT_TOKEN_HEADER, this.jwtTokenProvider.generateJwtToken(loggedInUserPrincipal));
		return headers;
	}
	
	private static void logAction(String message) {
    	System.out.println(message);
    	applicationLogger.debug(message);
    }
}
