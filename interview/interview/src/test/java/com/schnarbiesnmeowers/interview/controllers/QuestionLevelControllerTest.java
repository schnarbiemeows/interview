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

import com.schnarbiesnmeowers.interview.pojos.QuestionLevel;
import com.schnarbiesnmeowers.interview.dtos.QuestionLevelDTO;
import com.schnarbiesnmeowers.interview.business.QuestionLevelBusiness;
import com.schnarbiesnmeowers.interview.utilities.Randomizer;

/**
 * this class tests the QuestionLevelController class
 * these tests we want to run in order
 * @author Dylan I. Kessler
 *
 */
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class QuestionLevelControllerTest {

	/**
	 * generate a random port for testing
	 */
	@LocalServerPort
	int randomServerPort;

	/**
	 * create a Mock Business object
	 */
	@Mock
	private QuestionLevelBusiness questionlevelBusiness;

	/**
     * inject the Mock into the RestTemplate
     */
    @InjectMocks
    private RestTemplate restTemplate = new RestTemplate();

	/**
	 * test creating a new QuestionLevel
	 * @throws URISyntaxException
	 */
	@Test
	public void testA_CreateQuestionLevel() throws URISyntaxException
	{
	    QuestionLevelDTO questionlevel = generateRandomQuestionLevel();
		System.out.println("RANDOM SERVER PORT = " + randomServerPort);
		System.out.println(questionlevel.toString());
		final String createUrl = "http://localhost:" + randomServerPort + "/questionlevel/create";
		URI uri = new URI(createUrl);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<QuestionLevelDTO> request = new HttpEntity<>(questionlevel,headers);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		// Verify request succeed
		System.out.println("FINISHED testCreate + " + result.getBody().toString());
		assertEquals(201, result.getStatusCodeValue());
    }

    /**
	 * test getting all QuestionLevel
	 * @throws URISyntaxException
	 */
	@Test
	public void testB_GetAllQuestionLevel() throws URISyntaxException
	{
		System.out.println("RANDOM SERVER PORT = " + randomServerPort);
		final String baseUrl = "http://localhost:" + randomServerPort + "/questionlevel/all";
		URI uri = new URI(baseUrl);
		HttpEntity<String> request = new HttpEntity<>(new String());
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
	}

	/**
	 * test getting a single QuestionLevel by primary key
	 * @throws URISyntaxException
	 */
	@Test
	public void testC_GetQuestionLevel() throws URISyntaxException
	{
		System.out.println("RANDOM SERVER PORT = " + randomServerPort);
		int num = 1;
		final String baseUrl = "http://localhost:" + randomServerPort + "/questionlevel/findById/" + num;
		URI uri = new URI(baseUrl);
		HttpEntity<String> request = new HttpEntity<>(new String());
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
	}

    /**
	 * test updating a QuestionLevel
	 * @throws URISyntaxException
	 */
	@Test
	public void testD_UpdateQuestionLevel() throws URISyntaxException
	{
	    QuestionLevelDTO questionlevel = generateRandomQuestionLevel();
		final String updateUrl = "http://localhost:" + randomServerPort + "/questionlevel/update";
		URI uri = new URI(updateUrl);
		HttpEntity<QuestionLevelDTO> request = new HttpEntity<>(questionlevel);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		// Verify request succeed
		System.out.println("FINISHED testUpdate + " + result.getBody().toString());
		assertEquals(200, result.getStatusCodeValue());
	}

	/**
	 * test deleting a QuestionLevel
	 * @throws URISyntaxException
	 */
	@Test
	public void testE_DeleteQuestionLevel() throws URISyntaxException
	{
		QuestionLevelDTO questionlevel = generateRandomQuestionLevel();
		int num = 1;
		final String deleteUrl = "http://localhost:" + randomServerPort + "/questionlevel/delete/" + num;
		URI uri = new URI(deleteUrl);
		HttpEntity<QuestionLevelDTO> request = new HttpEntity<>(questionlevel);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);
		System.out.println("FINISHED testDelete");
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
	}


	public static QuestionLevelDTO generateRandomQuestionLevel() {
		QuestionLevelDTO record = new QuestionLevelDTO();
		record.setQuestionLevelDesc(Randomizer.randomString(20));
		record.setEvntTmestmp(Randomizer.randomDate());
		record.setEvntOperId(Randomizer.randomString(10));
		return record;
	}
}