package com.schnarbiesnmeowers.interview.controllers;

import static com.schnarbiesnmeowers.interview.utilities.Constants.INCORRECT_OLD_PASSWORD;
import static com.schnarbiesnmeowers.interview.utilities.Constants.JWT_TOKEN_HEADER;

import java.text.ParseException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.SendFailedException;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schnarbiesnmeowers.interview.business.InterviewUserBusiness;
import com.schnarbiesnmeowers.interview.dtos.CheckPasswordResetResponseDTO;
import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTO;
import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTOWrapper;
import com.schnarbiesnmeowers.interview.dtos.InterviewUserTempDTO;
import com.schnarbiesnmeowers.interview.dtos.PasswordResetDTO;
import com.schnarbiesnmeowers.interview.exceptions.handler.HttpResponse;
import com.schnarbiesnmeowers.interview.exceptions.handler.InterviewUserExceptionHandling;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.EmailExistsException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.EmailNotFoundException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.ExpiredLinkException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.PasswordIncorrectException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.PasswordResetException;
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
 * 
 * @author Dylan I. Kessler
 *
 */
//@CrossOrigin
@RestController
@RequestMapping(path = "/interviewuser")
public class InterviewUserController extends InterviewUserExceptionHandling {

	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");
	private static final Logger emailLogger = LogManager.getLogger("EmailAppender");
	public static final String EMAIL_SENT = "An email with a new password was sent to: ";
	public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";

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
	 * TODO - there are a number of things wrong with this method: 1 - makes no
	 * check to see if the fields are null or blank; will put record in DB 2 - makes
	 * no email validation; will put record with invalid email in the DB, then fails
	 * when sending the email
	 */
	/**
	 * this method allows the user to register an account
	 * 
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
	public ResponseEntity<InterviewUserDTO> register(@RequestBody InterviewUserTempDTO user)
			throws UserNotFoundException, UsernameExistsException, EmailExistsException, AddressException,
			MessagingException, UserFieldsNotValidException {
		logAction("attempting to validate new user registration");
		businessService.validateFields(user);
		logAction("new user registration validated");
		InterviewUser newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUserName(),
				user.getEmailAddr(), user.getPassword());
		return new ResponseEntity<>(newUser.toDTO(), HttpStatus.OK);
	}

	@PostMapping(path = "/confirmemail")
	public ResponseEntity<InterviewUserDTO> confirmEmail(@RequestBody String id)
			throws ExpiredLinkException, UserNotFoundException {
		logEmailAction("initiating email confirmation process");
		InterviewUser newUser = userService.confirmEmail(id);
		logEmailAction("email confirmation process completed");
		return new ResponseEntity<>(newUser.toDTO(), HttpStatus.OK);
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
	 * @throws ParseException 
	 */
	@PostMapping(path = "/login")
	public ResponseEntity<InterviewUserDTO> login(@RequestBody InterviewUserDTO user)
			throws UserNotFoundException, UsernameExistsException, EmailExistsException, ParseException {
		logAction("initiating the login process, authenticating the user");
		authenticate(user.getUserName(), user.getPassword());
		logAction("user has been authenticated");
		InterviewUserDTO loggedInUser = userService.findUserByUsername(user.getUserName()).toDTO();
		userService.checkPasswordResetTable(loggedInUser);
		UserPrincipal loggedInUserPrincipal = new UserPrincipal(loggedInUser);
		HttpHeaders jwtHeader = getJwtHeader(loggedInUserPrincipal);
		logAction("login process completed");
		return new ResponseEntity<>(loggedInUser, jwtHeader, HttpStatus.OK);
	}

	/**
	 * the 3 methods below are all used as part of the password reset functionality 
	 */
	
	/**
	 * this method is for people who have forgotten their password; it will send
	 * them an email with a new password in it
	 * @param email
	 * @return
	 * @throws MessagingException
	 * @throws EmailNotFoundException
	 */
	@PostMapping("/forgotpassword")
	public ResponseEntity<HttpResponse> forgotPassword(@RequestBody String email)
			throws MessagingException, EmailNotFoundException {
		logEmailAction("initiating the forgot password reset process");
		userService.resetPasswordInitiation(email);
		logEmailAction("forgot password reset process completed");
		return response(HttpStatus.OK, EMAIL_SENT + email);
	}

	/**
	 * this method is called upon initial load of the password reset page link that the user clicks in their email.
	 * it checks to make sure that:
	 * 1. there is an actual record in the password_rest table
	 * 2. the record is not expired
	 * @param code
	 * @return
	 * @throws ExpiredLinkException
	 * @throws UserNotFoundException
	 * @throws AddressException
	 * @throws NoSuchProviderException
	 * @throws SendFailedException
	 * @throws MessagingException
	 */
	@PostMapping(path = "/checkreset")
	public ResponseEntity<CheckPasswordResetResponseDTO> checkPasswordReset(@RequestBody String code)
			throws ExpiredLinkException, UserNotFoundException, AddressException, NoSuchProviderException, SendFailedException, MessagingException {
		logEmailAction("initiating the check password reset process");
		CheckPasswordResetResponseDTO results = userService.checkPasswordResetTable(code);
		logEmailAction("check password reset process completed");
		return new ResponseEntity<>(results, HttpStatus.OK);
	}
	
	/**
	 * this method is called once the user actually resets their password on the page link that the user clicks in their email.
	 * @param input
	 * @return
	 * @throws ExpiredLinkException
	 * @throws UserNotFoundException
	 * @throws AddressException
	 * @throws NoSuchProviderException
	 * @throws SendFailedException
	 * @throws MessagingException
	 * @throws PasswordResetException
	 */
	@PostMapping(path = "/finalizepassword")
	public ResponseEntity<InterviewUserDTO> finalizePasswordReset(@RequestBody PasswordResetDTO input)
			throws ExpiredLinkException, UserNotFoundException, AddressException, NoSuchProviderException, SendFailedException, MessagingException, PasswordResetException {
		logEmailAction("initiating the password reset finalization process");
		InterviewUserDTO results = userService.changePassword(input);
		logEmailAction("password reset finalization process completed");
		return new ResponseEntity<>(results, HttpStatus.OK);
	}
	
	/**
	 * this method is for people who have forgotten their username; it will send
	 * them an email with their username in it
	 * @param email
	 * @return
	 * @throws MessagingException
	 * @throws EmailNotFoundException
	 */
	@PostMapping("/forgotusername")
	public ResponseEntity<HttpResponse> forgotUsername(@RequestBody String email)
			throws MessagingException, EmailNotFoundException {
		logEmailAction("initiating the forgot username email process");
		userService.forgotUsername(email);
		logEmailAction("forgot username email process completed");
		return response(HttpStatus.OK, EMAIL_SENT + email);
	}

	/**
	 * update an InterviewUser by the same user
	 * update a user's contact info
	 * @param InterviewUserDTO
	 * @return InterviewUser
	 */
	@PostMapping(path = "/updateuserbyuser")
	@PreAuthorize("hasAnyAuthority('self:update')")
	public ResponseEntity<InterviewUserDTO> updateUserByUser(@Valid @RequestBody InterviewUserDTOWrapper data)
			throws Exception {
		if (data.getNewPassword() != null) {
			try {
				authenticate(data.getUserName(), data.getPassword());
			} catch (Exception e) {
				throw new PasswordIncorrectException(INCORRECT_OLD_PASSWORD);
			}
		}
		InterviewUserDTO updatedData = userService.updateUserByUser(data).toDTO();
		return ResponseEntity.status(HttpStatus.OK).body(updatedData);
	}

	/**
	 * get all InterviewUser records
	 * 
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
	 * 
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
	 * 
	 * @param InterviewUserDTO
	 * @return InterviewUser
	 */
	@PostMapping(path = "/create")
	@PreAuthorize("hasAnyAuthority('admin:create','user:create')")
	public ResponseEntity<InterviewUserDTO> createInterviewUser(@RequestHeader("Authorization") String token,
			@Valid @RequestBody InterviewUserDTO data) throws Exception {
		String alteredToken = removeBearerFromToken(token);
		String adminUser = jwtTokenProvider.getSubject(alteredToken);
		String[] authorizations = jwtTokenProvider.getClaimsFromToken(alteredToken);
		InterviewUserDTO createdData = businessService.createInterviewUser(data, authorizations, adminUser);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdData);

	}

	/**
	 * update a InterviewUser note: we cannot simply update via the primary key, as
	 * we never pass this value back out to the front end so we have to update by
	 * username, and account for if they are changing the username
	 * 
	 * @param InterviewUserDTO
	 * @return InterviewUser
	 */
	@PostMapping(path = "/update")
	@PreAuthorize("hasAnyAuthority('admin:update','user:update')")
	public ResponseEntity<InterviewUserDTO> updateInterviewUser(@RequestHeader("Authorization") String token,
			@Valid @RequestBody InterviewUserDTOWrapper data) throws Exception {
		String alteredToken = removeBearerFromToken(token);
		String adminUser = jwtTokenProvider.getSubject(alteredToken);
		String[] authorizations = jwtTokenProvider.getClaimsFromToken(alteredToken);
		InterviewUserDTO updatedData = businessService.updateInterviewUser(data, authorizations, adminUser);
		return ResponseEntity.status(HttpStatus.OK).body(updatedData);
	}

	/**
	 * delete a InterviewUser by their username
	 * 
	 * @param id
	 */
	@DeleteMapping(path = "/delete/{username}")
	@PreAuthorize("hasAnyAuthority('admin:delete','user:delete')")
	public ResponseEntity<ResponseMessage> deleteInterviewUser(@RequestHeader("Authorization") String token,
			@PathVariable String username) throws Exception {
		String alteredToken = removeBearerFromToken(token);
		String[] authorizations = jwtTokenProvider.getClaimsFromToken(alteredToken);
		String adminUser = jwtTokenProvider.getSubject(alteredToken);
		businessService.deleteInterviewUser(username, authorizations, adminUser);
		ResponseMessage rb = new ResponseMessage("successfully deleted");
		return ResponseEntity.status(HttpStatus.OK).body(rb);
	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	private String removeBearerFromToken(String token) {
		return token.replace(Constants.TOKEN_PREFIX, "");
	}

	/**
	 * method to call the AuthenticationManager to authenticate the user's
	 * username/password against what is stored in the database
	 * 
	 * @param userName
	 * @param password
	 */
	private void authenticate(String userName, String password) {
		this.authManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
	}

	/**
	 * method that makes an HttpHeaders object, generates the Jwt Token, and adds it
	 * to the headers
	 * 
	 * @param loggedInUserPrincipal
	 * @return
	 * @throws ParseException 
	 */
	private HttpHeaders getJwtHeader(UserPrincipal loggedInUserPrincipal) throws ParseException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Expose-Headers", JWT_TOKEN_HEADER);
		headers.add(Constants.JWT_TOKEN_HEADER, this.jwtTokenProvider.generateJwtToken(loggedInUserPrincipal));
		return headers;
	}

	/**
	 * method for creating an HttpResponse netity
	 * 
	 * @param httpStatus
	 * @param message
	 * @return
	 */
	private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
		return new ResponseEntity<>(
				new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message),
				httpStatus);
	}

	@PostMapping(path = "/setrole")
	public ResponseEntity<InterviewUserDTO> setRole(@RequestBody InterviewUserDTO user)
			throws UserNotFoundException, UsernameExistsException, EmailExistsException, AddressException,
			MessagingException, UserFieldsNotValidException {
		userService.setRole(user.getUserName());
		return new ResponseEntity<>(user, HttpStatus.OK);
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
	@PostMapping(path = "/setpwd")
	public ResponseEntity<InterviewUserDTO> setPassword(@RequestBody InterviewUserDTO user)
			throws UserNotFoundException, UsernameExistsException, EmailExistsException, AddressException,
			MessagingException, UserFieldsNotValidException {
		userService.setPassword(user.getUserName(), user.getPassword());
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/testemail")
	public ResponseEntity<HttpResponse> testEmail() throws AddressException, MessagingException, Exception {
		logEmailAction("running an email test");
		userService.testEmail();
		logEmailAction("email test complete");
		return response(HttpStatus.OK, EMAIL_SENT);
	}
	
	/**
	 * logging method
	 * 
	 * @param message
	 */
	private static void logAction(String message) {
		System.out.println("InterviewUserController: " + message);
		applicationLogger.debug("InterviewUserController: " + message);
	}
	
	/**
	 * logging method
	 * 
	 * @param message
	 */
	private static void logEmailAction(String message) {
		System.out.println("InterviewUserController: " + message);
		emailLogger.debug("InterviewUserController: " + message);
	}
}
