package com.schnarbiesnmeowers.interview.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.schnarbiesnmeowers.interview.dtos.QuestionLevelDTO;
import com.schnarbiesnmeowers.interview.pojos.QuestionLevel;
import com.schnarbiesnmeowers.interview.pojos.ResponseMessage;
import com.schnarbiesnmeowers.interview.services.QuestionLevelRepository;
import com.schnarbiesnmeowers.interview.utilities.Constants;
import com.schnarbiesnmeowers.interview.utilities.Randomizer;

/**
 * this class tests the QuestionLevelController class
 * these tests we want to run in order
 * @author Dylan I. Kessler
 *
 */
//@RunWith(SpringRunner.class)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@Spring_X_BootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@TestPropertySource(properties = "spring.datasource.hikari.maximum-pool-size=1")
public class QuestionLevelControllerTest {

	public static final String QL_CREATE_URL = "/questionlevel/create";
    public static final String QL_UPDATE_URL = "/questionlevel/update";
    public static final String QL_DELETE_URL = "/questionlevel/delete/";
    public static final String QL_FIND_ALL_URL = "/questionlevel/all";
    public static final String QL_FIND_BY_ID = "/questionlevel/findById/";

//	/**
//	 * admin user for testing
//	 */
//	@Value("${aaaaaaaaaaaa.token}")
//	private String admintoken;
//
//	/**
//	 * basic user for testing 403 responses
//	 */
//	@Value("${bbbbbbbbbbbb.token}")
//	private String usertoken;
//	
//	/**
//     * inject the Mock into the RestTemplate
//     */
//	@Autowired
//    private TestRestTemplate restTemplate;
//
//	@MockBean
//	private QuestionLevelRepository questionLevelRepository;
//
//	/**
//	 * test getting all QuestionLevel
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testGetAllQuestionLevel() throws URISyntaxException
//	{
//		QuestionLevel anQuestionLevel = generateRandomQuestionLevel();
//		List<QuestionLevel> answers = new ArrayList<QuestionLevel>();
//		answers.add(anQuestionLevel);
//		when(questionLevelRepository.findAll()).thenReturn(answers);
//		final String baseUrl = QL_FIND_ALL_URL;
//		HttpEntity<String> request = new HttpEntity<>(new String());
//		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
//		// Verify request succeed
//		assertEquals(200, result.getStatusCodeValue());
//		Gson gson = new Gson();
//		List<QuestionLevelDTO> responseBody = gson.fromJson(result.getBody(),new TypeToken<List<QuestionLevelDTO>>(){}.getType());
//		assertEquals(1,responseBody.size());
//	}
//
//	/**
//	 * test getting a single QuestionLevel by primary key
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testGetQuestionLevel() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
//		int num = 1;
//		//Optional<QuestionLevel> anQuestionLevel = Optional.of(generateRandomQuestionLevel());
//		QuestionLevel anQuestionLevel = generateRandomQuestionLevel();
//		anQuestionLevel.setQuestionLevelId(num);
//		Optional<QuestionLevel> anQuestionLevelOpt = Optional.of(anQuestionLevel);
//		when(questionLevelRepository.findById(num)).thenReturn(anQuestionLevelOpt);
//		final String baseUrl = QL_FIND_BY_ID + num;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<String> request = new HttpEntity<>(headers);
//		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
//		// Verify request succeed
//		assertEquals(200, result.getStatusCodeValue());
//		Gson gson = new Gson();
//		QuestionLevelDTO responseBody = gson.fromJson(result.getBody(),QuestionLevelDTO.class);
//		assertTrue(num==responseBody.getQuestionLevelId());
//	}
//	
//	/**
//	 * test creating a new QuestionLevel using a normal user
//	 * this should fail and produce a 403 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testCreateQuestionLevelWithUser() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
//		QuestionLevel anQuestionLevel = generateRandomQuestionLevel();
//		QuestionLevelDTO anQuestionLevelDTO = anQuestionLevel.toDTO();
//		Optional<QuestionLevel> anQuestionLevelOpt = Optional.of(anQuestionLevel);
//		when(questionLevelRepository.save(anQuestionLevel)).thenReturn(anQuestionLevel);
//		final String createUrl = QL_CREATE_URL;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<QuestionLevelDTO> request = new HttpEntity<QuestionLevelDTO>(anQuestionLevelDTO,headers);
//		ResponseEntity<String> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, String.class);
//		// 403 response from normal user
//		assertEquals(403, result.getStatusCodeValue());
//		
//    }
//	
//	/**
//	 * test creating a new QuestionLevel using an admin
//	 * this should succeed and produce a 201 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testCreateQuestionLevelWithAdmin() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
//		QuestionLevel anQuestionLevel = generateRandomQuestionLevel();
//		QuestionLevelDTO anQuestionLevelDTO = anQuestionLevel.toDTO();
//		Optional<QuestionLevel> anQuestionLevelOpt = Optional.of(anQuestionLevel);
//		when(questionLevelRepository.save(any(QuestionLevel.class))).thenReturn(anQuestionLevel);
//		final String createUrl = QL_CREATE_URL;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<QuestionLevelDTO> request = new HttpEntity<QuestionLevelDTO>(anQuestionLevelDTO,headers);
//		ResponseEntity<QuestionLevelDTO> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, QuestionLevelDTO.class);
//		// 201 response from normal user
//		assertEquals(201, result.getStatusCodeValue());
//		assertTrue(result.getBody().getQuestionLevelDesc().equals(anQuestionLevelDTO.getQuestionLevelDesc()));
//    }
//
//	/**
//	 * test updating an QuestionLevel using a normal user 
//	 * this should fail and produce a 403 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testUpdateQuestionLevelWithUser() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
//		QuestionLevel anQuestionLevel = generateRandomQuestionLevel();
//		QuestionLevelDTO anQuestionLevelDTO = anQuestionLevel.toDTO();
//		Optional<QuestionLevel> anQuestionLevelOpt = Optional.of(anQuestionLevel);
//		when(questionLevelRepository.save(anQuestionLevel)).thenReturn(anQuestionLevel);
//		final String createUrl = QL_UPDATE_URL;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<QuestionLevelDTO> request = new HttpEntity<QuestionLevelDTO>(anQuestionLevelDTO,headers);
//		ResponseEntity<String> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, String.class);
//		// 403 response from normal user
//		assertEquals(403, result.getStatusCodeValue());	
//    }
//	
//	/**
//	 * test updating an QuestionLevel using an admin user
//	 * this should succeed and produce a 200 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testUpdateQuestionLevelWithAdmin() throws URISyntaxException
//	{
//		int num = 1;
//		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
//		QuestionLevel anQuestionLevel = generateRandomQuestionLevel();
//		anQuestionLevel.setQuestionLevelId(num);
//		QuestionLevelDTO anQuestionLevelDTO = anQuestionLevel.toDTO();
//		Optional<QuestionLevel> anQuestionLevelOpt = Optional.of(anQuestionLevel);
//		when(questionLevelRepository.save(any(QuestionLevel.class))).thenReturn(anQuestionLevel);
//		when(questionLevelRepository.findById(num)).thenReturn(anQuestionLevelOpt);
//		final String createUrl = QL_UPDATE_URL;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<QuestionLevelDTO> request = new HttpEntity<QuestionLevelDTO>(anQuestionLevelDTO,headers);
//		ResponseEntity<QuestionLevelDTO> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, QuestionLevelDTO.class);
//		// 200 response from normal user
//		assertEquals(200, result.getStatusCodeValue());
//		assertTrue(result.getBody().getQuestionLevelDesc().equals(anQuestionLevelDTO.getQuestionLevelDesc()));
//    }
//
//	/**
//	 * test deleting an QuestionLevel using a normal user 
//	 * this should fail and produce a 403 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testDeleteQuestionLevelWithUser() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
//		int num = 1;
//		final String deleteUrl = QL_DELETE_URL + num;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<QuestionLevelDTO> request = new HttpEntity<>(headers);
//		ResponseEntity<String> result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, String.class);
//		// Verify 403 status
//		assertEquals(403, result.getStatusCodeValue());
//	}
//	
//	/**
//	 * test deleting an QuestionLevel using an admin user
//	 * this should succeed and produce a 200 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testDeleteQuestionLevelWithAdmin() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
//		int num = 1;
//		QuestionLevel anQuestionLevel = generateRandomQuestionLevel();
//		anQuestionLevel.setQuestionLevelId(num);
//		QuestionLevelDTO anQuestionLevelDTO = anQuestionLevel.toDTO();
//		Optional<QuestionLevel> anQuestionLevelOpt = Optional.of(anQuestionLevel);
//		doNothing().when(questionLevelRepository).deleteById(num);
//		when(questionLevelRepository.findById(num)).thenReturn(anQuestionLevelOpt);
//		final String deleteUrl = QL_DELETE_URL + num;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<QuestionLevelDTO> request = new HttpEntity<>(headers);
//		ResponseEntity<ResponseMessage> result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, ResponseMessage.class);
//		// Verify request succeed
//		assertEquals(200, result.getStatusCodeValue());
//		assertEquals(Constants.DELETED_MSG,result.getBody().getMessage());
//	}
//
//	public static QuestionLevelDTO generateRandomQuestionLevelDTO() {
//		QuestionLevelDTO record = new QuestionLevelDTO();
//		record.setQuestionLevelDesc(Randomizer.randomString(20));
//		record.setEvntTmestmp(Randomizer.randomDate());
//		record.setEvntOperId(Randomizer.randomString(10));
//		return record;
//	}
//	
//	public static QuestionLevel generateRandomQuestionLevel() {
//		QuestionLevel record = new QuestionLevel();
//		record.setQuestionLevelDesc(Randomizer.randomString(20));
//		record.setEvntTmestmp(Randomizer.randomDate());
//		record.setEvntOperId(Randomizer.randomString(10));
//		return record;
//	}
}