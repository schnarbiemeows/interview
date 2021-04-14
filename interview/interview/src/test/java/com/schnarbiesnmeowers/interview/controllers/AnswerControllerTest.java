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

import com.schnarbiesnmeowers.interview.pojos.Answer;
import com.schnarbiesnmeowers.interview.dtos.AnswerDTO;
import com.schnarbiesnmeowers.interview.business.AnswerBusiness;
import com.schnarbiesnmeowers.interview.utilities.Randomizer;

/**
 * this class tests the AnswerController class
 * these tests we want to run in order
 * @author Dylan I. Kessler
 *
 */
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AnswerControllerTest {

	/**
	 * generate a random port for testing
	 */
	@LocalServerPort
	int randomServerPort;

	/**
	 * create a Mock Business object
	 */
	@Mock
	private AnswerBusiness answerBusiness;

	/**
     * inject the Mock into the RestTemplate
     */
    @InjectMocks
    private RestTemplate restTemplate = new RestTemplate();

	/**
	 * test creating a new Answer
	 * @throws URISyntaxException
	 */
	@Test
	public void testA_CreateAnswer() throws URISyntaxException
	{
	    AnswerDTO answer = generateRandomAnswer();
		System.out.println("RANDOM SERVER PORT = " + randomServerPort);
		System.out.println(answer.toString());
		final String createUrl = "http://localhost:" + randomServerPort + "/answer/create";
		URI uri = new URI(createUrl);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<AnswerDTO> request = new HttpEntity<>(answer,headers);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		// Verify request succeed
		System.out.println("FINISHED testCreate + " + result.getBody().toString());
		assertEquals(201, result.getStatusCodeValue());
    }

    /**
	 * test getting all Answer
	 * @throws URISyntaxException
	 */
	@Test
	public void testB_GetAllAnswer() throws URISyntaxException
	{
		System.out.println("RANDOM SERVER PORT = " + randomServerPort);
		final String baseUrl = "http://localhost:" + randomServerPort + "/answer/all";
		URI uri = new URI(baseUrl);
		HttpEntity<String> request = new HttpEntity<>(new String());
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
	}

	/**
	 * test getting a single Answer by primary key
	 * @throws URISyntaxException
	 */
	@Test
	public void testC_GetAnswer() throws URISyntaxException
	{
		System.out.println("RANDOM SERVER PORT = " + randomServerPort);
		int num = 1;
		final String baseUrl = "http://localhost:" + randomServerPort + "/answer/findById/" + num;
		URI uri = new URI(baseUrl);
		HttpEntity<String> request = new HttpEntity<>(new String());
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
	}

    /**
	 * test updating a Answer
	 * @throws URISyntaxException
	 */
	@Test
	public void testD_UpdateAnswer() throws URISyntaxException
	{
	    AnswerDTO answer = generateRandomAnswer();
		final String updateUrl = "http://localhost:" + randomServerPort + "/answer/update";
		URI uri = new URI(updateUrl);
		HttpEntity<AnswerDTO> request = new HttpEntity<>(answer);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		// Verify request succeed
		System.out.println("FINISHED testUpdate + " + result.getBody().toString());
		assertEquals(200, result.getStatusCodeValue());
	}

	/**
	 * test deleting a Answer
	 * @throws URISyntaxException
	 */
	@Test
	public void testE_DeleteAnswer() throws URISyntaxException
	{
		AnswerDTO answer = generateRandomAnswer();
		int num = 1;
		final String deleteUrl = "http://localhost:" + randomServerPort + "/answer/delete/" + num;
		URI uri = new URI(deleteUrl);
		HttpEntity<AnswerDTO> request = new HttpEntity<>(answer);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);
		System.out.println("FINISHED testDelete");
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
	}


	public static AnswerDTO generateRandomAnswer() {
		AnswerDTO record = new AnswerDTO();
		record.setAnswerTxt(Randomizer.randomString(20));
		record.setEvntTmestmp(Randomizer.randomDate());
		record.setEvntOperId(Randomizer.randomString(10));
		return record;
	}
}