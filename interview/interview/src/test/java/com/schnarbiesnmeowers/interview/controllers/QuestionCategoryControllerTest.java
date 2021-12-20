package com.schnarbiesnmeowers.interview.controllers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.math.*;
import java.util.*;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.schnarbiesnmeowers.interview.pojos.QuestionCategory;
import com.schnarbiesnmeowers.interview.pojos.QuestionCategory;
import com.schnarbiesnmeowers.interview.pojos.ResponseMessage;
import com.schnarbiesnmeowers.interview.services.QuestionCategoryRepository;
import com.schnarbiesnmeowers.interview.dtos.QuestionCategoryDTO;
import com.schnarbiesnmeowers.interview.dtos.QuestionCategoryDTO;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.schnarbiesnmeowers.interview.business.QuestionCategoryBusiness;
import com.schnarbiesnmeowers.interview.utilities.Constants;
import com.schnarbiesnmeowers.interview.utilities.Randomizer;

/**
 * this class tests the QuestionCategoryController class
 * these tests we want to run in order
 * @author Dylan I. Kessler
 *
 */
//@RunWith(SpringRunner.class)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@Spring_X_BootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@TestPropertySource(properties = "spring.datasource.hikari.maximum-pool-size=1")
public class QuestionCategoryControllerTest {

	public static final String QC_CREATE_URL = "/questioncategory/create";
    public static final String QC_UPDATE_URL = "/questioncategory/update";
    public static final String QC_DELETE_URL = "/questioncategory/delete/";
    public static final String QC_FIND_ALL_URL = "/questioncategory/all";
    public static final String QC_FIND_BY_ID = "/questioncategory/findById/";

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
//	private QuestionCategoryRepository questionCategoryRepository;
//
//	/**
//	 * test getting all QuestionCategory
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testGetAllQuestionCategory() throws URISyntaxException
//	{
//		QuestionCategory anQuestionCategory = generateRandomQuestionCategory();
//		List<QuestionCategory> answers = new ArrayList<QuestionCategory>();
//		answers.add(anQuestionCategory);
//		when(questionCategoryRepository.findAll()).thenReturn(answers);
//		final String baseUrl = QC_FIND_ALL_URL;
//		HttpEntity<String> request = new HttpEntity<>(new String());
//		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
//		// Verify request succeed
//		assertEquals(200, result.getStatusCodeValue());
//		Gson gson = new Gson();
//		List<QuestionCategoryDTO> responseBody = gson.fromJson(result.getBody(),new TypeToken<List<QuestionCategoryDTO>>(){}.getType());
//		assertEquals(1,responseBody.size());
//	}
//
//	/**
//	 * test getting a single QuestionCategory by primary key
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testGetQuestionCategory() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
//		int num = 1;
//		//Optional<QuestionCategory> anQuestionCategory = Optional.of(generateRandomQuestionCategory());
//		QuestionCategory anQuestionCategory = generateRandomQuestionCategory();
//		anQuestionCategory.setQuestionCategoryId(num);
//		Optional<QuestionCategory> anQuestionCategoryOpt = Optional.of(anQuestionCategory);
//		when(questionCategoryRepository.findById(num)).thenReturn(anQuestionCategoryOpt);
//		final String baseUrl = QC_FIND_BY_ID + num;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<String> request = new HttpEntity<>(headers);
//		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
//		// Verify request succeed
//		assertEquals(200, result.getStatusCodeValue());
//		Gson gson = new Gson();
//		QuestionCategoryDTO responseBody = gson.fromJson(result.getBody(),QuestionCategoryDTO.class);
//		assertTrue(num==responseBody.getQuestionCategoryId());
//	}
//	
//	/**
//	 * test creating a new QuestionCategory using a normal user
//	 * this should fail and produce a 403 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testCreateQuestionCategoryWithUser() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
//		QuestionCategory anQuestionCategory = generateRandomQuestionCategory();
//		QuestionCategoryDTO anQuestionCategoryDTO = anQuestionCategory.toDTO();
//		Optional<QuestionCategory> anQuestionCategoryOpt = Optional.of(anQuestionCategory);
//		when(questionCategoryRepository.save(anQuestionCategory)).thenReturn(anQuestionCategory);
//		final String createUrl = QC_CREATE_URL;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<QuestionCategoryDTO> request = new HttpEntity<QuestionCategoryDTO>(anQuestionCategoryDTO,headers);
//		ResponseEntity<String> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, String.class);
//		// 403 response from normal user
//		assertEquals(403, result.getStatusCodeValue());
//		
//    }
//	
//	/**
//	 * test creating a new QuestionCategory using an admin
//	 * this should succeed and produce a 201 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testCreateQuestionCategoryWithAdmin() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
//		QuestionCategory anQuestionCategory = generateRandomQuestionCategory();
//		QuestionCategoryDTO anQuestionCategoryDTO = anQuestionCategory.toDTO();
//		Optional<QuestionCategory> anQuestionCategoryOpt = Optional.of(anQuestionCategory);
//		when(questionCategoryRepository.save(any(QuestionCategory.class))).thenReturn(anQuestionCategory);
//		final String createUrl = QC_CREATE_URL;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<QuestionCategoryDTO> request = new HttpEntity<QuestionCategoryDTO>(anQuestionCategoryDTO,headers);
//		ResponseEntity<QuestionCategoryDTO> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, QuestionCategoryDTO.class);
//		// 201 response from normal user
//		assertEquals(201, result.getStatusCodeValue());
//		assertTrue(result.getBody().getQuestionCategoryDesc().equals(anQuestionCategoryDTO.getQuestionCategoryDesc()));
//    }
//
//	/**
//	 * test updating an QuestionCategory using a normal user 
//	 * this should fail and produce a 403 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testUpdateQuestionCategoryWithUser() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
//		QuestionCategory anQuestionCategory = generateRandomQuestionCategory();
//		QuestionCategoryDTO anQuestionCategoryDTO = anQuestionCategory.toDTO();
//		Optional<QuestionCategory> anQuestionCategoryOpt = Optional.of(anQuestionCategory);
//		when(questionCategoryRepository.save(anQuestionCategory)).thenReturn(anQuestionCategory);
//		final String createUrl = QC_UPDATE_URL;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<QuestionCategoryDTO> request = new HttpEntity<QuestionCategoryDTO>(anQuestionCategoryDTO,headers);
//		ResponseEntity<String> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, String.class);
//		// 403 response from normal user
//		assertEquals(403, result.getStatusCodeValue());	
//    }
//	
//	/**
//	 * test updating an QuestionCategory using an admin user
//	 * this should succeed and produce a 200 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testUpdateQuestionCategoryWithAdmin() throws URISyntaxException
//	{
//		int num = 1;
//		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
//		QuestionCategory anQuestionCategory = generateRandomQuestionCategory();
//		anQuestionCategory.setQuestionCategoryId(num);
//		QuestionCategoryDTO anQuestionCategoryDTO = anQuestionCategory.toDTO();
//		Optional<QuestionCategory> anQuestionCategoryOpt = Optional.of(anQuestionCategory);
//		when(questionCategoryRepository.save(any(QuestionCategory.class))).thenReturn(anQuestionCategory);
//		when(questionCategoryRepository.findById(num)).thenReturn(anQuestionCategoryOpt);
//		final String createUrl = QC_UPDATE_URL;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<QuestionCategoryDTO> request = new HttpEntity<QuestionCategoryDTO>(anQuestionCategoryDTO,headers);
//		ResponseEntity<QuestionCategoryDTO> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, QuestionCategoryDTO.class);
//		// 200 response from normal user
//		assertEquals(200, result.getStatusCodeValue());
//		assertTrue(result.getBody().getQuestionCategoryDesc().equals(anQuestionCategoryDTO.getQuestionCategoryDesc()));
//    }
//
//	/**
//	 * test deleting an QuestionCategory using a normal user 
//	 * this should fail and produce a 403 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testDeleteQuestionCategoryWithUser() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
//		int num = 1;
//		final String deleteUrl = QC_DELETE_URL + num;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<QuestionCategoryDTO> request = new HttpEntity<>(headers);
//		ResponseEntity<String> result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, String.class);
//		// Verify 403 status
//		assertEquals(403, result.getStatusCodeValue());
//	}
//	
//	/**
//	 * test deleting an QuestionCategory using an admin user
//	 * this should succeed and produce a 200 status code
//	 * @throws URISyntaxException
//	 */
//	@Test
//	public void testDeleteQuestionCategoryWithAdmin() throws URISyntaxException
//	{
//		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
//		int num = 1;
//		QuestionCategory anQuestionCategory = generateRandomQuestionCategory();
//		anQuestionCategory.setQuestionCategoryId(num);
//		QuestionCategoryDTO anQuestionCategoryDTO = anQuestionCategory.toDTO();
//		Optional<QuestionCategory> anQuestionCategoryOpt = Optional.of(anQuestionCategory);
//		doNothing().when(questionCategoryRepository).deleteById(num);
//		when(questionCategoryRepository.findById(num)).thenReturn(anQuestionCategoryOpt);
//		final String deleteUrl = QC_DELETE_URL + num;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(Constants.AUTHORIZATION, bearerToken);
//		HttpEntity<QuestionCategoryDTO> request = new HttpEntity<>(headers);
//		ResponseEntity<ResponseMessage> result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, ResponseMessage.class);
//		// Verify request succeed
//		assertEquals(200, result.getStatusCodeValue());
//		assertEquals(Constants.DELETED_MSG,result.getBody().getMessage());
//	}
//
//	public static QuestionCategoryDTO generateRandomQuestionCategoryDTO() {
//		QuestionCategoryDTO record = new QuestionCategoryDTO();
//		record.setQuestionCategoryDesc(Randomizer.randomString(20));
//		record.setEvntTmestmp(Randomizer.randomDate());
//		record.setEvntOperId(Randomizer.randomString(10));
//		return record;
//	}
//	
//	public static QuestionCategory generateRandomQuestionCategory() {
//		QuestionCategory record = new QuestionCategory();
//		record.setQuestionCategoryDesc(Randomizer.randomString(20));
//		record.setEvntTmestmp(Randomizer.randomDate());
//		record.setEvntOperId(Randomizer.randomString(10));
//		return record;
//	}
}