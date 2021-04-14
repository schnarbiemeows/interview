package com.schnarbiesnmeowers.interview.controllers;

import static org.junit.Assert.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.schnarbiesnmeowers.interview.pojos.QuestionCategory;
import com.schnarbiesnmeowers.interview.dtos.QuestionCategoryDTO;
import com.schnarbiesnmeowers.interview.business.QuestionCategoryBusiness;
import com.schnarbiesnmeowers.interview.utilities.Randomizer;

/**
 * this class tests the QuestionCategoryController class
 * these tests we want to run in order
 * @author Dylan I. Kessler
 *
 */
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class QuestionCategoryControllerTest {

	/**
	 * generate a random port for testing
	 */
	@LocalServerPort
	int randomServerPort;

	/**
	 * create a Mock Business object
	 */
	@Mock
	private QuestionCategoryBusiness questioncategoryBusiness;

	/**
     * inject the Mock into the RestTemplate
     */
    @InjectMocks
    private RestTemplate restTemplate = new RestTemplate();

	/**
	 * test creating a new QuestionCategory
	 * @throws URISyntaxException
	 */
	@Test
	public void testA_CreateQuestionCategory() throws URISyntaxException
	{
	    QuestionCategoryDTO questioncategory = generateRandomQuestionCategory();
		System.out.println("RANDOM SERVER PORT = " + randomServerPort);
		System.out.println(questioncategory.toString());
		final String createUrl = "http://localhost:" + randomServerPort + "/questioncategory/create";
		URI uri = new URI(createUrl);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<QuestionCategoryDTO> request = new HttpEntity<>(questioncategory,headers);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		// Verify request succeed
		System.out.println("FINISHED testCreate + " + result.getBody().toString());
		assertEquals(201, result.getStatusCodeValue());
    }

    /**
	 * test getting all QuestionCategory
	 * @throws URISyntaxException
	 */
	@Test
	public void testB_GetAllQuestionCategory() throws URISyntaxException
	{
		System.out.println("RANDOM SERVER PORT = " + randomServerPort);
		final String baseUrl = "http://localhost:" + randomServerPort + "/questioncategory/all";
		URI uri = new URI(baseUrl);
		HttpEntity<String> request = new HttpEntity<>(new String());
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
	}

	/**
	 * test getting a single QuestionCategory by primary key
	 * @throws URISyntaxException
	 */
	@Test
	public void testC_GetQuestionCategory() throws URISyntaxException
	{
		System.out.println("RANDOM SERVER PORT = " + randomServerPort);
		int num = 1;
		final String baseUrl = "http://localhost:" + randomServerPort + "/questioncategory/findById/" + num;
		URI uri = new URI(baseUrl);
		HttpEntity<String> request = new HttpEntity<>(new String());
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
	}

    /**
	 * test updating a QuestionCategory
	 * @throws URISyntaxException
	 */
	@Test
	public void testD_UpdateQuestionCategory() throws URISyntaxException
	{
	    QuestionCategoryDTO questioncategory = generateRandomQuestionCategory();
		final String updateUrl = "http://localhost:" + randomServerPort + "/questioncategory/update";
		URI uri = new URI(updateUrl);
		HttpEntity<QuestionCategoryDTO> request = new HttpEntity<>(questioncategory);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		// Verify request succeed
		System.out.println("FINISHED testUpdate + " + result.getBody().toString());
		assertEquals(200, result.getStatusCodeValue());
	}

	/**
	 * test deleting a QuestionCategory
	 * @throws URISyntaxException
	 */
	@Test
	public void testE_DeleteQuestionCategory() throws URISyntaxException
	{
		QuestionCategoryDTO questioncategory = generateRandomQuestionCategory();
		int num = 1;
		final String deleteUrl = "http://localhost:" + randomServerPort + "/questioncategory/delete/" + num;
		URI uri = new URI(deleteUrl);
		HttpEntity<QuestionCategoryDTO> request = new HttpEntity<>(questioncategory);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);
		System.out.println("FINISHED testDelete");
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
	}


	public static QuestionCategoryDTO generateRandomQuestionCategory() {
		QuestionCategoryDTO record = new QuestionCategoryDTO();
		record.setQuestionCategoryDesc(Randomizer.randomString(20));
		record.setEvntTmestmp(Randomizer.randomDate());
		record.setEvntOperId(Randomizer.randomString(10));
		return record;
	}
}