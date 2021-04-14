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

import com.schnarbiesnmeowers.interview.pojos.InterviewUser;
import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTO;
import com.schnarbiesnmeowers.interview.business.InterviewUserBusiness;
import com.schnarbiesnmeowers.interview.utilities.Randomizer;

/**
 * this class tests the InterviewUserController class
 * these tests we want to run in order
 * @author Dylan I. Kessler
 *
 */
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class InterviewUserControllerTest {

	/**
	 * generate a random port for testing
	 */
	@LocalServerPort
	int randomServerPort;

	/**
	 * create a Mock Business object
	 */
	@Mock
	private InterviewUserBusiness interviewuserBusiness;

	/**
     * inject the Mock into the RestTemplate
     */
    @InjectMocks
    private RestTemplate restTemplate = new RestTemplate();

	/**
	 * test creating a new InterviewUser
	 * @throws URISyntaxException
	 */
	@Test
	public void testA_CreateInterviewUser() throws URISyntaxException
	{
	    InterviewUserDTO interviewuser = generateRandomInterviewUser();
		System.out.println("RANDOM SERVER PORT = " + randomServerPort);
		System.out.println(interviewuser.toString());
		final String createUrl = "http://localhost:" + randomServerPort + "/interviewuser/create";
		URI uri = new URI(createUrl);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<InterviewUserDTO> request = new HttpEntity<>(interviewuser,headers);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		// Verify request succeed
		System.out.println("FINISHED testCreate + " + result.getBody().toString());
		assertEquals(201, result.getStatusCodeValue());
    }

    /**
	 * test getting all InterviewUser
	 * @throws URISyntaxException
	 */
	@Test
	public void testB_GetAllInterviewUser() throws URISyntaxException
	{
		System.out.println("RANDOM SERVER PORT = " + randomServerPort);
		final String baseUrl = "http://localhost:" + randomServerPort + "/interviewuser/all";
		URI uri = new URI(baseUrl);
		HttpEntity<String> request = new HttpEntity<>(new String());
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
	}

	/**
	 * test getting a single InterviewUser by primary key
	 * @throws URISyntaxException
	 */
	@Test
	public void testC_GetInterviewUser() throws URISyntaxException
	{
		System.out.println("RANDOM SERVER PORT = " + randomServerPort);
		int num = 1;
		final String baseUrl = "http://localhost:" + randomServerPort + "/interviewuser/findById/" + num;
		URI uri = new URI(baseUrl);
		HttpEntity<String> request = new HttpEntity<>(new String());
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
	}

    /**
	 * test updating a InterviewUser
	 * @throws URISyntaxException
	 */
	@Test
	public void testD_UpdateInterviewUser() throws URISyntaxException
	{
	    InterviewUserDTO interviewuser = generateRandomInterviewUser();
		final String updateUrl = "http://localhost:" + randomServerPort + "/interviewuser/update";
		URI uri = new URI(updateUrl);
		HttpEntity<InterviewUserDTO> request = new HttpEntity<>(interviewuser);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		// Verify request succeed
		System.out.println("FINISHED testUpdate + " + result.getBody().toString());
		assertEquals(200, result.getStatusCodeValue());
	}

	/**
	 * test deleting a InterviewUser
	 * @throws URISyntaxException
	 */
	@Test
	public void testE_DeleteInterviewUser() throws URISyntaxException
	{
		InterviewUserDTO interviewuser = generateRandomInterviewUser();
		int num = 1;
		final String deleteUrl = "http://localhost:" + randomServerPort + "/interviewuser/delete/" + num;
		URI uri = new URI(deleteUrl);
		HttpEntity<InterviewUserDTO> request = new HttpEntity<>(interviewuser);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);
		System.out.println("FINISHED testDelete");
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
	}


	public static InterviewUserDTO generateRandomInterviewUser() {
		InterviewUserDTO record = new InterviewUserDTO();
		String[] stringarray = new String[1];
		stringarray[0] = Randomizer.randomString(3);
		record.setAuthorizations(stringarray);
		record.setEmailaddr(Randomizer.randomString(20));
		record.setFirstname(Randomizer.randomString(20));
		record.setIsuseractive(Randomizer.randomBoolean());
		record.setIsusernotlocked(Randomizer.randomBoolean());
		record.setJoindate(Randomizer.randomDate());
		record.setLastlogindate(Randomizer.randomDate());
		record.setLastlogindatedisplay(Randomizer.randomDate());
		record.setLastname(Randomizer.randomString(20));
		record.setPassword(Randomizer.randomString(20));
		record.setProfileimage(Randomizer.randomString(20));
		record.setRoles(Randomizer.randomString(20));
		record.setUseridentifier(Randomizer.randomString(20));
		record.setUsername(Randomizer.randomString(20));
		return record;
	}
}