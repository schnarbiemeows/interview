package com.schnarbiesnmeowers.interview.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.net.URI;
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
import com.schnarbiesnmeowers.interview.dtos.QuestionDTO;
import com.schnarbiesnmeowers.interview.pojos.Question;
import com.schnarbiesnmeowers.interview.pojos.ResponseMessage;
import com.schnarbiesnmeowers.interview.services.QuestionRepository;
import com.schnarbiesnmeowers.interview.utilities.Constants;
import com.schnarbiesnmeowers.interview.utilities.Randomizer;

/**
 * this class tests the QuestionController class
 * these tests we want to run in order
 * @author Dylan I. Kessler
 *
 */
//@RunWith(SpringRunner.class)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//Spring_X_BootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@TestPropertySource(properties = "spring.datasource.hikari.maximum-pool-size=1")
public class QuestionControllerTest {

	public static final String Q_CREATE_URL = "/question/create";
    public static final String Q_UPDATE_URL = "/question/update";
    public static final String Q_DELETE_URL = "/question/delete/";
    public static final String Q_FIND_ALL_URL = "/question/all";
    public static final String Q_FIND_BY_ID = "/question/findById/";
    public static final String Q_FIND_BY_CATEGORY_ID = "/question/findByQuestionCategoryId/";
    public static final String Q_FIND_BY_LEVEL_ID = "/question/findByQuestionLevelId/";
    public static final String Q_FIND_BY_ANSWER_ID =  "/question/findByAnswerId/";
    public static final String Q_FIND_BY_ALL_FKS = "/question/findByQuestionCategoryIdAndQuestionLevelIdAndAnswerId/";
    
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
//	private QuestionRepository questionRepository;
//
//	/**
//	 * test getting all Question
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testGetAllQuestion() throws URISyntaxException
//	{
//		Question anQuestion = generateRandomQuestion();
//		List<Question> answers = new ArrayList<Question>();
//		answers.add(anQuestion);
//		when(questionRepository.findAll()).thenReturn(answers);
//		final String baseUrl = Q_FIND_ALL_URL;
//		HttpEntity<String> request = new HttpEntity<>(new String());
//		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
//		// Verify request succeed
//		assertEquals(200, result.getStatusCodeValue());
//		Gson gson = new Gson();
//		List<QuestionDTO> responseBody = gson.fromJson(result.getBody(),new TypeToken<List<QuestionDTO>>(){}.getType());
//		assertEquals(1,responseBody.size());
//	}
//
//	/**
//	 * test getting a single Question by primary key
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testGetQuestion() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
//		int num = 1;
//		//Optional<Question> anQuestion = Optional.of(generateRandomQuestion());
//		Question anQuestion = generateRandomQuestion();
//		anQuestion.setQuestionId(num);
//		Optional<Question> anQuestionOpt = Optional.of(anQuestion);
//		when(questionRepository.findById(num)).thenReturn(anQuestionOpt);
//		final String baseUrl = Q_FIND_BY_ID + num;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<String> request = new HttpEntity<>(headers);
//		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
//		// Verify request succeed
//		assertEquals(200, result.getStatusCodeValue());
//		Gson gson = new Gson();
//		QuestionDTO responseBody = gson.fromJson(result.getBody(),QuestionDTO.class);
//		assertTrue(num==responseBody.getQuestionId());
//	}
//	
//	/**
//	 * test creating a new Question using a normal user
//	 * this should fail and produce a 403 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testCreateQuestionWithUser() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
//		Question anQuestion = generateRandomQuestion();
//		QuestionDTO anQuestionDTO = anQuestion.toDTO();
//		Optional<Question> anQuestionOpt = Optional.of(anQuestion);
//		when(questionRepository.save(anQuestion)).thenReturn(anQuestion);
//		final String createUrl = Q_CREATE_URL;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<QuestionDTO> request = new HttpEntity<QuestionDTO>(anQuestionDTO,headers);
//		ResponseEntity<String> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, String.class);
//		// 403 response from normal user
//		assertEquals(403, result.getStatusCodeValue());
//		
//    }
//	
//	/**
//	 * test creating a new Question using an admin
//	 * this should succeed and produce a 201 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testCreateQuestionWithAdmin() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
//		Question anQuestion = generateRandomQuestion();
//		QuestionDTO anQuestionDTO = anQuestion.toDTO();
//		Optional<Question> anQuestionOpt = Optional.of(anQuestion);
//		when(questionRepository.save(any(Question.class))).thenReturn(anQuestion);
//		final String createUrl = Q_CREATE_URL;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<QuestionDTO> request = new HttpEntity<QuestionDTO>(anQuestionDTO,headers);
//		ResponseEntity<QuestionDTO> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, QuestionDTO.class);
//		// 201 response from normal user
//		assertEquals(201, result.getStatusCodeValue());
//		assertTrue(result.getBody().getQuestionTxt().equals(anQuestionDTO.getQuestionTxt()));
//    }
//
//	/**
//	 * test updating an Question using a normal user 
//	 * this should fail and produce a 403 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testUpdateQuestionWithUser() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
//		Question anQuestion = generateRandomQuestion();
//		QuestionDTO anQuestionDTO = anQuestion.toDTO();
//		Optional<Question> anQuestionOpt = Optional.of(anQuestion);
//		when(questionRepository.save(anQuestion)).thenReturn(anQuestion);
//		final String createUrl = Q_UPDATE_URL;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<QuestionDTO> request = new HttpEntity<QuestionDTO>(anQuestionDTO,headers);
//		ResponseEntity<String> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, String.class);
//		// 403 response from normal user
//		assertEquals(403, result.getStatusCodeValue());	
//    }
//	
//	/**
//	 * test updating an Question using an admin user
//	 * this should succeed and produce a 200 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testUpdateQuestionWithAdmin() throws URISyntaxException
//	{
//		int num = 1;
//		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
//		Question anQuestion = generateRandomQuestion();
//		anQuestion.setQuestionId(num);
//		QuestionDTO anQuestionDTO = anQuestion.toDTO();
//		Optional<Question> anQuestionOpt = Optional.of(anQuestion);
//		when(questionRepository.save(any(Question.class))).thenReturn(anQuestion);
//		when(questionRepository.findById(num)).thenReturn(anQuestionOpt);
//		final String createUrl = Q_UPDATE_URL;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<QuestionDTO> request = new HttpEntity<QuestionDTO>(anQuestionDTO,headers);
//		ResponseEntity<QuestionDTO> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, QuestionDTO.class);
//		// 200 response from normal user
//		assertEquals(200, result.getStatusCodeValue());
//		assertTrue(result.getBody().getQuestionTxt().equals(anQuestionDTO.getQuestionTxt()));
//    }
//
//	/**
//	 * test deleting an Question using a normal user 
//	 * this should fail and produce a 403 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testDeleteQuestionWithUser() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
//		int num = 1;
//		final String deleteUrl = Q_DELETE_URL + num;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<QuestionDTO> request = new HttpEntity<>(headers);
//		ResponseEntity<String> result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, String.class);
//		// Verify 403 status
//		assertEquals(403, result.getStatusCodeValue());
//	}
//	
//	/**
//	 * test deleting an Question using an admin user
//	 * this should succeed and produce a 200 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testDeleteQuestionWithAdmin() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
//		int num = 1;
//		Question anQuestion = generateRandomQuestion();
//		anQuestion.setQuestionId(num);
//		QuestionDTO anQuestionDTO = anQuestion.toDTO();
//		Optional<Question> anQuestionOpt = Optional.of(anQuestion);
//		doNothing().when(questionRepository).deleteById(num);
//		when(questionRepository.findById(num)).thenReturn(anQuestionOpt);
//		final String deleteUrl = Q_DELETE_URL + num;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<QuestionDTO> request = new HttpEntity<>(headers);
//		ResponseEntity<ResponseMessage> result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, ResponseMessage.class);
//		// Verify request succeed
//		assertEquals(200, result.getStatusCodeValue());
//		assertEquals(Constants.DELETED_MSG,result.getBody().getMessage());
//	}
//
//	/**
//	 * test getting all Question by foreign key questionCategoryId
//	 * @throws URISyntaxException
//	*/
//	@Test
//	public void testGetQuestionByQuestionCategoryId() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
//		int num = 1;
//		Question anQuestion = generateRandomQuestion();
//		anQuestion.setQuestionCategoryId(num);
//		List<Question> questionList = new ArrayList<Question>();
//		questionList.add(anQuestion);
//		when(questionRepository.findQuestionByQuestionCategoryId(num)).thenReturn(questionList);
//		final String baseUrl = Q_FIND_BY_CATEGORY_ID + num;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<String> request = new HttpEntity<>(headers);
//		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
//		// Verify request succeed
//		assertEquals(200, result.getStatusCodeValue());
//		Gson gson = new Gson();
//		List<QuestionDTO> responseBody = gson.fromJson(result.getBody(),new TypeToken<List<QuestionDTO>>(){}.getType());
//		assertEquals(1,responseBody.size());
//	}
//	
//	/**
//	 * test getting all Question by foreign key questionLevelId
//	 * @throws URISyntaxException
//	*/
//	@Test
//	public void testGetQuestionByQuestionLevelId() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
//		int num = 1;
//		Question anQuestion = generateRandomQuestion();
//		anQuestion.setQuestionLevelId(num);
//		List<Question> questionList = new ArrayList<Question>();
//		questionList.add(anQuestion);
//		when(questionRepository.findQuestionByQuestionLevelId(num)).thenReturn(questionList);
//		final String baseUrl = Q_FIND_BY_LEVEL_ID + num;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<String> request = new HttpEntity<>(headers);
//		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
//		// Verify request succeed
//		assertEquals(200, result.getStatusCodeValue());
//		Gson gson = new Gson();
//		List<QuestionDTO> responseBody = gson.fromJson(result.getBody(),new TypeToken<List<QuestionDTO>>(){}.getType());
//		assertEquals(1,responseBody.size());
//	}
//
//	/**
//	 * test getting all Question by foreign key answerId
//	 * @throws URISyntaxException
//	*/
//	@Test
//	public void testGetQuestionByAnswerId() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
//		int num = 1;
//		Question anQuestion = generateRandomQuestion();
//		anQuestion.setAnswerId(num);
//		List<Question> questionList = new ArrayList<Question>();
//		questionList.add(anQuestion);
//		when(questionRepository.findQuestionByAnswerId(num)).thenReturn(questionList);
//		final String baseUrl = Q_FIND_BY_ANSWER_ID + num;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<String> request = new HttpEntity<>(headers);
//		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
//		// Verify request succeed
//		assertEquals(200, result.getStatusCodeValue());
//		Gson gson = new Gson();
//		List<QuestionDTO> responseBody = gson.fromJson(result.getBody(),new TypeToken<List<QuestionDTO>>(){}.getType());
//		assertEquals(1,responseBody.size());
//	}
//	
//	/**
//	 * test getting all Question by all foreign keys
//	 * @throws URISyntaxException
//	*/
//	@Test
//	public void testGetQuestionByQuestionCategoryIdAndQuestionLevelIdAndAnswerId() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
//		int num1 = 1;
//		int num2 = 1;
//		int num3 = 1;
//		Question anQuestion = generateRandomQuestion();
//		anQuestion.setQuestionCategoryId(num1);
//		anQuestion.setQuestionLevelId(num2);
//		anQuestion.setAnswerId(num3);
//		List<Question> questionList = new ArrayList<Question>();
//		questionList.add(anQuestion);
//		when(questionRepository.findQuestionByQuestionCategoryIdAndQuestionLevelIdAndAnswerId(num1,num2,num3)).thenReturn(questionList);
//		final String baseUrl = Q_FIND_BY_ALL_FKS + num1 + "/" + num2 + "/" + num3;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<String> request = new HttpEntity<>(headers);
//		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
//		// Verify request succeed
//		assertEquals(200, result.getStatusCodeValue());
//		Gson gson = new Gson();
//		List<QuestionDTO> responseBody = gson.fromJson(result.getBody(),new TypeToken<List<QuestionDTO>>(){}.getType());
//		assertEquals(1,responseBody.size());
//	}
//
//	public static QuestionDTO generateRandomQuestionDTO() {
//		QuestionDTO record = new QuestionDTO();
//		record.setQuestionTxt(Randomizer.randomString(20));
//		record.setEvntTmestmp(Randomizer.randomDate());
//		record.setEvntOperId(Randomizer.randomString(10));
//		return record;
//	}
//	
//	public static Question generateRandomQuestion() {
//		Question record = new Question();
//		record.setQuestionTxt(Randomizer.randomString(20));
//		record.setEvntTmestmp(Randomizer.randomDate());
//		record.setEvntOperId(Randomizer.randomString(10));
//		return record;
//	}
}