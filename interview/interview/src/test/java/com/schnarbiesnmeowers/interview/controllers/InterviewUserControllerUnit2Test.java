package com.schnarbiesnmeowers.interview.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTO;
import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTOWrapper;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.EmailExistsException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.PasswordIncorrectException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UserNotFoundException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UsernameExistsException;
import com.schnarbiesnmeowers.interview.pojos.InterviewUser;
import com.schnarbiesnmeowers.interview.services.impl.UserServiceImpl;
import com.schnarbiesnmeowers.interview.utilities.Constants;
import com.schnarbiesnmeowers.interview.utilities.HelperUtility;

//@RunWith(SpringRunner.class)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@TestPropertySource(properties = "spring.datasource.hikari.maximum-pool-size=1")
public class InterviewUserControllerUnit2Test {

	public static final String UPDATE_USER_BY_USER_URL = "/interviewuser/updateuserbyuser";
	public static final String A = "a";
	public static final String B = "b";
	public static final int ONE = 1;
	
	/**
	 * basic user for testing 403 responses
	 */
	@Value("${bbbbbbbbbbbb.token}")
	private String usertoken;
	
	/**
     * inject the Mock into the RestTemplate
     */
	@Autowired
    private TestRestTemplate restTemplate;
	
	@MockBean
	private AuthenticationManager authManager;
	
	@MockBean
	private UserServiceImpl userService;
	
	/**
	 * test the updating of a User by themselves
	 * @throws PasswordIncorrectException 
	 * @throws IOException 
	 * @throws EmailExistsException 
	 * @throws UsernameExistsException 
	 * @throws UserNotFoundException 
	 */
	@Test
	public void testUpdateUserByUser() throws UserNotFoundException, UsernameExistsException, EmailExistsException, IOException, PasswordIncorrectException {
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		final String baseUrl = UPDATE_USER_BY_USER_URL;
		InterviewUserDTO userDTO = HelperUtility.generateRandomInterviewUserDTO();
		InterviewUser user = userDTO.toEntity();
		InterviewUserDTOWrapper wrapper = new InterviewUserDTOWrapper(userDTO);
		wrapper.setNewEmailAddr(B);
		wrapper.setNewFirstName(B);
		wrapper.setNewLastName(B);
		wrapper.setNewPassword(B);
		wrapper.setNewUserName(B);
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<InterviewUserDTOWrapper> request = new HttpEntity<InterviewUserDTOWrapper>(wrapper,headers);
		doReturn(null).when(authManager).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
		when(userService.updateUserByUser(Mockito.any(InterviewUserDTOWrapper.class))).thenReturn(user);
		ResponseEntity<InterviewUserDTO> result = restTemplate.exchange(baseUrl, HttpMethod.POST, request, InterviewUserDTO.class);
		assertEquals(200, result.getStatusCodeValue());
	}
	
}
