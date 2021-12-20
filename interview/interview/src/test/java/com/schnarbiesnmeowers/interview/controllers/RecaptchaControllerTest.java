package com.schnarbiesnmeowers.interview.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestOperations;

import com.google.gson.Gson;
//import com.schnarbiesnmeowers.interview.business.RecaptchaBusiness;
//import com.schnarbiesnmeowers.interview.dtos.GoogleRequestDTO;
//import com.schnarbiesnmeowers.interview.dtos.GoogleResponseDTO;
//import com.schnarbiesnmeowers.interview.exceptions.InvalidReCaptchaException;
//import com.schnarbiesnmeowers.interview.exceptions.ReCaptchaInvalidException;

/**
 * 
 * @author dylan
 *
 */
//@RunWith(SpringRunner.class)
//@Spring_X_BootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RecaptchaControllerTest {

	public static final String RECAPTCHA_URL = "/recaptcha/post";
	
//	@Value("${google.url}")
//	private String google_url;
//	
//	@Value("${recaptcha.secret}")
//	private String secret;
//	
//	@Autowired
//	private TestRestTemplate restTemplate; 
//	
//	@MockBean
//	private RestOperations restOperationsTemplate;
	
//	@Test
//	public void validateRecaptchaTest() throws Exception {
//		String requestData = ""; 
//		GoogleRequestDTO data = new GoogleRequestDTO();
//		data.setResponse("HERE");
//		Gson gson = new Gson();
//		GoogleResponseDTO response = new GoogleResponseDTO();
//		response.setSuccess(true);
//		URI verifyUri = URI.create(String.format(google_url+"?secret=%s&response=%s",secret, data.getResponse()));
//		when(restOperationsTemplate.getForObject(verifyUri,GoogleResponseDTO.class)).thenReturn(response);
//		final String baseUrl = RECAPTCHA_URL;
//		HttpEntity<GoogleRequestDTO> request = new HttpEntity<GoogleRequestDTO>(data);
//		ResponseEntity<GoogleResponseDTO> result = restTemplate.exchange(baseUrl, HttpMethod.POST, request, GoogleResponseDTO.class);
//		// Verify request succeed
//		assertEquals(201, result.getStatusCodeValue());
//	}
//	
//	@Test
//	public void validateRecaptchaTest_InvalidReCaptchaException() {
//		String requestData = ""; 
//		GoogleRequestDTO data = new GoogleRequestDTO();
//		data.setResponse("HERE*&^%$");
//		Gson gson = new Gson();
//		GoogleResponseDTO response = new GoogleResponseDTO();
//		response.setSuccess(true);
//		URI verifyUri = URI.create(String.format(google_url+"?secret=%s&response=%s",secret, "any"));
//		when(restOperationsTemplate.getForObject(verifyUri,GoogleResponseDTO.class)).thenReturn(response);
//		final String baseUrl = RECAPTCHA_URL;
//		HttpEntity<GoogleRequestDTO> request = new HttpEntity<GoogleRequestDTO>(data);
//		ResponseEntity<GoogleResponseDTO> result = restTemplate.exchange(baseUrl, HttpMethod.POST, request, GoogleResponseDTO.class);
//		// Verify request succeed
//		assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, result.getStatusCodeValue());
//	}
//	
//	@Test
//	public void validateRecaptchaTest_ReCaptchaInvalidException() {
//		String requestData = ""; 
//		GoogleRequestDTO data = new GoogleRequestDTO();
//		data.setResponse("HERE");
//		Gson gson = new Gson();
//		GoogleResponseDTO response = new GoogleResponseDTO();
//		response.setSuccess(false);
//		URI verifyUri = URI.create(String.format(google_url+"?secret=%s&response=%s",secret, data.getResponse()));
//		when(restOperationsTemplate.getForObject(verifyUri,GoogleResponseDTO.class)).thenReturn(response);
//		final String baseUrl = RECAPTCHA_URL;
//		HttpEntity<GoogleRequestDTO> request = new HttpEntity<GoogleRequestDTO>(data);
//		ResponseEntity<GoogleResponseDTO> result = restTemplate.exchange(baseUrl, HttpMethod.POST, request, GoogleResponseDTO.class);
//		// Verify request succeed
//		assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, result.getStatusCodeValue());
//	}
}
