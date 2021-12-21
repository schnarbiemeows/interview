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
//import com.mysql.cj.Constants;
import com.schnarbiesnmeowers.interview.dtos.AnswerDTO;
import com.schnarbiesnmeowers.interview.pojos.Answer;
import com.schnarbiesnmeowers.interview.pojos.ResponseMessage;
import com.schnarbiesnmeowers.interview.services.AnswerRepository;
import com.schnarbiesnmeowers.interview.utilities.Constants;
//import com.schnarbiesnmeowers.interview.business.AnswerBusinessMock;
import com.schnarbiesnmeowers.interview.utilities.Randomizer;
/**
 * this class tests the AnswerController class
 * these tests we want to run in order
 * @author Dylan I. Kessler
 *
 */
////@RunWith(SpringRunner.class)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@Spring_X_BootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@TestPropertySource(properties = "spring.datasource.hikari.maximum-pool-size=1")
public class AnswerControllerTest {

	public static final String CREATE_URL = "/answer/create";
    public static final String UPDATE_URL = "/answer/update";
    public static final String DELETE_URL = "/answer/delete/";
    public static final String FIND_ALL_URL = "/answer/all";
    public static final String FIND_BY_ID = "/answer/findById/";

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
//	private AnswerRepository repository;
//
//	/**
//	 * test getting all Answer
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testGetAllAnswer() throws URISyntaxException
//	{
//		Answer anAnswer = generateRandomAnswer();
//		List<Answer> answers = new ArrayList<Answer>();
//		answers.add(anAnswer);
//		when(repository.findAll()).thenReturn(answers);
//		final String baseUrl = FIND_ALL_URL;
//		HttpEntity<String> request = new HttpEntity<>(new String());
//		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
//		// Verify request succeed
//		assertEquals(200, result.getStatusCodeValue());
//		Gson gson = new Gson();
//		List<AnswerDTO> responseBody = gson.fromJson(result.getBody(),new TypeToken<List<AnswerDTO>>(){}.getType());
//		assertEquals(1,responseBody.size());
//	}
//
//	/**
//	 * test getting a single Answer by primary key
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testGetAnswer() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
//		int num = 1;
//		//Optional<Answer> anAnswer = Optional.of(generateRandomAnswer());
//		Answer anAnswer = generateRandomAnswer();
//		anAnswer.setAnswerId(num);
//		Optional<Answer> anAnswerOpt = Optional.of(anAnswer);
//		when(repository.findById(num)).thenReturn(anAnswerOpt);
//		final String baseUrl = FIND_BY_ID + num;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<String> request = new HttpEntity<>(headers);
//		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
//		// Verify request succeed
//		assertEquals(200, result.getStatusCodeValue());
//		Gson gson = new Gson();
//		AnswerDTO responseBody = gson.fromJson(result.getBody(),AnswerDTO.class);
//		assertTrue(num==responseBody.getAnswerId());
//	}
//	
//	/**
//	 * test creating a new Answer using a normal user
//	 * this should fail and produce a 403 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testCreateAnswerWithUser() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
//		Answer anAnswer = generateRandomAnswer();
//		AnswerDTO anAnswerDTO = anAnswer.toDTO();
//		Optional<Answer> anAnswerOpt = Optional.of(anAnswer);
//		when(repository.save(anAnswer)).thenReturn(anAnswer);
//		final String createUrl = CREATE_URL;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<AnswerDTO> request = new HttpEntity<AnswerDTO>(anAnswerDTO,headers);
//		ResponseEntity<String> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, String.class);
//		// 403 response from normal user
//		assertEquals(403, result.getStatusCodeValue());
//		
//    }
//	
//	/**
//	 * test creating a new Answer using an admin
//	 * this should succeed and produce a 201 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testCreateAnswerWithAdmin() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
//		Answer anAnswer = generateRandomAnswer();
//		AnswerDTO anAnswerDTO = anAnswer.toDTO();
//		Optional<Answer> anAnswerOpt = Optional.of(anAnswer);
//		when(repository.save(any(Answer.class))).thenReturn(anAnswer);
//		final String createUrl = CREATE_URL;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<AnswerDTO> request = new HttpEntity<AnswerDTO>(anAnswerDTO,headers);
//		ResponseEntity<AnswerDTO> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, AnswerDTO.class);
//		// 201 response from normal user
//		assertEquals(201, result.getStatusCodeValue());
//		assertTrue(result.getBody().getAnswerTxt().equals(anAnswerDTO.getAnswerTxt()));
//    }
//
//	/**
//	 * test updating an Answer using a normal user 
//	 * this should fail and produce a 403 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testUpdateAnswerWithUser() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
//		Answer anAnswer = generateRandomAnswer();
//		AnswerDTO anAnswerDTO = anAnswer.toDTO();
//		Optional<Answer> anAnswerOpt = Optional.of(anAnswer);
//		when(repository.save(anAnswer)).thenReturn(anAnswer);
//		final String createUrl = UPDATE_URL;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<AnswerDTO> request = new HttpEntity<AnswerDTO>(anAnswerDTO,headers);
//		ResponseEntity<String> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, String.class);
//		// 403 response from normal user
//		assertEquals(403, result.getStatusCodeValue());	
//    }
//	
//	/**
//	 * test updating an Answer using an admin user
//	 * this should succeed and produce a 200 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testUpdateAnswerWithAdmin() throws URISyntaxException
//	{
//		int num = 1;
//		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
//		Answer anAnswer = generateRandomAnswer();
//		anAnswer.setAnswerId(num);
//		AnswerDTO anAnswerDTO = anAnswer.toDTO();
//		Optional<Answer> anAnswerOpt = Optional.of(anAnswer);
//		when(repository.save(any(Answer.class))).thenReturn(anAnswer);
//		when(repository.findById(num)).thenReturn(anAnswerOpt);
//		final String createUrl = UPDATE_URL;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<AnswerDTO> request = new HttpEntity<AnswerDTO>(anAnswerDTO,headers);
//		ResponseEntity<AnswerDTO> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, AnswerDTO.class);
//		// 200 response from normal user
//		assertEquals(200, result.getStatusCodeValue());
//		assertTrue(result.getBody().getAnswerTxt().equals(anAnswerDTO.getAnswerTxt()));
//    }
//
//	/**
//	 * test deleting an Answer using a normal user 
//	 * this should fail and produce a 403 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testDeleteAnswerWithUser() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
//		int num = 1;
//		final String deleteUrl = DELETE_URL + num;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<AnswerDTO> request = new HttpEntity<>(headers);
//		ResponseEntity<String> result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, String.class);
//		// Verify 403 status
//		assertEquals(403, result.getStatusCodeValue());
//	}
//	
//	/**
//	 * test deleting an Answer using an admin user
//	 * this should succeed and produce a 200 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testDeleteAnswerWithAdmin() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
//		int num = 1;
//		Answer anAnswer = generateRandomAnswer();
//		anAnswer.setAnswerId(num);
//		AnswerDTO anAnswerDTO = anAnswer.toDTO();
//		Optional<Answer> anAnswerOpt = Optional.of(anAnswer);
//		doNothing().when(repository).deleteById(num);
//		when(repository.findById(num)).thenReturn(anAnswerOpt);
//		final String deleteUrl = DELETE_URL + num;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<AnswerDTO> request = new HttpEntity<>(headers);
//		ResponseEntity<ResponseMessage> result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, ResponseMessage.class);
//		// Verify request succeed
//		assertEquals(200, result.getStatusCodeValue());
//		assertEquals(Constants.DELETED_MSG,result.getBody().getMessage());
//	}
//
//	public static AnswerDTO generateRandomAnswerDTO() {
//		AnswerDTO record = new AnswerDTO();
//		record.setAnswerTxt(Randomizer.randomString(20));
//		record.setEvntTmestmp(Randomizer.randomDate());
//		record.setEvntOperId(Randomizer.randomString(10));
//		return record;
//	}
//	
//	public static Answer generateRandomAnswer() {
//		Answer record = new Answer();
//		record.setAnswerTxt(Randomizer.randomString(20));
//		record.setEvntTmestmp(Randomizer.randomDate());
//		record.setEvntOperId(Randomizer.randomString(10));
//		return record;
//	}
}