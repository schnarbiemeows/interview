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

import com.schnarbiesnmeowers.interview.pojos.Question;
import com.schnarbiesnmeowers.interview.dtos.QuestionDTO;
import com.schnarbiesnmeowers.interview.business.QuestionBusiness;
import com.schnarbiesnmeowers.interview.utilities.Randomizer;

/**
 * this class tests the QuestionController class
 * these tests we want to run in order
 * @author Dylan I. Kessler
 *
 */
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class QuestionControllerTest {

	/**
	 * generate a random port for testing
	 */
	@LocalServerPort
	int randomServerPort;

	/**
	 * create a Mock Business object
	 */
	@Mock
	private QuestionBusiness questionBusiness;

	/**
     * inject the Mock into the RestTemplate
     */
    @InjectMocks
    private RestTemplate restTemplate = new RestTemplate();

	/**
	 * test creating a new Question
	 * @throws URISyntaxException
	 */
	@Test
	public void testA_CreateQuestion() throws URISyntaxException
	{
	    QuestionDTO question = generateRandomQuestion();
		System.out.println("RANDOM SERVER PORT = " + randomServerPort);
		System.out.println(question.toString());
		final String createUrl = "http://localhost:" + randomServerPort + "/question/create";
		URI uri = new URI(createUrl);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<QuestionDTO> request = new HttpEntity<>(question,headers);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		// Verify request succeed
		System.out.println("FINISHED testCreate + " + result.getBody().toString());
		assertEquals(201, result.getStatusCodeValue());
    }

    /**
	 * test getting all Question
	 * @throws URISyntaxException
	 */
	@Test
	public void testB_GetAllQuestion() throws URISyntaxException
	{
		System.out.println("RANDOM SERVER PORT = " + randomServerPort);
		final String baseUrl = "http://localhost:" + randomServerPort + "/question/all";
		URI uri = new URI(baseUrl);
		HttpEntity<String> request = new HttpEntity<>(new String());
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
	}

	/**
	 * test getting a single Question by primary key
	 * @throws URISyntaxException
	 */
	@Test
	public void testC_GetQuestion() throws URISyntaxException
	{
		System.out.println("RANDOM SERVER PORT = " + randomServerPort);
		int num = 1;
		final String baseUrl = "http://localhost:" + randomServerPort + "/question/findById/" + num;
		URI uri = new URI(baseUrl);
		HttpEntity<String> request = new HttpEntity<>(new String());
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
	}

    /**
	 * test updating a Question
	 * @throws URISyntaxException
	 */
	@Test
	public void testD_UpdateQuestion() throws URISyntaxException
	{
	    QuestionDTO question = generateRandomQuestion();
		final String updateUrl = "http://localhost:" + randomServerPort + "/question/update";
		URI uri = new URI(updateUrl);
		HttpEntity<QuestionDTO> request = new HttpEntity<>(question);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		// Verify request succeed
		System.out.println("FINISHED testUpdate + " + result.getBody().toString());
		assertEquals(200, result.getStatusCodeValue());
	}

	/**
	 * test deleting a Question
	 * @throws URISyntaxException
	 */
	@Test
	public void testE_DeleteQuestion() throws URISyntaxException
	{
		QuestionDTO question = generateRandomQuestion();
		int num = 1;
		final String deleteUrl = "http://localhost:" + randomServerPort + "/question/delete/" + num;
		URI uri = new URI(deleteUrl);
		HttpEntity<QuestionDTO> request = new HttpEntity<>(question);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);
		System.out.println("FINISHED testDelete");
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
	}

	/**
	 * test getting all Question by foreign key questionCategoryId
	 * @throws URISyntaxException
	*/
	@Test
	public void testGetQuestionByQuestionCategoryId() throws URISyntaxException {
		int num = 1;
		final String baseUrl = "http://localhost:" + randomServerPort + "/question/findByQuestionCategoryId/" + num;
		URI uri = new URI(baseUrl);
		HttpEntity<String> request = new HttpEntity<>(new String());
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
		assertEquals(200, result.getStatusCodeValue());
	}

	/**
	 * test getting all Question by foreign key questionLevelId
	 * @throws URISyntaxException
	*/
	@Test
	public void testGetQuestionByQuestionLevelId() throws URISyntaxException {
		int num = 1;
		final String baseUrl = "http://localhost:" + randomServerPort + "/question/findByQuestionLevelId/" + num;
		URI uri = new URI(baseUrl);
		HttpEntity<String> request = new HttpEntity<>(new String());
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
		assertEquals(200, result.getStatusCodeValue());
	}

	/**
	 * test getting all Question by foreign key answerId
	 * @throws URISyntaxException
	*/
	@Test
	public void testGetQuestionByAnswerId() throws URISyntaxException {
		int num = 1;
		final String baseUrl = "http://localhost:" + randomServerPort + "/question/findByAnswerId/" + num;
		URI uri = new URI(baseUrl);
		HttpEntity<String> request = new HttpEntity<>(new String());
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
		assertEquals(200, result.getStatusCodeValue());
	}

	/**
	 * test getting all Question by all foreign keys
	 * @throws URISyntaxException
	*/
	@Test
	public void testGetQuestionByQuestionCategoryIdAndQuestionLevelIdAndAnswerId() throws URISyntaxException {
		int num = 1;
		final String baseUrl = "http://localhost:" + randomServerPort + "/question/findByQuestionCategoryIdAndQuestionLevelIdAndAnswerId/1/1/1";
		URI uri = new URI(baseUrl);
		HttpEntity<String> request = new HttpEntity<>(new String());
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
		assertEquals(200, result.getStatusCodeValue());
	}


	public static QuestionDTO generateRandomQuestion() {
		QuestionDTO record = new QuestionDTO();
		record.setQuestionCategoryId(Randomizer.randomInt(1000));
		record.setQuestionLevelId(Randomizer.randomInt(1000));
		record.setAnswerId(Randomizer.randomInt(1000));
		record.setQuestionTxt(Randomizer.randomString(20));
		record.setEvntTmestmp(Randomizer.randomDate());
		record.setEvntOperId(Randomizer.randomString(10));
		return record;
	}
}