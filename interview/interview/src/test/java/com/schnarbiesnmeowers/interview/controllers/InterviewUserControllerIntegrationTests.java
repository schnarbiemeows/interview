package com.schnarbiesnmeowers.interview.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.schnarbiesnmeowers.interview.business.InterviewUserBusiness;
import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTO;
import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTOWrapper;
import com.schnarbiesnmeowers.interview.dtos.InterviewUserTempDTO;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.EmailExistsException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UserFieldsNotValidException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UserNotFoundException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UsernameExistsException;
import com.schnarbiesnmeowers.interview.pojos.InterviewUser;
import com.schnarbiesnmeowers.interview.pojos.ResponseMessage;
import com.schnarbiesnmeowers.interview.services.InterviewUserRepository;
import com.schnarbiesnmeowers.interview.services.UserService;
import com.schnarbiesnmeowers.interview.services.impl.EmailService;
import com.schnarbiesnmeowers.interview.services.impl.UserServiceImpl;
import com.schnarbiesnmeowers.interview.utilities.Constants;
import com.schnarbiesnmeowers.interview.utilities.HelperUtility;
import com.schnarbiesnmeowers.interview.utilities.Randomizer;
import com.schnarbiesnmeowers.interview.utilities.Roles;

/**
 * this class tests the InterviewUserController class
 * the select/create/update/delete methods are deeper dive tests(integration tests)
 * the other methods are unit tests
 * these tests we want to run in order
 * @author Dylan I. Kessler
 *
 */
//@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@TestPropertySource(properties = "spring.datasource.hikari.maximum-pool-size=1")
public class InterviewUserControllerIntegrationTests {

	
	
	public static final String CREATE_URL = "/interviewuser/create";
    public static final String UPDATE_URL = "/interviewuser/update";
    public static final String DELETE_URL = "/interviewuser/delete/";
    public static final String FIND_ALL_URL = "/interviewuser/all";
    public static final String FIND_BY_ID = "/interviewuser/findById/";
    public static final String UPDATE_USER_BY_USER_URL = "/interviewuser/updateuserbyuser";
    public static final String A = "a";
	public static final String B = "b";
	public static final int ONE = 1;
	public static final String VALID_EMAIL = "dummy@email.com";
	
	/**
	 * admin user for testing
	 */
	@Value("${aaaaaaaaaaaa.token}")
	private String admintoken;

	@Value("${aaaaaaaaaaaa.username}")
	private String adminusername;
	
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
	private InterviewUserRepository interviewUserRepository;
	
	@MockBean
	private EmailService emailService;
	
	@MockBean
	private AuthenticationManager authManager;
	
	/**
	 * test getting all InterviewUser using a normal user 
	 * this should fail and produce a 403 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testGetAllInterviewUserWithUser() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		final String baseUrl = FIND_ALL_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(403, result.getStatusCodeValue());
	}

	/**
	 * test getting all InterviewUser using an admin user
	 * this should succeed and produce a 200 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testGetAllInterviewUserWithAdmin() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		InterviewUser anInterviewUser = generateRandomInterviewUser();
		List<InterviewUser> answers = new ArrayList<InterviewUser>();
		answers.add(anInterviewUser);
		when(interviewUserRepository.findAll()).thenReturn(answers);
		final String baseUrl = FIND_ALL_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		Gson gson = new Gson();
		List<InterviewUserDTO> responseBody = gson.fromJson(result.getBody(),new TypeToken<List<InterviewUserDTO>>(){}.getType());
		assertEquals(1,responseBody.size());
	}
	
	/**
	 * test getting a single InterviewUser using a normal user 
	 * this should fail and produce a 403 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testGetInterviewUserWithUser() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		int num = 1;
		final String baseUrl = FIND_BY_ID + num;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(403, result.getStatusCodeValue());
	}
	
	/**
	 * test getting a single InterviewUser using an admin user
	 * this should succeed and produce a 200 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testGetInterviewUserWithAdmin() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		int num = 1;
		InterviewUser anInterviewUser = generateRandomInterviewUser();
		anInterviewUser.setUserId(num);
		Optional<InterviewUser> anInterviewUserOpt = Optional.of(anInterviewUser);
		when(interviewUserRepository.findById(num)).thenReturn(anInterviewUserOpt);
		final String baseUrl = FIND_BY_ID + num;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		Gson gson = new Gson();
		InterviewUserDTO responseBody = gson.fromJson(result.getBody(),InterviewUserDTO.class);
		assertTrue(anInterviewUser.getUserName().equals(responseBody.getUserName()));
	}
	
	/**
	 * test creating a new InterviewUser using a normal user
	 * this should fail and produce a 403 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testCreateInterviewUserWithUser() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		InterviewUser anInterviewUser = generateRandomInterviewUser();
		InterviewUserDTO anInterviewUserDTO = anInterviewUser.toDTO();
		Optional<InterviewUser> anInterviewUserOpt = Optional.of(anInterviewUser);
		when(interviewUserRepository.save(anInterviewUser)).thenReturn(anInterviewUser);
		final String createUrl = CREATE_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<InterviewUserDTO> request = new HttpEntity<InterviewUserDTO>(anInterviewUserDTO,headers);
		ResponseEntity<String> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, String.class);
		// 403 response from normal user
		assertEquals(403, result.getStatusCodeValue());
    }
	
	/**
	 * test creating some new InterviewUser users using an admin user
	 * the users made and the expected responses are:
	 * ROLE_BASIC_USER = 201
	 * ROLE_ADV_USER = 201
	 * ROLE_PREMIUM_USER = 201
	 * ROLE_ADMIN = 403(admins cannot make other admins)
	 * @throws URISyntaxException
	 */
	@Test
	public void testCreateInterviewUsersWithAdmin() throws URISyntaxException
	{
		// ADMIN who is trying to create a BASIC USER- should be a 201
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		InterviewUserDTO anInterviewUserDTO = HelperUtility.generateRandomInterviewUserDTO();
		HelperUtility.addRolesAndAuthorizationsToUser(Roles.ROLE_BASIC_USER,anInterviewUserDTO);
		InterviewUser anInterviewUser = anInterviewUserDTO.toEntity();
		Optional<InterviewUser> anInterviewUserOpt = Optional.of(anInterviewUser);
		when(interviewUserRepository.save(Mockito.any(InterviewUser.class))).thenReturn(anInterviewUser);
		final String createUrl = CREATE_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<InterviewUserDTO> request = new HttpEntity<InterviewUserDTO>(anInterviewUserDTO,headers);
		ResponseEntity<InterviewUserDTO> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, InterviewUserDTO.class);
		// 201 response
		assertEquals(201, result.getStatusCodeValue());
		assertTrue(result.getBody().getUserName().equals(anInterviewUserDTO.getUserName()));
		assertTrue(result.getBody().getFirstName().equals(anInterviewUserDTO.getFirstName()));
		assertTrue(result.getBody().getLastName().equals(anInterviewUserDTO.getLastName()));
		assertTrue(result.getBody().getEmailAddr().equals(anInterviewUserDTO.getEmailAddr()));
		assertTrue(result.getBody().getRoles().equals(anInterviewUserDTO.getRoles()));
		assertTrue(result.getBody().getAuthorizations().length==anInterviewUserDTO.getAuthorizations().length);
		// ADMIN who is trying to create a ADVANCED USER- should be a 201
		anInterviewUserDTO = HelperUtility.generateRandomInterviewUserDTO();
		HelperUtility.addRolesAndAuthorizationsToUser(Roles.ROLE_ADV_USER,anInterviewUserDTO);
		anInterviewUser = anInterviewUserDTO.toEntity();
		anInterviewUserOpt = Optional.of(anInterviewUser);
		when(interviewUserRepository.save(Mockito.any(InterviewUser.class))).thenReturn(anInterviewUser);
		headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		request = new HttpEntity<InterviewUserDTO>(anInterviewUserDTO,headers);
		result = restTemplate.exchange(createUrl, HttpMethod.POST, request, InterviewUserDTO.class);
		// 201 response
		assertEquals(201, result.getStatusCodeValue());
		assertTrue(result.getBody().getUserName().equals(anInterviewUserDTO.getUserName()));
		assertTrue(result.getBody().getFirstName().equals(anInterviewUserDTO.getFirstName()));
		assertTrue(result.getBody().getLastName().equals(anInterviewUserDTO.getLastName()));
		assertTrue(result.getBody().getEmailAddr().equals(anInterviewUserDTO.getEmailAddr()));
		assertTrue(result.getBody().getRoles().equals(anInterviewUserDTO.getRoles()));
		assertTrue(result.getBody().getAuthorizations().length==anInterviewUserDTO.getAuthorizations().length);
		// ADMIN who is trying to create a PREMIUM USER- should be a 201
		anInterviewUserDTO = HelperUtility.generateRandomInterviewUserDTO();
		HelperUtility.addRolesAndAuthorizationsToUser(Roles.ROLE_PREMIUM_USER,anInterviewUserDTO);
		anInterviewUser = anInterviewUserDTO.toEntity();
		anInterviewUserOpt = Optional.of(anInterviewUser);
		when(interviewUserRepository.save(Mockito.any(InterviewUser.class))).thenReturn(anInterviewUser);
		headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		request = new HttpEntity<InterviewUserDTO>(anInterviewUserDTO,headers);
		result = restTemplate.exchange(createUrl, HttpMethod.POST, request, InterviewUserDTO.class);
		// 201 response
		assertEquals(201, result.getStatusCodeValue());
		assertTrue(result.getBody().getUserName().equals(anInterviewUserDTO.getUserName()));
		assertTrue(result.getBody().getFirstName().equals(anInterviewUserDTO.getFirstName()));
		assertTrue(result.getBody().getLastName().equals(anInterviewUserDTO.getLastName()));
		assertTrue(result.getBody().getEmailAddr().equals(anInterviewUserDTO.getEmailAddr()));
		assertTrue(result.getBody().getRoles().equals(anInterviewUserDTO.getRoles()));
		assertTrue(result.getBody().getAuthorizations().length==anInterviewUserDTO.getAuthorizations().length);
		// ADMIN who is trying to create another ADMIN - should be a 403
		anInterviewUserDTO = HelperUtility.generateRandomInterviewUserDTO();
		HelperUtility.addRolesAndAuthorizationsToUser(Roles.ROLE_ADMIN,anInterviewUserDTO);
		anInterviewUser = anInterviewUserDTO.toEntity();
		anInterviewUserOpt = Optional.of(anInterviewUser);
		when(interviewUserRepository.save(Mockito.any(InterviewUser.class))).thenReturn(anInterviewUser);
		headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		request = new HttpEntity<InterviewUserDTO>(anInterviewUserDTO,headers);
		result = restTemplate.exchange(createUrl, HttpMethod.POST, request, InterviewUserDTO.class);
		// 403 response
		assertEquals(403, result.getStatusCodeValue());
		// ADMIN who is trying to create a SUPER ADMIN - should be a 403
		anInterviewUserDTO = HelperUtility.generateRandomInterviewUserDTO();
		HelperUtility.addRolesAndAuthorizationsToUser(Roles.ROLE_SUPER,anInterviewUserDTO);
		anInterviewUser = anInterviewUserDTO.toEntity();
		anInterviewUserOpt = Optional.of(anInterviewUser);
		when(interviewUserRepository.save(Mockito.any(InterviewUser.class))).thenReturn(anInterviewUser);
		headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		request = new HttpEntity<InterviewUserDTO>(anInterviewUserDTO,headers);
		result = restTemplate.exchange(createUrl, HttpMethod.POST, request, InterviewUserDTO.class);
		// 403 response
		assertEquals(403, result.getStatusCodeValue());
    }

	/**
	 * test creating some new InterviewUser users using a super admin user
	 * the users made and the expected responses are:
	 * ROLE_BASIC_USER = 201
	 * ROLE_ADV_USER = 201
	 * ROLE_PREMIUM_USER = 201
	 * ROLE_ADMIN = 201
	 * @throws URISyntaxException
	 */
	@Test
	public void testCreateInterviewUsersWithSuperAdmin() throws URISyntaxException
	{
		/*
		 * TODO - make a super admin test user and fill out this method
		 */
//		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
//		InterviewUserDTO anInterviewUserDTO = HelperUtility.generateRandomInterviewUserDTO();
//		addRolesAndAuthorizationsToUser(Roles.ROLE_BASIC_USER,anInterviewUserDTO);
//		InterviewUser anInterviewUser = anInterviewUserDTO.toEntity();
//		Optional<InterviewUser> anInterviewUserOpt = Optional.of(anInterviewUser);
//		when(interviewUserRepository.save(Mockito.any(InterviewUser.class))).thenReturn(anInterviewUser);
//		final String createUrl = CREATE_URL;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<InterviewUserDTO> request = new HttpEntity<InterviewUserDTO>(anInterviewUserDTO,headers);
//		ResponseEntity<InterviewUserDTO> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, InterviewUserDTO.class);
//		// 201 response
//		assertEquals(201, result.getStatusCodeValue());
//		assertTrue(result.getBody().getUserName().equals(anInterviewUserDTO.getUserName()));
//		assertTrue(result.getBody().getFirstName().equals(anInterviewUserDTO.getFirstName()));
//		assertTrue(result.getBody().getLastName().equals(anInterviewUserDTO.getLastName()));
//		assertTrue(result.getBody().getEmailAddr().equals(anInterviewUserDTO.getEmailAddr()));
//		assertTrue(result.getBody().getRoles().equals(anInterviewUserDTO.getRoles()));
//		assertTrue(result.getBody().getAuthorizations().length==anInterviewUserDTO.getAuthorizations().length);
//		// advanced user
//		anInterviewUserDTO = HelperUtility.generateRandomInterviewUserDTO();
//		addRolesAndAuthorizationsToUser(Roles.ROLE_ADV_USER,anInterviewUserDTO);
//		anInterviewUser = anInterviewUserDTO.toEntity();
//		anInterviewUserOpt = Optional.of(anInterviewUser);
//		when(interviewUserRepository.save(Mockito.any(InterviewUser.class))).thenReturn(anInterviewUser);
//		headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		request = new HttpEntity<InterviewUserDTO>(anInterviewUserDTO,headers);
//		result = restTemplate.exchange(createUrl, HttpMethod.POST, request, InterviewUserDTO.class);
//		// 201 response
//		assertEquals(201, result.getStatusCodeValue());
//		assertTrue(result.getBody().getUserName().equals(anInterviewUserDTO.getUserName()));
//		assertTrue(result.getBody().getFirstName().equals(anInterviewUserDTO.getFirstName()));
//		assertTrue(result.getBody().getLastName().equals(anInterviewUserDTO.getLastName()));
//		assertTrue(result.getBody().getEmailAddr().equals(anInterviewUserDTO.getEmailAddr()));
//		assertTrue(result.getBody().getRoles().equals(anInterviewUserDTO.getRoles()));
//		assertTrue(result.getBody().getAuthorizations().length==anInterviewUserDTO.getAuthorizations().length);
//		// premium user
//		anInterviewUserDTO = HelperUtility.generateRandomInterviewUserDTO();
//		addRolesAndAuthorizationsToUser(Roles.ROLE_PREMIUM_USER,anInterviewUserDTO);
//		anInterviewUser = anInterviewUserDTO.toEntity();
//		anInterviewUserOpt = Optional.of(anInterviewUser);
//		when(interviewUserRepository.save(Mockito.any(InterviewUser.class))).thenReturn(anInterviewUser);
//		headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		request = new HttpEntity<InterviewUserDTO>(anInterviewUserDTO,headers);
//		result = restTemplate.exchange(createUrl, HttpMethod.POST, request, InterviewUserDTO.class);
//		// 201 response
//		assertEquals(201, result.getStatusCodeValue());
//		assertTrue(result.getBody().getUserName().equals(anInterviewUserDTO.getUserName()));
//		assertTrue(result.getBody().getFirstName().equals(anInterviewUserDTO.getFirstName()));
//		assertTrue(result.getBody().getLastName().equals(anInterviewUserDTO.getLastName()));
//		assertTrue(result.getBody().getEmailAddr().equals(anInterviewUserDTO.getEmailAddr()));
//		assertTrue(result.getBody().getRoles().equals(anInterviewUserDTO.getRoles()));
//		assertTrue(result.getBody().getAuthorizations().length==anInterviewUserDTO.getAuthorizations().length);
//		// another admin - should be 403 error
//		anInterviewUserDTO = HelperUtility.generateRandomInterviewUserDTO();
//		addRolesAndAuthorizationsToUser(Roles.ROLE_ADMIN,anInterviewUserDTO);
//		anInterviewUser = anInterviewUserDTO.toEntity();
//		anInterviewUserOpt = Optional.of(anInterviewUser);
//		when(interviewUserRepository.save(Mockito.any(InterviewUser.class))).thenReturn(anInterviewUser);
//		headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		request = new HttpEntity<InterviewUserDTO>(anInterviewUserDTO,headers);
//		result = restTemplate.exchange(createUrl, HttpMethod.POST, request, InterviewUserDTO.class);
//		// 403 response
//		assertEquals(403, result.getStatusCodeValue());
//		assertTrue(true);
    }
	
	/**
	 * test updating an InterviewUser using a normal user 
	 * NOTE: this method is not the one used whenever a USER is trying to update their information
	 * this method is only called via the administration console, which is not accessible by users, only admins
	 * this should fail and produce a 403 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testUpdateInterviewUserWithUser() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		InterviewUser anInterviewUser = generateRandomInterviewUser();
		InterviewUserDTO anInterviewUserDTO = anInterviewUser.toDTO();
		Optional<InterviewUser> anInterviewUserOpt = Optional.of(anInterviewUser);
		when(interviewUserRepository.save(anInterviewUser)).thenReturn(anInterviewUser);
		final String createUrl = UPDATE_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<InterviewUserDTO> request = new HttpEntity<InterviewUserDTO>(anInterviewUserDTO,headers);
		ResponseEntity<String> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, String.class);
		// 403 response from normal user
		assertEquals(403, result.getStatusCodeValue());	
    }
	
	/**
	 * test updating some InterviewUser users using an admin user
	 * the users updated and the expected responses are:
	 * ROLE_BASIC_USER = 200
	 * ROLE_ADV_USER = 200
	 * ROLE_PREMIUM_USER = 200
	 * ROLE_ADMIN(an admin other than the one making the changes) = 403
	 * ROLE_ADMIN(same admin as the one making the changes) = 200
	 * ROLE_SUPER = 403
	 * @throws URISyntaxException
	 */
	@Test
	public void testUpdateInterviewUserWithAdmin() throws URISyntaxException
	{
		// ADMIN who is trying to change a BASIC USER- should be a 200
		int num = 1;
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		InterviewUserDTO anInterviewUserDTO = HelperUtility.generateRandomInterviewUserDTO();
		anInterviewUserDTO.setEmailAddr(A);
		anInterviewUserDTO.setUserName(A);
		anInterviewUserDTO.setFirstName(A);
		anInterviewUserDTO.setLastName(A);
		anInterviewUserDTO.setPassword(A);
		anInterviewUserDTO.setUserNotLocked(true);
		anInterviewUserDTO.setUserActive(true);
		HelperUtility.addRolesAndAuthorizationsToUser(Roles.ROLE_BASIC_USER,anInterviewUserDTO);
		anInterviewUserDTO.setUserId(num);
		InterviewUser anInterviewUser = anInterviewUserDTO.toEntity();
		InterviewUserDTOWrapper inputData = new InterviewUserDTOWrapper(anInterviewUserDTO);
		inputData.setEmailAddr(B);
		inputData.setFirstName(B);
		inputData.setLastName(B);
		inputData.setNewUserName(B);
		inputData.setPassword(B);
		inputData.setUserNotLocked(false);
		inputData.setUserActive(false);
		Optional<InterviewUser> anInterviewUserOpt = Optional.of(anInterviewUser);
		when(interviewUserRepository.save(Mockito.any(InterviewUser.class))).thenReturn(anInterviewUser);
		when(interviewUserRepository.findById(num)).thenReturn(anInterviewUserOpt);
		when(interviewUserRepository.findUserByUserName(Mockito.anyString())).thenReturn(anInterviewUser);
		when(interviewUserRepository.findUserByEmailAddr(Mockito.anyString())).thenReturn(anInterviewUser);
		final String updateUrl = UPDATE_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<InterviewUserDTO> request = new HttpEntity<InterviewUserDTO>(inputData,headers);
		ResponseEntity<InterviewUserDTO> result = restTemplate.exchange(updateUrl, HttpMethod.POST, request, InterviewUserDTO.class);
		// 200 response 
		assertEquals(200, result.getStatusCodeValue());
		assertTrue(!result.getBody().getUserName().equals(anInterviewUserDTO.getUserName()));
		assertTrue(!result.getBody().getFirstName().equals(anInterviewUserDTO.getFirstName()));
		assertTrue(!result.getBody().getLastName().equals(anInterviewUserDTO.getLastName()));
		assertTrue(!result.getBody().getEmailAddr().equals(anInterviewUserDTO.getEmailAddr()));
		// can't do this one below, because the password is read-only
		//assertTrue(!result.getBody().getPassword().equals(anInterviewUserDTO.getPassword()));
		assertTrue(result.getBody().isUserActive()!=anInterviewUserDTO.isUserActive());
		assertTrue(result.getBody().isUserNotLocked()!=anInterviewUserDTO.isUserNotLocked());
		// ADMIN who is trying to change a ADVANCED USER- should be a 200
		HelperUtility.addRolesAndAuthorizationsToUser(Roles.ROLE_ADV_USER,anInterviewUserDTO);
		anInterviewUser = anInterviewUserDTO.toEntity();
		inputData = new InterviewUserDTOWrapper(anInterviewUserDTO);
		inputData.setEmailAddr(B);
		inputData.setFirstName(B);
		inputData.setLastName(B);
		inputData.setNewUserName(B);
		inputData.setPassword(B);
		inputData.setUserNotLocked(false);
		inputData.setUserActive(false);
		anInterviewUserOpt = Optional.of(anInterviewUser);
		request = new HttpEntity<InterviewUserDTO>(inputData,headers);
		result = restTemplate.exchange(updateUrl, HttpMethod.POST, request, InterviewUserDTO.class);
		// 200 response 
		assertEquals(200, result.getStatusCodeValue());
		assertTrue(!result.getBody().getUserName().equals(anInterviewUserDTO.getUserName()));
		assertTrue(!result.getBody().getFirstName().equals(anInterviewUserDTO.getFirstName()));
		assertTrue(!result.getBody().getLastName().equals(anInterviewUserDTO.getLastName()));
		assertTrue(!result.getBody().getEmailAddr().equals(anInterviewUserDTO.getEmailAddr()));
		// can't do this one below, because the password is read-only
		//assertTrue(!result.getBody().getPassword().equals(anInterviewUserDTO.getPassword()));
		assertTrue(result.getBody().isUserActive()!=anInterviewUserDTO.isUserActive());
		assertTrue(result.getBody().isUserNotLocked()!=anInterviewUserDTO.isUserNotLocked());
		// ADMIN who is trying to change a PREMIUM USER- should be a 200
		HelperUtility.addRolesAndAuthorizationsToUser(Roles.ROLE_PREMIUM_USER,anInterviewUserDTO);
		anInterviewUser = anInterviewUserDTO.toEntity();
		inputData = new InterviewUserDTOWrapper(anInterviewUserDTO);
		inputData.setEmailAddr(B);
		inputData.setFirstName(B);
		inputData.setLastName(B);
		inputData.setNewUserName(B);
		inputData.setPassword(B);
		inputData.setUserNotLocked(false);
		inputData.setUserActive(false);
		anInterviewUserOpt = Optional.of(anInterviewUser);
		request = new HttpEntity<InterviewUserDTO>(inputData,headers);
		result = restTemplate.exchange(updateUrl, HttpMethod.POST, request, InterviewUserDTO.class);
		// 200 response 
		assertEquals(200, result.getStatusCodeValue());
		assertTrue(!result.getBody().getUserName().equals(anInterviewUserDTO.getUserName()));
		assertTrue(!result.getBody().getFirstName().equals(anInterviewUserDTO.getFirstName()));
		assertTrue(!result.getBody().getLastName().equals(anInterviewUserDTO.getLastName()));
		assertTrue(!result.getBody().getEmailAddr().equals(anInterviewUserDTO.getEmailAddr()));
		// can't do this one below, because the password is read-only
		//assertTrue(!result.getBody().getPassword().equals(anInterviewUserDTO.getPassword()));
		assertTrue(result.getBody().isUserActive()!=anInterviewUserDTO.isUserActive());
		assertTrue(result.getBody().isUserNotLocked()!=anInterviewUserDTO.isUserNotLocked());
		// ADMIN who is trying to change their own info - should be a 200
		anInterviewUserDTO.setUserName(adminusername);
		HelperUtility.addRolesAndAuthorizationsToUser(Roles.ROLE_ADMIN,anInterviewUserDTO);
		anInterviewUser = anInterviewUserDTO.toEntity();
		anInterviewUser.setUserName(adminusername);
		when(interviewUserRepository.findUserByUserName(Mockito.anyString())).thenReturn(anInterviewUser);
		when(interviewUserRepository.findUserByEmailAddr(Mockito.anyString())).thenReturn(anInterviewUser);
		when(interviewUserRepository.save(Mockito.any(InterviewUser.class))).thenReturn(anInterviewUser);
		inputData = new InterviewUserDTOWrapper(anInterviewUserDTO);
		inputData.setEmailAddr(B);
		inputData.setFirstName(B);
		inputData.setLastName(B);
		inputData.setNewUserName(adminusername);
		inputData.setUserName(adminusername);
		inputData.setPassword(B);
		inputData.setUserNotLocked(false);
		inputData.setUserActive(false);
		anInterviewUserOpt = Optional.of(anInterviewUser);
		request = new HttpEntity<InterviewUserDTO>(inputData,headers);
		result = restTemplate.exchange(updateUrl, HttpMethod.POST, request, InterviewUserDTO.class);
		// 200 response 
		assertEquals(200, result.getStatusCodeValue());
		assertTrue(result.getBody().getUserName().equals(anInterviewUserDTO.getUserName()));
		assertTrue(!result.getBody().getFirstName().equals(anInterviewUserDTO.getFirstName()));
		assertTrue(!result.getBody().getLastName().equals(anInterviewUserDTO.getLastName()));
		assertTrue(!result.getBody().getEmailAddr().equals(anInterviewUserDTO.getEmailAddr()));
		// can't do this one below, because the password is read-only
		//assertTrue(!result.getBody().getPassword().equals(anInterviewUserDTO.getPassword()));
		assertTrue(result.getBody().isUserActive()!=anInterviewUserDTO.isUserActive());
		assertTrue(result.getBody().isUserNotLocked()!=anInterviewUserDTO.isUserNotLocked());
		// ADMIN who is trying to change another admin - should be a 403
		anInterviewUserDTO.setUserName(adminusername);
		HelperUtility.addRolesAndAuthorizationsToUser(Roles.ROLE_ADMIN,anInterviewUserDTO);
		anInterviewUser = anInterviewUserDTO.toEntity();
		anInterviewUser.setUserName("x");
		when(interviewUserRepository.findUserByUserName(Mockito.anyString())).thenReturn(anInterviewUser);
		when(interviewUserRepository.findUserByEmailAddr(Mockito.anyString())).thenReturn(anInterviewUser);
		when(interviewUserRepository.save(Mockito.any(InterviewUser.class))).thenReturn(anInterviewUser);
		inputData = new InterviewUserDTOWrapper(anInterviewUserDTO);
		inputData.setEmailAddr(B);
		inputData.setFirstName(B);
		inputData.setLastName(B);
		inputData.setNewUserName("x");
		inputData.setUserName("x");
		inputData.setPassword(B);
		inputData.setUserNotLocked(false);
		inputData.setUserActive(false);
		anInterviewUserOpt = Optional.of(anInterviewUser);
		request = new HttpEntity<InterviewUserDTO>(inputData,headers);
		result = restTemplate.exchange(updateUrl, HttpMethod.POST, request, InterviewUserDTO.class);
		// 403 response 
		assertEquals(403, result.getStatusCodeValue());
		// ADMIN who is trying to change a SUPER ADMINn - should be a 403
		HelperUtility.addRolesAndAuthorizationsToUser(Roles.ROLE_SUPER,anInterviewUserDTO);
		anInterviewUser = anInterviewUserDTO.toEntity();
		anInterviewUser.setUserName("x");
		when(interviewUserRepository.findUserByUserName(Mockito.anyString())).thenReturn(anInterviewUser);
		when(interviewUserRepository.findUserByEmailAddr(Mockito.anyString())).thenReturn(anInterviewUser);
		when(interviewUserRepository.save(Mockito.any(InterviewUser.class))).thenReturn(anInterviewUser);
		inputData = new InterviewUserDTOWrapper(anInterviewUserDTO);
		inputData.setEmailAddr(B);
		inputData.setFirstName(B);
		inputData.setLastName(B);
		inputData.setNewUserName("x");
		inputData.setUserName("x");
		inputData.setPassword(B);
		inputData.setUserNotLocked(false);
		inputData.setUserActive(false);
		anInterviewUserOpt = Optional.of(anInterviewUser);
		request = new HttpEntity<InterviewUserDTO>(inputData,headers);
		result = restTemplate.exchange(updateUrl, HttpMethod.POST, request, InterviewUserDTO.class);
		// 403 response 
		assertEquals(403, result.getStatusCodeValue());
    }

	/**
	 * test updating some InterviewUser users using a super admin user
	 * the users updated and the expected responses are:
	 * all cases should succeed and produce a 200 response
	 * @throws URISyntaxException
	 */
	@Test
	public void testUpdateInterviewUserWithSuperAdmin() throws URISyntaxException
	{
		/*
		 * TODO - make a super admin test user and fill out this method
		 */
		// ADMIN who is trying to change a BASIC USER- should be a 200
//		int num = 1;
//		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
//		InterviewUserDTO anInterviewUserDTO = HelperUtility.generateRandomInterviewUserDTO();
//		anInterviewUserDTO.setEmailAddr(A);
//		anInterviewUserDTO.setUserName(A);
//		anInterviewUserDTO.setFirstName(A);
//		anInterviewUserDTO.setLastName(A);
//		anInterviewUserDTO.setPassword(A);
//		anInterviewUserDTO.setUserNotLocked(true);
//		anInterviewUserDTO.setUserActive(true);
//		addRolesAndAuthorizationsToUser(Roles.ROLE_BASIC_USER,anInterviewUserDTO);
//		anInterviewUserDTO.setUserId(num);
//		InterviewUser anInterviewUser = anInterviewUserDTO.toEntity();
//		InterviewUserDTOWrapper inputData = new InterviewUserDTOWrapper(anInterviewUserDTO);
//		inputData.setEmailAddr(B);
//		inputData.setFirstName(B);
//		inputData.setLastName(B);
//		inputData.setNewUserName(B);
//		inputData.setPassword(B);
//		inputData.setUserNotLocked(false);
//		inputData.setUserActive(false);
//		Optional<InterviewUser> anInterviewUserOpt = Optional.of(anInterviewUser);
//		when(interviewUserRepository.save(Mockito.any(InterviewUser.class))).thenReturn(anInterviewUser);
//		when(interviewUserRepository.findById(num)).thenReturn(anInterviewUserOpt);
//		when(interviewUserRepository.findUserByUserName(Mockito.anyString())).thenReturn(anInterviewUser);
//		when(interviewUserRepository.findUserByEmailAddr(Mockito.anyString())).thenReturn(anInterviewUser);
//		final String updateUrl = UPDATE_URL;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<InterviewUserDTO> request = new HttpEntity<InterviewUserDTO>(inputData,headers);
//		ResponseEntity<InterviewUserDTO> result = restTemplate.exchange(updateUrl, HttpMethod.POST, request, InterviewUserDTO.class);
//		// 200 response 
//		assertEquals(200, result.getStatusCodeValue());
//		assertTrue(!result.getBody().getUserName().equals(anInterviewUserDTO.getUserName()));
//		assertTrue(!result.getBody().getFirstName().equals(anInterviewUserDTO.getFirstName()));
//		assertTrue(!result.getBody().getLastName().equals(anInterviewUserDTO.getLastName()));
//		assertTrue(!result.getBody().getEmailAddr().equals(anInterviewUserDTO.getEmailAddr()));
//		// can't do this one below, because the password is read-only
//		//assertTrue(!result.getBody().getPassword().equals(anInterviewUserDTO.getPassword()));
//		assertTrue(result.getBody().isUserActive()!=anInterviewUserDTO.isUserActive());
//		assertTrue(result.getBody().isUserNotLocked()!=anInterviewUserDTO.isUserNotLocked());
//		// ADMIN who is trying to change a ADVANCED USER- should be a 200
//		addRolesAndAuthorizationsToUser(Roles.ROLE_ADV_USER,anInterviewUserDTO);
//		anInterviewUser = anInterviewUserDTO.toEntity();
//		inputData = new InterviewUserDTOWrapper(anInterviewUserDTO);
//		inputData.setEmailAddr(B);
//		inputData.setFirstName(B);
//		inputData.setLastName(B);
//		inputData.setNewUserName(B);
//		inputData.setPassword(B);
//		inputData.setUserNotLocked(false);
//		inputData.setUserActive(false);
//		anInterviewUserOpt = Optional.of(anInterviewUser);
//		request = new HttpEntity<InterviewUserDTO>(inputData,headers);
//		result = restTemplate.exchange(updateUrl, HttpMethod.POST, request, InterviewUserDTO.class);
//		// 200 response 
//		assertEquals(200, result.getStatusCodeValue());
//		assertTrue(!result.getBody().getUserName().equals(anInterviewUserDTO.getUserName()));
//		assertTrue(!result.getBody().getFirstName().equals(anInterviewUserDTO.getFirstName()));
//		assertTrue(!result.getBody().getLastName().equals(anInterviewUserDTO.getLastName()));
//		assertTrue(!result.getBody().getEmailAddr().equals(anInterviewUserDTO.getEmailAddr()));
//		// can't do this one below, because the password is read-only
//		//assertTrue(!result.getBody().getPassword().equals(anInterviewUserDTO.getPassword()));
//		assertTrue(result.getBody().isUserActive()!=anInterviewUserDTO.isUserActive());
//		assertTrue(result.getBody().isUserNotLocked()!=anInterviewUserDTO.isUserNotLocked());
//		// ADMIN who is trying to change a PREMIUM USER- should be a 200
//		addRolesAndAuthorizationsToUser(Roles.ROLE_PREMIUM_USER,anInterviewUserDTO);
//		anInterviewUser = anInterviewUserDTO.toEntity();
//		inputData = new InterviewUserDTOWrapper(anInterviewUserDTO);
//		inputData.setEmailAddr(B);
//		inputData.setFirstName(B);
//		inputData.setLastName(B);
//		inputData.setNewUserName(B);
//		inputData.setPassword(B);
//		inputData.setUserNotLocked(false);
//		inputData.setUserActive(false);
//		anInterviewUserOpt = Optional.of(anInterviewUser);
//		request = new HttpEntity<InterviewUserDTO>(inputData,headers);
//		result = restTemplate.exchange(updateUrl, HttpMethod.POST, request, InterviewUserDTO.class);
//		// 200 response 
//		assertEquals(200, result.getStatusCodeValue());
//		assertTrue(!result.getBody().getUserName().equals(anInterviewUserDTO.getUserName()));
//		assertTrue(!result.getBody().getFirstName().equals(anInterviewUserDTO.getFirstName()));
//		assertTrue(!result.getBody().getLastName().equals(anInterviewUserDTO.getLastName()));
//		assertTrue(!result.getBody().getEmailAddr().equals(anInterviewUserDTO.getEmailAddr()));
//		// can't do this one below, because the password is read-only
//		//assertTrue(!result.getBody().getPassword().equals(anInterviewUserDTO.getPassword()));
//		assertTrue(result.getBody().isUserActive()!=anInterviewUserDTO.isUserActive());
//		assertTrue(result.getBody().isUserNotLocked()!=anInterviewUserDTO.isUserNotLocked());
//		// ADMIN who is trying to change their own info - should be a 200
//		anInterviewUserDTO.setUserName(adminusername);
//		addRolesAndAuthorizationsToUser(Roles.ROLE_ADMIN,anInterviewUserDTO);
//		anInterviewUser = anInterviewUserDTO.toEntity();
//		anInterviewUser.setUserName(adminusername);
//		when(interviewUserRepository.findUserByUserName(Mockito.anyString())).thenReturn(anInterviewUser);
//		when(interviewUserRepository.findUserByEmailAddr(Mockito.anyString())).thenReturn(anInterviewUser);
//		when(interviewUserRepository.save(Mockito.any(InterviewUser.class))).thenReturn(anInterviewUser);
//		inputData = new InterviewUserDTOWrapper(anInterviewUserDTO);
//		inputData.setEmailAddr(B);
//		inputData.setFirstName(B);
//		inputData.setLastName(B);
//		inputData.setNewUserName(adminusername);
//		inputData.setUserName(adminusername);
//		inputData.setPassword(B);
//		inputData.setUserNotLocked(false);
//		inputData.setUserActive(false);
//		anInterviewUserOpt = Optional.of(anInterviewUser);
//		request = new HttpEntity<InterviewUserDTO>(inputData,headers);
//		result = restTemplate.exchange(updateUrl, HttpMethod.POST, request, InterviewUserDTO.class);
//		// 200 response 
//		assertEquals(200, result.getStatusCodeValue());
//		assertTrue(result.getBody().getUserName().equals(anInterviewUserDTO.getUserName()));
//		assertTrue(!result.getBody().getFirstName().equals(anInterviewUserDTO.getFirstName()));
//		assertTrue(!result.getBody().getLastName().equals(anInterviewUserDTO.getLastName()));
//		assertTrue(!result.getBody().getEmailAddr().equals(anInterviewUserDTO.getEmailAddr()));
//		// can't do this one below, because the password is read-only
//		//assertTrue(!result.getBody().getPassword().equals(anInterviewUserDTO.getPassword()));
//		assertTrue(result.getBody().isUserActive()!=anInterviewUserDTO.isUserActive());
//		assertTrue(result.getBody().isUserNotLocked()!=anInterviewUserDTO.isUserNotLocked());
//		// ADMIN who is trying to change another admin - should be a 403
//		anInterviewUserDTO.setUserName(adminusername);
//		addRolesAndAuthorizationsToUser(Roles.ROLE_ADMIN,anInterviewUserDTO);
//		anInterviewUser = anInterviewUserDTO.toEntity();
//		anInterviewUser.setUserName("x");
//		when(interviewUserRepository.findUserByUserName(Mockito.anyString())).thenReturn(anInterviewUser);
//		when(interviewUserRepository.findUserByEmailAddr(Mockito.anyString())).thenReturn(anInterviewUser);
//		when(interviewUserRepository.save(Mockito.any(InterviewUser.class))).thenReturn(anInterviewUser);
//		inputData = new InterviewUserDTOWrapper(anInterviewUserDTO);
//		inputData.setEmailAddr(B);
//		inputData.setFirstName(B);
//		inputData.setLastName(B);
//		inputData.setNewUserName("x");
//		inputData.setUserName("x");
//		inputData.setPassword(B);
//		inputData.setUserNotLocked(false);
//		inputData.setUserActive(false);
//		anInterviewUserOpt = Optional.of(anInterviewUser);
//		request = new HttpEntity<InterviewUserDTO>(inputData,headers);
//		result = restTemplate.exchange(updateUrl, HttpMethod.POST, request, InterviewUserDTO.class);
//		// 403 response 
//		assertEquals(403, result.getStatusCodeValue());
//		// ADMIN who is trying to change a SUPER ADMINn - should be a 403
//		addRolesAndAuthorizationsToUser(Roles.ROLE_SUPER,anInterviewUserDTO);
//		anInterviewUser = anInterviewUserDTO.toEntity();
//		anInterviewUser.setUserName("x");
//		when(interviewUserRepository.findUserByUserName(Mockito.anyString())).thenReturn(anInterviewUser);
//		when(interviewUserRepository.findUserByEmailAddr(Mockito.anyString())).thenReturn(anInterviewUser);
//		when(interviewUserRepository.save(Mockito.any(InterviewUser.class))).thenReturn(anInterviewUser);
//		inputData = new InterviewUserDTOWrapper(anInterviewUserDTO);
//		inputData.setEmailAddr(B);
//		inputData.setFirstName(B);
//		inputData.setLastName(B);
//		inputData.setNewUserName("x");
//		inputData.setUserName("x");
//		inputData.setPassword(B);
//		inputData.setUserNotLocked(false);
//		inputData.setUserActive(false);
//		anInterviewUserOpt = Optional.of(anInterviewUser);
//		request = new HttpEntity<InterviewUserDTO>(inputData,headers);
//		result = restTemplate.exchange(updateUrl, HttpMethod.POST, request, InterviewUserDTO.class);
//		// 403 response 
//		assertEquals(403, result.getStatusCodeValue());
//		assertTrue(true);
    }
	
	/**
	 * test deleting an InterviewUser using a normal user 
	 * this should fail and produce a 403 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testDeleteInterviewUserWithUser() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		int num = 1;
		final String deleteUrl = DELETE_URL + num;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<InterviewUserDTO> request = new HttpEntity<>(headers);
		ResponseEntity<String> result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, String.class);
		// Verify 403 status
		assertEquals(403, result.getStatusCodeValue());
	}
	
	/**
	 * test deleting an InterviewUser using an admin user
	 * this should succeed and produce a 200 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testDeleteInterviewUserWithAdmin() throws URISyntaxException
	{
		// ADMIN who is trying to delete a BASIC USER- should be a 200
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		String username = "test";
		InterviewUserDTO anInterviewUserDTO = HelperUtility.generateRandomInterviewUserDTO();
		HelperUtility.addRolesAndAuthorizationsToUser(Roles.ROLE_BASIC_USER,anInterviewUserDTO);
		anInterviewUserDTO.setUserName(username); 
		InterviewUser anInterviewUser = anInterviewUserDTO.toEntity();
		Optional<InterviewUser> anInterviewUserOpt = Optional.of(anInterviewUser);
		doNothing().when(interviewUserRepository).deleteById(Mockito.anyInt());
		when(interviewUserRepository.findUserByUserName(Mockito.anyString())).thenReturn(anInterviewUser);
		//when(interviewUserRepository.findById(num)).thenReturn(anInterviewUserOpt);
		final String deleteUrl = DELETE_URL + username;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<InterviewUserDTO> request = new HttpEntity<>(headers);
		ResponseEntity<ResponseMessage> result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, ResponseMessage.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		assertEquals(Constants.DELETED_MSG,result.getBody().getMessage());
		// ADMIN who is trying to delete a ADVANCED USER- should be a 200
		HelperUtility.addRolesAndAuthorizationsToUser(Roles.ROLE_ADV_USER,anInterviewUserDTO);
		anInterviewUserDTO.setUserName(username); 
		anInterviewUser = anInterviewUserDTO.toEntity();
		anInterviewUserOpt = Optional.of(anInterviewUser);
		doNothing().when(interviewUserRepository).deleteById(Mockito.anyInt());
		when(interviewUserRepository.findUserByUserName(Mockito.anyString())).thenReturn(anInterviewUser);
		//when(interviewUserRepository.findById(num)).thenReturn(anInterviewUserOpt);
		headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		request = new HttpEntity<>(headers);
		result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, ResponseMessage.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		assertEquals(Constants.DELETED_MSG,result.getBody().getMessage());
		// ADMIN who is trying to delete a PREMIUM USER- should be a 200
		HelperUtility.addRolesAndAuthorizationsToUser(Roles.ROLE_PREMIUM_USER,anInterviewUserDTO);
		anInterviewUserDTO.setUserName(username); 
		anInterviewUser = anInterviewUserDTO.toEntity();
		anInterviewUserOpt = Optional.of(anInterviewUser);
		doNothing().when(interviewUserRepository).deleteById(Mockito.anyInt());
		when(interviewUserRepository.findUserByUserName(Mockito.anyString())).thenReturn(anInterviewUser);
		//when(interviewUserRepository.findById(num)).thenReturn(anInterviewUserOpt);
		headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		request = new HttpEntity<>(headers);
		result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, ResponseMessage.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		assertEquals(Constants.DELETED_MSG,result.getBody().getMessage());
		// ADMIN who is trying to delete a ADMIN - should be a 403
		HelperUtility.addRolesAndAuthorizationsToUser(Roles.ROLE_ADMIN,anInterviewUserDTO);
		anInterviewUserDTO.setUserName(username); 
		anInterviewUser = anInterviewUserDTO.toEntity();
		anInterviewUserOpt = Optional.of(anInterviewUser);
		doNothing().when(interviewUserRepository).deleteById(Mockito.anyInt());
		when(interviewUserRepository.findUserByUserName(Mockito.anyString())).thenReturn(anInterviewUser);
		//when(interviewUserRepository.findById(num)).thenReturn(anInterviewUserOpt);
		headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		request = new HttpEntity<>(headers);
		result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, ResponseMessage.class);
		// Verify request succeed
		assertEquals(403, result.getStatusCodeValue());
		// ADMIN who is trying to delete a SUPER ADMIN - should be a 403
		HelperUtility.addRolesAndAuthorizationsToUser(Roles.ROLE_SUPER,anInterviewUserDTO);
		anInterviewUserDTO.setUserName(username); 
		anInterviewUser = anInterviewUserDTO.toEntity();
		anInterviewUserOpt = Optional.of(anInterviewUser);
		doNothing().when(interviewUserRepository).deleteById(Mockito.anyInt());
		when(interviewUserRepository.findUserByUserName(Mockito.anyString())).thenReturn(anInterviewUser);
		//when(interviewUserRepository.findById(num)).thenReturn(anInterviewUserOpt);
		headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		request = new HttpEntity<>(headers);
		result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, ResponseMessage.class);
		// Verify request succeed
		assertEquals(403, result.getStatusCodeValue());
	}

	/**
	 * test deleting an InterviewUser using a super admin user
	 * all cases should succeed and produce a 200 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testDeleteInterviewUserWithSuperAdmin() throws URISyntaxException
	{
		/*
		 * TODO - make a super admin test user and fill out this method
		 */
		// ADMIN who is trying to delete a BASIC USER- should be a 200
//		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
//		String username = "test";
//		InterviewUserDTO anInterviewUserDTO = HelperUtility.generateRandomInterviewUserDTO();
//		addRolesAndAuthorizationsToUser(Roles.ROLE_BASIC_USER,anInterviewUserDTO);
//		anInterviewUserDTO.setUserName(username); 
//		InterviewUser anInterviewUser = anInterviewUserDTO.toEntity();
//		Optional<InterviewUser> anInterviewUserOpt = Optional.of(anInterviewUser);
//		doNothing().when(interviewUserRepository).deleteById(Mockito.anyInt());
//		when(interviewUserRepository.findUserByUserName(Mockito.anyString())).thenReturn(anInterviewUser);
//		//when(interviewUserRepository.findById(num)).thenReturn(anInterviewUserOpt);
//		final String deleteUrl = DELETE_URL + username;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<InterviewUserDTO> request = new HttpEntity<>(headers);
//		ResponseEntity<ResponseMessage> result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, ResponseMessage.class);
//		// Verify request succeed
//		assertEquals(200, result.getStatusCodeValue());
//		assertEquals(Constants.DELETED_MSG,result.getBody().getMessage());
//		// ADMIN who is trying to delete a ADVANCED USER- should be a 200
//		addRolesAndAuthorizationsToUser(Roles.ROLE_ADV_USER,anInterviewUserDTO);
//		anInterviewUserDTO.setUserName(username); 
//		anInterviewUser = anInterviewUserDTO.toEntity();
//		anInterviewUserOpt = Optional.of(anInterviewUser);
//		doNothing().when(interviewUserRepository).deleteById(Mockito.anyInt());
//		when(interviewUserRepository.findUserByUserName(Mockito.anyString())).thenReturn(anInterviewUser);
//		//when(interviewUserRepository.findById(num)).thenReturn(anInterviewUserOpt);
//		headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		request = new HttpEntity<>(headers);
//		result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, ResponseMessage.class);
//		// Verify request succeed
//		assertEquals(200, result.getStatusCodeValue());
//		assertEquals(Constants.DELETED_MSG,result.getBody().getMessage());
//		// ADMIN who is trying to delete a PREMIUM USER- should be a 200
//		addRolesAndAuthorizationsToUser(Roles.ROLE_PREMIUM_USER,anInterviewUserDTO);
//		anInterviewUserDTO.setUserName(username); 
//		anInterviewUser = anInterviewUserDTO.toEntity();
//		anInterviewUserOpt = Optional.of(anInterviewUser);
//		doNothing().when(interviewUserRepository).deleteById(Mockito.anyInt());
//		when(interviewUserRepository.findUserByUserName(Mockito.anyString())).thenReturn(anInterviewUser);
//		//when(interviewUserRepository.findById(num)).thenReturn(anInterviewUserOpt);
//		headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		request = new HttpEntity<>(headers);
//		result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, ResponseMessage.class);
//		// Verify request succeed
//		assertEquals(200, result.getStatusCodeValue());
//		assertEquals(Constants.DELETED_MSG,result.getBody().getMessage());
//		// ADMIN who is trying to delete a ADMIN - should be a 403
//		addRolesAndAuthorizationsToUser(Roles.ROLE_ADMIN,anInterviewUserDTO);
//		anInterviewUserDTO.setUserName(username); 
//		anInterviewUser = anInterviewUserDTO.toEntity();
//		anInterviewUserOpt = Optional.of(anInterviewUser);
//		doNothing().when(interviewUserRepository).deleteById(Mockito.anyInt());
//		when(interviewUserRepository.findUserByUserName(Mockito.anyString())).thenReturn(anInterviewUser);
//		//when(interviewUserRepository.findById(num)).thenReturn(anInterviewUserOpt);
//		headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		request = new HttpEntity<>(headers);
//		result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, ResponseMessage.class);
//		// Verify request succeed
//		assertEquals(403, result.getStatusCodeValue());
//		// ADMIN who is trying to delete a SUPER ADMIN - should be a 403
//		addRolesAndAuthorizationsToUser(Roles.ROLE_SUPER,anInterviewUserDTO);
//		anInterviewUserDTO.setUserName(username); 
//		anInterviewUser = anInterviewUserDTO.toEntity();
//		anInterviewUserOpt = Optional.of(anInterviewUser);
//		doNothing().when(interviewUserRepository).deleteById(Mockito.anyInt());
//		when(interviewUserRepository.findUserByUserName(Mockito.anyString())).thenReturn(anInterviewUser);
//		//when(interviewUserRepository.findById(num)).thenReturn(anInterviewUserOpt);
//		headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		request = new HttpEntity<>(headers);
//		result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, ResponseMessage.class);
//		// Verify request succeed
//		assertEquals(403, result.getStatusCodeValue());
//		assertTrue(true);
	}
	
	public static InterviewUser generateRandomInterviewUser() {
		InterviewUser record = new InterviewUser();
		String[] stringarray = new String[1];
		stringarray[0] = Randomizer.randomString(3);
		record.setAuthorizations(stringarray);
		record.setEmailAddr(Randomizer.randomString(20));
		record.setFirstName(Randomizer.randomString(20));
		record.setUserActive(Randomizer.randomBoolean());
		record.setUserNotLocked(Randomizer.randomBoolean());
		record.setJoinDate(Randomizer.randomDate());
		record.setLastLoginDate(Randomizer.randomDate());
		record.setLastLoginDateDisplay(Randomizer.randomDate());
		record.setLastName(Randomizer.randomString(20));
		record.setPassword(Randomizer.randomString(20));
		record.setProfileImage(Randomizer.randomString(20));
		record.setRoles(Randomizer.randomString(20));
		record.setUserIdentifier(Randomizer.randomString(20));
		record.setUserName(Randomizer.randomString(20));
		return record;
	}
	
}