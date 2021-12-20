package com.schnarbiesnmeowers.interview.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestOperations;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.schnarbiesnmeowers.interview.business.RecaptchaBusiness;
import com.schnarbiesnmeowers.interview.dtos.AnswerDTO;
import com.schnarbiesnmeowers.interview.dtos.GoogleRequestDTO;
import com.schnarbiesnmeowers.interview.dtos.GoogleResponseDTO;
import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTO;
import com.schnarbiesnmeowers.interview.dtos.QuestionAnswerItemDTO;
import com.schnarbiesnmeowers.interview.dtos.QuestionCategoryDTO;
import com.schnarbiesnmeowers.interview.dtos.QuestionDTO;
import com.schnarbiesnmeowers.interview.dtos.QuestionLevelDTO;
import com.schnarbiesnmeowers.interview.pojos.Answer;
import com.schnarbiesnmeowers.interview.pojos.Question;
import com.schnarbiesnmeowers.interview.pojos.QuestionCategory;
import com.schnarbiesnmeowers.interview.pojos.QuestionLevel;
import com.schnarbiesnmeowers.interview.pojos.ResponseMessage;
import com.schnarbiesnmeowers.interview.security.JwtTokenProvider;
import com.schnarbiesnmeowers.interview.security.UserPrincipal;
import com.schnarbiesnmeowers.interview.services.AnswerRepository;
import com.schnarbiesnmeowers.interview.services.QuestionCategoryRepository;
import com.schnarbiesnmeowers.interview.services.QuestionLevelRepository;
import com.schnarbiesnmeowers.interview.services.QuestionRepository;
import com.schnarbiesnmeowers.interview.services.impl.EmailService;
import com.schnarbiesnmeowers.interview.utilities.Constants;
import com.schnarbiesnmeowers.interview.utilities.HelperUtility;
import com.schnarbiesnmeowers.interview.utilities.Randomizer;
import com.schnarbiesnmeowers.interview.utilities.Roles;

/**
 * 
 * @author dylan
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ServiceAndControllerTests {

	public static final String A = "a";
	public static final String B = "b";
	public static final int ONE = 1;
	public static final String SUCCESS = "success!";
	public static final String RECAPTCHA_URL = "/recaptcha/post";
	public static final String ANS_CREATE_URL = "/answer/create";
    public static final String ANS_UPDATE_URL = "/answer/update";
    public static final String ANS_DELETE_URL = "/answer/delete/";
    public static final String ANS_FIND_ALL_URL = "/answer/all";
    public static final String ANS_FIND_BY_ID = "/answer/findById/";
    public static final String QC_CREATE_URL = "/questioncategory/create";
    public static final String QC_UPDATE_URL = "/questioncategory/update";
    public static final String QC_DELETE_URL = "/questioncategory/delete/";
    public static final String QC_FIND_ALL_URL = "/questioncategory/all";
    public static final String QC_FIND_BY_ID = "/questioncategory/findById/";
    public static final String QL_CREATE_URL = "/questionlevel/create";
    public static final String QL_UPDATE_URL = "/questionlevel/update";
    public static final String QL_DELETE_URL = "/questionlevel/delete/";
    public static final String QL_FIND_ALL_URL = "/questionlevel/all";
    public static final String QL_FIND_BY_ID = "/questionlevel/findById/";
    public static final String Q_CREATE_URL = "/question/create";
    public static final String Q_CREATE_PAIR_URL = "/question/createpair";
    public static final String Q_UPDATE_URL = "/question/update";
    public static final String Q_UPDATE_PAIR_URL = "/question/updatepair";
    public static final String Q_DELETE_URL = "/question/delete/";
    public static final String Q_FIND_ALL_URL = "/question/all";
    public static final String Q_FIND_BY_ID = "/question/findById/";
    public static final String Q_FIND_BY_CATEGORY_ID = "/question/findByQuestionCategoryId/";
    public static final String Q_FIND_BY_LEVEL_ID = "/question/findByQuestionLevelId/";
    public static final String Q_FIND_BY_ANSWER_ID =  "/question/findByAnswerId/";
    public static final String Q_FIND_BY_ALL_FKS = "/question/findByQuestionCategoryIdAndQuestionLevelIdAndAnswerId/";
    
	@Autowired
	private MockMvc mockMvc;
	
	/**
	 * admin user for testing
	 */
	@Value("${aaaaaaaaaaaa.token}")
	private String admintoken;

	/**
	 * basic user for testing 403 responses
	 */
	@Value("${bbbbbbbbbbbb.token}")
	private String usertoken;
	
	/**
	 * secret for generating Jwt token
	 */
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${google.url}")
	private String google_url;
	
	@Value("${recaptcha.secret}")
	private String recaptchaSecret;
	
	/**
     * inject the Mock into the RestTemplate
     */
	@Autowired
    private TestRestTemplate restTemplate;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@MockBean
	private RestOperations restOperationsTemplate;
	
	@MockBean
	private AnswerRepository answerRepository;
	
	@MockBean
	private QuestionCategoryRepository questionCategoryRepository;
	
	@MockBean
	private QuestionLevelRepository questionLevelRepository;
	
	@MockBean
	private QuestionRepository questionRepository;
	
	@Autowired
	private EmailService emailService;

	/********************************************* HealthCheckController *********************************************/
	
	@Test
	public void testHeathCheckController() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders
										.get("/healthcheck/ping")
										.accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(request)
										.andExpect(status().isOk())
										.andReturn();
		assertEquals(SUCCESS,mvcResult.getResponse().getContentAsString());
	}
	
	/********************************************* RecaptchaController *********************************************/
	
	@Test
	public void validateRecaptchaTest() throws Exception {
		String requestData = ""; 
		GoogleRequestDTO data = new GoogleRequestDTO();
		data.setResponse("HERE");
		Gson gson = new Gson();
		GoogleResponseDTO response = new GoogleResponseDTO();
		response.setSuccess(true);
		URI verifyUri = URI.create(String.format(google_url+"?secret=%s&response=%s",recaptchaSecret, data.getResponse()));
		when(restOperationsTemplate.getForObject(verifyUri,GoogleResponseDTO.class)).thenReturn(response);
		final String baseUrl = RECAPTCHA_URL;
		HttpEntity<GoogleRequestDTO> request = new HttpEntity<GoogleRequestDTO>(data);
		ResponseEntity<GoogleResponseDTO> result = restTemplate.exchange(baseUrl, HttpMethod.POST, request, GoogleResponseDTO.class);
		// Verify request succeed
		assertEquals(201, result.getStatusCodeValue());
	}
	
	@Test
	public void validateRecaptchaTest_InvalidReCaptchaException() {
		String requestData = ""; 
		GoogleRequestDTO data = new GoogleRequestDTO();
		data.setResponse("HERE*&^%$");
		Gson gson = new Gson();
		GoogleResponseDTO response = new GoogleResponseDTO();
		response.setSuccess(true);
		URI verifyUri = URI.create(String.format(google_url+"?secret=%s&response=%s",recaptchaSecret, "any"));
		when(restOperationsTemplate.getForObject(verifyUri,GoogleResponseDTO.class)).thenReturn(response);
		final String baseUrl = RECAPTCHA_URL;
		HttpEntity<GoogleRequestDTO> request = new HttpEntity<GoogleRequestDTO>(data);
		ResponseEntity<GoogleResponseDTO> result = restTemplate.exchange(baseUrl, HttpMethod.POST, request, GoogleResponseDTO.class);
		// Verify request succeed
		assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, result.getStatusCodeValue());
	}
	
	@Test
	public void validateRecaptchaTest_ReCaptchaInvalidException() {
		String requestData = ""; 
		GoogleRequestDTO data = new GoogleRequestDTO();
		data.setResponse("HERE");
		Gson gson = new Gson();
		GoogleResponseDTO response = new GoogleResponseDTO();
		response.setSuccess(false);
		URI verifyUri = URI.create(String.format(google_url+"?secret=%s&response=%s",recaptchaSecret, data.getResponse()));
		when(restOperationsTemplate.getForObject(verifyUri,GoogleResponseDTO.class)).thenReturn(response);
		final String baseUrl = RECAPTCHA_URL;
		HttpEntity<GoogleRequestDTO> request = new HttpEntity<GoogleRequestDTO>(data);
		ResponseEntity<GoogleResponseDTO> result = restTemplate.exchange(baseUrl, HttpMethod.POST, request, GoogleResponseDTO.class);
		// Verify request succeed
		assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, result.getStatusCodeValue());
	}
	
	/********************************************* AnswerController *********************************************/
	
	/**
	 * test getting all Answer
	 * @throws URISyntaxException
	 */
	@Test
	public void testGetAllAnswer() throws URISyntaxException
	{
		Answer anAnswer = generateRandomAnswer();
		List<Answer> answers = new ArrayList<Answer>();
		answers.add(anAnswer);
		when(answerRepository.findAll()).thenReturn(answers);
		final String baseUrl = ANS_FIND_ALL_URL;
		HttpEntity<String> request = new HttpEntity<>(new String());
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		Gson gson = new Gson();
		List<AnswerDTO> responseBody = gson.fromJson(result.getBody(),new TypeToken<List<AnswerDTO>>(){}.getType());
		assertEquals(1,responseBody.size());
	}

	/**
	 * test getting a single Answer by primary key
	 * @throws URISyntaxException
	 */
	@Test
	public void testGetAnswer() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		int num = 1;
		//Optional<Answer> anAnswer = Optional.of(generateRandomAnswer());
		Answer anAnswer = generateRandomAnswer();
		anAnswer.setAnswerId(num);
		Optional<Answer> anAnswerOpt = Optional.of(anAnswer);
		when(answerRepository.findById(num)).thenReturn(anAnswerOpt);
		final String baseUrl = ANS_FIND_BY_ID + num;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		Gson gson = new Gson();
		AnswerDTO responseBody = gson.fromJson(result.getBody(),AnswerDTO.class);
		assertTrue(num==responseBody.getAnswerId());
	}
	
	/**
	 * test creating a new Answer using a normal user
	 * this should fail and produce a 403 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testCreateAnswerWithUser() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		Answer anAnswer = generateRandomAnswer();
		AnswerDTO anAnswerDTO = anAnswer.toDTO();
		Optional<Answer> anAnswerOpt = Optional.of(anAnswer);
		when(answerRepository.save(anAnswer)).thenReturn(anAnswer);
		final String createUrl = ANS_CREATE_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<AnswerDTO> request = new HttpEntity<AnswerDTO>(anAnswerDTO,headers);
		ResponseEntity<String> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, String.class);
		// 403 response from normal user
		assertEquals(403, result.getStatusCodeValue());
		
    }
	
	/**
	 * test creating a new Answer using an admin
	 * this should succeed and produce a 201 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testCreateAnswerWithAdmin() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		Answer anAnswer = generateRandomAnswer();
		AnswerDTO anAnswerDTO = anAnswer.toDTO();
		Optional<Answer> anAnswerOpt = Optional.of(anAnswer);
		when(answerRepository.save(any(Answer.class))).thenReturn(anAnswer);
		final String createUrl = ANS_CREATE_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<AnswerDTO> request = new HttpEntity<AnswerDTO>(anAnswerDTO,headers);
		ResponseEntity<AnswerDTO> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, AnswerDTO.class);
		// 201 response from normal user
		assertEquals(201, result.getStatusCodeValue());
		assertTrue(result.getBody().getAnswerTxt().equals(anAnswerDTO.getAnswerTxt()));
    }

	/**
	 * test updating an Answer using a normal user 
	 * this should fail and produce a 403 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testUpdateAnswerWithUser() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		Answer anAnswer = generateRandomAnswer();
		AnswerDTO anAnswerDTO = anAnswer.toDTO();
		Optional<Answer> anAnswerOpt = Optional.of(anAnswer);
		when(answerRepository.save(anAnswer)).thenReturn(anAnswer);
		final String createUrl = ANS_UPDATE_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<AnswerDTO> request = new HttpEntity<AnswerDTO>(anAnswerDTO,headers);
		ResponseEntity<String> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, String.class);
		// 403 response from normal user
		assertEquals(403, result.getStatusCodeValue());	
    }
	
	/**
	 * test updating an Answer using an admin user
	 * this should succeed and produce a 200 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testUpdateAnswerWithAdmin() throws URISyntaxException
	{
		int num = 1;
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		Answer anAnswer = generateRandomAnswer();
		anAnswer.setAnswerId(num);
		AnswerDTO anAnswerDTO = anAnswer.toDTO();
		Optional<Answer> anAnswerOpt = Optional.of(anAnswer);
		when(answerRepository.save(any(Answer.class))).thenReturn(anAnswer);
		when(answerRepository.findById(num)).thenReturn(anAnswerOpt);
		final String createUrl = ANS_UPDATE_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<AnswerDTO> request = new HttpEntity<AnswerDTO>(anAnswerDTO,headers);
		ResponseEntity<AnswerDTO> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, AnswerDTO.class);
		// 200 response from normal user
		assertEquals(200, result.getStatusCodeValue());
		assertTrue(result.getBody().getAnswerTxt().equals(anAnswerDTO.getAnswerTxt()));
    }

	/**
	 * test deleting an Answer using a normal user 
	 * this should fail and produce a 403 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testDeleteAnswerWithUser() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		int num = 1;
		final String deleteUrl = ANS_DELETE_URL + num;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<AnswerDTO> request = new HttpEntity<>(headers);
		ResponseEntity<String> result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, String.class);
		// Verify 403 status
		assertEquals(403, result.getStatusCodeValue());
	}
	
	/**
	 * test deleting an Answer using an admin user
	 * this should succeed and produce a 200 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testDeleteAnswerWithAdmin() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		int num = 1;
		Answer anAnswer = generateRandomAnswer();
		anAnswer.setAnswerId(num);
		AnswerDTO anAnswerDTO = anAnswer.toDTO();
		Optional<Answer> anAnswerOpt = Optional.of(anAnswer);
		doNothing().when(answerRepository).deleteById(num);
		when(answerRepository.findById(num)).thenReturn(anAnswerOpt);
		final String deleteUrl = ANS_DELETE_URL + num;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<AnswerDTO> request = new HttpEntity<>(headers);
		ResponseEntity<ResponseMessage> result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, ResponseMessage.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		assertEquals(Constants.DELETED_MSG,result.getBody().getMessage());
	}

	/********************************************* QuestionCategoryController *********************************************/
	
	/**
	 * test getting all QuestionCategory
	 * @throws URISyntaxException
	 */
	@Test
	public void testGetAllQuestionCategory() throws URISyntaxException
	{
		QuestionCategory anQuestionCategory = generateRandomQuestionCategory();
		List<QuestionCategory> answers = new ArrayList<QuestionCategory>();
		answers.add(anQuestionCategory);
		when(questionCategoryRepository.findAll()).thenReturn(answers);
		final String baseUrl = QC_FIND_ALL_URL;
		HttpEntity<String> request = new HttpEntity<>(new String());
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		Gson gson = new Gson();
		List<QuestionCategoryDTO> responseBody = gson.fromJson(result.getBody(),new TypeToken<List<QuestionCategoryDTO>>(){}.getType());
		assertEquals(1,responseBody.size());
	}

	/**
	 * test getting a single QuestionCategory by primary key
	 * @throws URISyntaxException
	 */
	@Test
	public void testGetQuestionCategory() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		int num = 1;
		//Optional<QuestionCategory> anQuestionCategory = Optional.of(generateRandomQuestionCategory());
		QuestionCategory anQuestionCategory = generateRandomQuestionCategory();
		anQuestionCategory.setQuestionCategoryId(num);
		Optional<QuestionCategory> anQuestionCategoryOpt = Optional.of(anQuestionCategory);
		when(questionCategoryRepository.findById(num)).thenReturn(anQuestionCategoryOpt);
		final String baseUrl = QC_FIND_BY_ID + num;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		Gson gson = new Gson();
		QuestionCategoryDTO responseBody = gson.fromJson(result.getBody(),QuestionCategoryDTO.class);
		assertTrue(num==responseBody.getQuestionCategoryId());
	}
	
	/**
	 * test creating a new QuestionCategory using a normal user
	 * this should fail and produce a 403 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testCreateQuestionCategoryWithUser() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		QuestionCategory anQuestionCategory = generateRandomQuestionCategory();
		QuestionCategoryDTO anQuestionCategoryDTO = anQuestionCategory.toDTO();
		Optional<QuestionCategory> anQuestionCategoryOpt = Optional.of(anQuestionCategory);
		when(questionCategoryRepository.save(anQuestionCategory)).thenReturn(anQuestionCategory);
		final String createUrl = QC_CREATE_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<QuestionCategoryDTO> request = new HttpEntity<QuestionCategoryDTO>(anQuestionCategoryDTO,headers);
		ResponseEntity<String> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, String.class);
		// 403 response from normal user
		assertEquals(403, result.getStatusCodeValue());
		
    }
	
	/**
	 * test creating a new QuestionCategory using an admin
	 * this should succeed and produce a 201 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testCreateQuestionCategoryWithAdmin() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		QuestionCategory anQuestionCategory = generateRandomQuestionCategory();
		QuestionCategoryDTO anQuestionCategoryDTO = anQuestionCategory.toDTO();
		Optional<QuestionCategory> anQuestionCategoryOpt = Optional.of(anQuestionCategory);
		when(questionCategoryRepository.save(any(QuestionCategory.class))).thenReturn(anQuestionCategory);
		final String createUrl = QC_CREATE_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<QuestionCategoryDTO> request = new HttpEntity<QuestionCategoryDTO>(anQuestionCategoryDTO,headers);
		ResponseEntity<QuestionCategoryDTO> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, QuestionCategoryDTO.class);
		// 201 response from normal user
		assertEquals(201, result.getStatusCodeValue());
		assertTrue(result.getBody().getQuestionCategoryDesc().equals(anQuestionCategoryDTO.getQuestionCategoryDesc()));
    }

	/**
	 * test updating an QuestionCategory using a normal user 
	 * this should fail and produce a 403 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testUpdateQuestionCategoryWithUser() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		QuestionCategory anQuestionCategory = generateRandomQuestionCategory();
		QuestionCategoryDTO anQuestionCategoryDTO = anQuestionCategory.toDTO();
		Optional<QuestionCategory> anQuestionCategoryOpt = Optional.of(anQuestionCategory);
		when(questionCategoryRepository.save(anQuestionCategory)).thenReturn(anQuestionCategory);
		final String createUrl = QC_UPDATE_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<QuestionCategoryDTO> request = new HttpEntity<QuestionCategoryDTO>(anQuestionCategoryDTO,headers);
		ResponseEntity<String> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, String.class);
		// 403 response from normal user
		assertEquals(403, result.getStatusCodeValue());	
    }
	
	/**
	 * test updating an QuestionCategory using an admin user
	 * this should succeed and produce a 200 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testUpdateQuestionCategoryWithAdmin() throws URISyntaxException
	{
		int num = 1;
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		QuestionCategory anQuestionCategory = generateRandomQuestionCategory();
		anQuestionCategory.setQuestionCategoryId(num);
		QuestionCategoryDTO anQuestionCategoryDTO = anQuestionCategory.toDTO();
		Optional<QuestionCategory> anQuestionCategoryOpt = Optional.of(anQuestionCategory);
		when(questionCategoryRepository.save(any(QuestionCategory.class))).thenReturn(anQuestionCategory);
		when(questionCategoryRepository.findById(num)).thenReturn(anQuestionCategoryOpt);
		final String createUrl = QC_UPDATE_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<QuestionCategoryDTO> request = new HttpEntity<QuestionCategoryDTO>(anQuestionCategoryDTO,headers);
		ResponseEntity<QuestionCategoryDTO> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, QuestionCategoryDTO.class);
		// 200 response from normal user
		assertEquals(200, result.getStatusCodeValue());
		assertTrue(result.getBody().getQuestionCategoryDesc().equals(anQuestionCategoryDTO.getQuestionCategoryDesc()));
    }

	/**
	 * test deleting an QuestionCategory using a normal user 
	 * this should fail and produce a 403 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testDeleteQuestionCategoryWithUser() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		int num = 1;
		final String deleteUrl = QC_DELETE_URL + num;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<QuestionCategoryDTO> request = new HttpEntity<>(headers);
		ResponseEntity<String> result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, String.class);
		// Verify 403 status
		assertEquals(403, result.getStatusCodeValue());
	}
	
	/**
	 * test deleting an QuestionCategory using an admin user
	 * this should succeed and produce a 200 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testDeleteQuestionCategoryWithAdmin() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		int num = 1;
		QuestionCategory anQuestionCategory = generateRandomQuestionCategory();
		anQuestionCategory.setQuestionCategoryId(num);
		QuestionCategoryDTO anQuestionCategoryDTO = anQuestionCategory.toDTO();
		Optional<QuestionCategory> anQuestionCategoryOpt = Optional.of(anQuestionCategory);
		doNothing().when(questionCategoryRepository).deleteById(num);
		when(questionCategoryRepository.findById(num)).thenReturn(anQuestionCategoryOpt);
		final String deleteUrl = QC_DELETE_URL + num;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<QuestionCategoryDTO> request = new HttpEntity<>(headers);
		ResponseEntity<ResponseMessage> result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, ResponseMessage.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		assertEquals(Constants.DELETED_MSG,result.getBody().getMessage());
	}
	
	/********************************************* QuestionLevelController *********************************************/
	
	/**
	 * test getting all QuestionLevel
	 * @throws URISyntaxException
	 */
	@Test
	public void testGetAllQuestionLevel() throws URISyntaxException
	{
		QuestionLevel anQuestionLevel = generateRandomQuestionLevel();
		List<QuestionLevel> answers = new ArrayList<QuestionLevel>();
		answers.add(anQuestionLevel);
		when(questionLevelRepository.findAll()).thenReturn(answers);
		final String baseUrl = QL_FIND_ALL_URL;
		HttpEntity<String> request = new HttpEntity<>(new String());
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		Gson gson = new Gson();
		List<QuestionLevelDTO> responseBody = gson.fromJson(result.getBody(),new TypeToken<List<QuestionLevelDTO>>(){}.getType());
		assertEquals(1,responseBody.size());
	}

	/**
	 * test getting a single QuestionLevel by primary key
	 * @throws URISyntaxException
	 */
	@Test
	public void testGetQuestionLevel() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		int num = 1;
		//Optional<QuestionLevel> anQuestionLevel = Optional.of(generateRandomQuestionLevel());
		QuestionLevel anQuestionLevel = generateRandomQuestionLevel();
		anQuestionLevel.setQuestionLevelId(num);
		Optional<QuestionLevel> anQuestionLevelOpt = Optional.of(anQuestionLevel);
		when(questionLevelRepository.findById(num)).thenReturn(anQuestionLevelOpt);
		final String baseUrl = QL_FIND_BY_ID + num;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		Gson gson = new Gson();
		QuestionLevelDTO responseBody = gson.fromJson(result.getBody(),QuestionLevelDTO.class);
		assertTrue(num==responseBody.getQuestionLevelId());
	}
	
	/**
	 * test creating a new QuestionLevel using a normal user
	 * this should fail and produce a 403 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testCreateQuestionLevelWithUser() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		QuestionLevel anQuestionLevel = generateRandomQuestionLevel();
		QuestionLevelDTO anQuestionLevelDTO = anQuestionLevel.toDTO();
		Optional<QuestionLevel> anQuestionLevelOpt = Optional.of(anQuestionLevel);
		when(questionLevelRepository.save(anQuestionLevel)).thenReturn(anQuestionLevel);
		final String createUrl = QL_CREATE_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<QuestionLevelDTO> request = new HttpEntity<QuestionLevelDTO>(anQuestionLevelDTO,headers);
		ResponseEntity<String> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, String.class);
		// 403 response from normal user
		assertEquals(403, result.getStatusCodeValue());
		
    }
	
	/**
	 * test creating a new QuestionLevel using an admin
	 * this should succeed and produce a 201 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testCreateQuestionLevelWithAdmin() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		QuestionLevel anQuestionLevel = generateRandomQuestionLevel();
		QuestionLevelDTO anQuestionLevelDTO = anQuestionLevel.toDTO();
		Optional<QuestionLevel> anQuestionLevelOpt = Optional.of(anQuestionLevel);
		when(questionLevelRepository.save(any(QuestionLevel.class))).thenReturn(anQuestionLevel);
		final String createUrl = QL_CREATE_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<QuestionLevelDTO> request = new HttpEntity<QuestionLevelDTO>(anQuestionLevelDTO,headers);
		ResponseEntity<QuestionLevelDTO> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, QuestionLevelDTO.class);
		// 201 response from normal user
		assertEquals(201, result.getStatusCodeValue());
		assertTrue(result.getBody().getQuestionLevelDesc().equals(anQuestionLevelDTO.getQuestionLevelDesc()));
    }

	/**
	 * test updating an QuestionLevel using a normal user 
	 * this should fail and produce a 403 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testUpdateQuestionLevelWithUser() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		QuestionLevel anQuestionLevel = generateRandomQuestionLevel();
		QuestionLevelDTO anQuestionLevelDTO = anQuestionLevel.toDTO();
		Optional<QuestionLevel> anQuestionLevelOpt = Optional.of(anQuestionLevel);
		when(questionLevelRepository.save(anQuestionLevel)).thenReturn(anQuestionLevel);
		final String createUrl = QL_UPDATE_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<QuestionLevelDTO> request = new HttpEntity<QuestionLevelDTO>(anQuestionLevelDTO,headers);
		ResponseEntity<String> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, String.class);
		// 403 response from normal user
		assertEquals(403, result.getStatusCodeValue());	
    }
	
	/**
	 * test updating an QuestionLevel using an admin user
	 * this should succeed and produce a 200 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testUpdateQuestionLevelWithAdmin() throws URISyntaxException
	{
		int num = 1;
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		QuestionLevel anQuestionLevel = generateRandomQuestionLevel();
		anQuestionLevel.setQuestionLevelId(num);
		QuestionLevelDTO anQuestionLevelDTO = anQuestionLevel.toDTO();
		Optional<QuestionLevel> anQuestionLevelOpt = Optional.of(anQuestionLevel);
		when(questionLevelRepository.save(any(QuestionLevel.class))).thenReturn(anQuestionLevel);
		when(questionLevelRepository.findById(num)).thenReturn(anQuestionLevelOpt);
		final String createUrl = QL_UPDATE_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<QuestionLevelDTO> request = new HttpEntity<QuestionLevelDTO>(anQuestionLevelDTO,headers);
		ResponseEntity<QuestionLevelDTO> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, QuestionLevelDTO.class);
		// 200 response from normal user
		assertEquals(200, result.getStatusCodeValue());
		assertTrue(result.getBody().getQuestionLevelDesc().equals(anQuestionLevelDTO.getQuestionLevelDesc()));
    }

	/**
	 * test deleting an QuestionLevel using a normal user 
	 * this should fail and produce a 403 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testDeleteQuestionLevelWithUser() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		int num = 1;
		final String deleteUrl = QL_DELETE_URL + num;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<QuestionLevelDTO> request = new HttpEntity<>(headers);
		ResponseEntity<String> result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, String.class);
		// Verify 403 status
		assertEquals(403, result.getStatusCodeValue());
	}
	
	/**
	 * test deleting an QuestionLevel using an admin user
	 * this should succeed and produce a 200 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testDeleteQuestionLevelWithAdmin() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		int num = 1;
		QuestionLevel anQuestionLevel = generateRandomQuestionLevel();
		anQuestionLevel.setQuestionLevelId(num);
		QuestionLevelDTO anQuestionLevelDTO = anQuestionLevel.toDTO();
		Optional<QuestionLevel> anQuestionLevelOpt = Optional.of(anQuestionLevel);
		doNothing().when(questionLevelRepository).deleteById(num);
		when(questionLevelRepository.findById(num)).thenReturn(anQuestionLevelOpt);
		final String deleteUrl = QL_DELETE_URL + num;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<QuestionLevelDTO> request = new HttpEntity<>(headers);
		ResponseEntity<ResponseMessage> result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, ResponseMessage.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		assertEquals(Constants.DELETED_MSG,result.getBody().getMessage());
	}
	
	/********************************************* QuestionController *********************************************/
	
	/**
	 * test getting all Question
	 * @throws URISyntaxException
	 */
	@Test
	public void testGetAllQuestion() throws URISyntaxException
	{
		Question anQuestion = generateRandomQuestion();
		List<Question> answers = new ArrayList<Question>();
		answers.add(anQuestion);
		when(questionRepository.findAll()).thenReturn(answers);
		final String baseUrl = Q_FIND_ALL_URL;
		HttpEntity<String> request = new HttpEntity<>(new String());
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		Gson gson = new Gson();
		List<QuestionDTO> responseBody = gson.fromJson(result.getBody(),new TypeToken<List<QuestionDTO>>(){}.getType());
		assertEquals(1,responseBody.size());
	}

	/**
	 * test getting a single Question by primary key
	 * @throws URISyntaxException
	 */
	@Test
	public void testGetQuestion() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		int num = 1;
		//Optional<Question> anQuestion = Optional.of(generateRandomQuestion());
		Question anQuestion = generateRandomQuestion();
		anQuestion.setQuestionId(num);
		Optional<Question> anQuestionOpt = Optional.of(anQuestion);
		when(questionRepository.findById(num)).thenReturn(anQuestionOpt);
		final String baseUrl = Q_FIND_BY_ID + num;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		Gson gson = new Gson();
		QuestionDTO responseBody = gson.fromJson(result.getBody(),QuestionDTO.class);
		assertTrue(num==responseBody.getQuestionId());
	}
	
	/**
	 * test creating a new Question using a normal user
	 * this should fail and produce a 403 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testCreateQuestionWithUser() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		Question anQuestion = generateRandomQuestion();
		QuestionDTO anQuestionDTO = anQuestion.toDTO();
		Optional<Question> anQuestionOpt = Optional.of(anQuestion);
		when(questionRepository.save(anQuestion)).thenReturn(anQuestion);
		final String createUrl = Q_CREATE_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<QuestionDTO> request = new HttpEntity<QuestionDTO>(anQuestionDTO,headers);
		ResponseEntity<String> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, String.class);
		// 403 response from normal user
		assertEquals(403, result.getStatusCodeValue());
		
    }
	
	/**
	 * test creating a new Question using an admin
	 * this should succeed and produce a 201 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testCreateQuestionWithAdmin() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		Question anQuestion = generateRandomQuestion();
		QuestionDTO anQuestionDTO = anQuestion.toDTO();
		Optional<Question> anQuestionOpt = Optional.of(anQuestion);
		when(questionRepository.save(any(Question.class))).thenReturn(anQuestion);
		final String createUrl = Q_CREATE_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<QuestionDTO> request = new HttpEntity<QuestionDTO>(anQuestionDTO,headers);
		ResponseEntity<QuestionDTO> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, QuestionDTO.class);
		// 201 response from normal user
		assertEquals(201, result.getStatusCodeValue());
		assertTrue(result.getBody().getQuestionTxt().equals(anQuestionDTO.getQuestionTxt()));
    }
	
	/**
	 * test creating a new Question/Answer pair using an admin
	 * this should succeed and produce a 201 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testCreateQuestionAndAnswerWithAdmin() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		QuestionAnswerItemDTO questionAnswerPairDTO = generateRandomQuestionAnswerItem();
		Answer testAnswer = new Answer();
		Question testQuestion = new Question();
		testAnswer.setAnswerId(ONE);
		testQuestion.setQuestionId(ONE);
		testQuestion.setAnswerId(ONE);
		when(answerRepository.save(any(Answer.class))).thenReturn(testAnswer);
		when(questionRepository.save(any(Question.class))).thenReturn(testQuestion);
		final String createPairUrl = Q_CREATE_PAIR_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<QuestionAnswerItemDTO> request = new HttpEntity<QuestionAnswerItemDTO>(questionAnswerPairDTO,headers);
		ResponseEntity<QuestionAnswerItemDTO> result = restTemplate.exchange(createPairUrl, HttpMethod.POST, request, QuestionAnswerItemDTO.class);
		// 201 response from normal user
		assertEquals(201, result.getStatusCodeValue());
    }

	

	/**
	 * test updating an Question using a normal user 
	 * this should fail and produce a 403 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testUpdateQuestionWithUser() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		Question anQuestion = generateRandomQuestion();
		QuestionDTO anQuestionDTO = anQuestion.toDTO();
		Optional<Question> anQuestionOpt = Optional.of(anQuestion);
		when(questionRepository.save(anQuestion)).thenReturn(anQuestion);
		final String createUrl = Q_UPDATE_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<QuestionDTO> request = new HttpEntity<QuestionDTO>(anQuestionDTO,headers);
		ResponseEntity<String> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, String.class);
		// 403 response from normal user
		assertEquals(403, result.getStatusCodeValue());	
    }
	
	/**
	 * test updating a new Question/Answer pair using an admin
	 * this should succeed and produce a 201 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testUpdateQuestionAndAnswerWithAdmin() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		QuestionAnswerItemDTO questionAnswerPairDTO = generateRandomQuestionAnswerItem();
		questionAnswerPairDTO.setAnswerId(ONE);
		questionAnswerPairDTO.setQuestionId(ONE);
		Answer testAnswer = new Answer();
		Question testQuestion = new Question();
		testAnswer.setAnswerId(ONE);
		testQuestion.setQuestionId(ONE);
		testQuestion.setAnswerId(ONE);
		when(answerRepository.getOne(Mockito.anyInt())).thenReturn(testAnswer);
		when(questionRepository.getOne(Mockito.anyInt())).thenReturn(testQuestion);
		when(answerRepository.save(any(Answer.class))).thenReturn(testAnswer);
		when(questionRepository.save(any(Question.class))).thenReturn(testQuestion);
		final String updatePairUrl = Q_UPDATE_PAIR_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<QuestionAnswerItemDTO> request = new HttpEntity<QuestionAnswerItemDTO>(questionAnswerPairDTO,headers);
		ResponseEntity<QuestionAnswerItemDTO> result = restTemplate.exchange(updatePairUrl, HttpMethod.POST, request, QuestionAnswerItemDTO.class);
		// 201 response from normal user
		assertEquals(200, result.getStatusCodeValue());
    }
	
	/**
	 * test updating an Question using an admin user
	 * this should succeed and produce a 200 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testUpdateQuestionWithAdmin() throws URISyntaxException
	{
		int num = 1;
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		Question anQuestion = generateRandomQuestion();
		anQuestion.setQuestionId(num);
		QuestionDTO anQuestionDTO = anQuestion.toDTO();
		Optional<Question> anQuestionOpt = Optional.of(anQuestion);
		when(questionRepository.save(any(Question.class))).thenReturn(anQuestion);
		when(questionRepository.findById(num)).thenReturn(anQuestionOpt);
		final String createUrl = Q_UPDATE_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<QuestionDTO> request = new HttpEntity<QuestionDTO>(anQuestionDTO,headers);
		ResponseEntity<QuestionDTO> result = restTemplate.exchange(createUrl, HttpMethod.POST, request, QuestionDTO.class);
		// 200 response from normal user
		assertEquals(200, result.getStatusCodeValue());
		assertTrue(result.getBody().getQuestionTxt().equals(anQuestionDTO.getQuestionTxt()));
    }

	/**
	 * test deleting an Question using a normal user 
	 * this should fail and produce a 403 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testDeleteQuestionWithUser() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		int num = 1;
		final String deleteUrl = Q_DELETE_URL + num;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<QuestionDTO> request = new HttpEntity<>(headers);
		ResponseEntity<String> result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, String.class);
		// Verify 403 status
		assertEquals(403, result.getStatusCodeValue());
	}
	
	/**
	 * test deleting an Question using an admin user
	 * this should succeed and produce a 200 status code
	 * @throws URISyntaxException
	 */
	@Test
	public void testDeleteQuestionWithAdmin() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + admintoken;
		int num = 1;
		Question anQuestion = generateRandomQuestion();
		anQuestion.setQuestionId(num);
		QuestionDTO anQuestionDTO = anQuestion.toDTO();
		Optional<Question> anQuestionOpt = Optional.of(anQuestion);
		doNothing().when(questionRepository).deleteById(num);
		when(questionRepository.findById(num)).thenReturn(anQuestionOpt);
		final String deleteUrl = Q_DELETE_URL + num;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<QuestionDTO> request = new HttpEntity<>(headers);
		ResponseEntity<ResponseMessage> result = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, ResponseMessage.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		assertEquals(Constants.DELETED_MSG,result.getBody().getMessage());
	}

	/**
	 * test getting all Question by foreign key questionCategoryId
	 * @throws URISyntaxException
	*/
	@Test
	public void testGetQuestionByQuestionCategoryId() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		int num = 1;
		Question anQuestion = generateRandomQuestion();
		anQuestion.setQuestionCategoryId(num);
		List<Question> questionList = new ArrayList<Question>();
		questionList.add(anQuestion);
		when(questionRepository.findQuestionByQuestionCategoryId(num)).thenReturn(questionList);
		final String baseUrl = Q_FIND_BY_CATEGORY_ID + num;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		Gson gson = new Gson();
		List<QuestionDTO> responseBody = gson.fromJson(result.getBody(),new TypeToken<List<QuestionDTO>>(){}.getType());
		assertEquals(1,responseBody.size());
	}
	
	/**
	 * test getting all Question by foreign key questionLevelId
	 * @throws URISyntaxException
	*/
	@Test
	public void testGetQuestionByQuestionLevelId() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		int num = 1;
		Question anQuestion = generateRandomQuestion();
		anQuestion.setQuestionLevelId(num);
		List<Question> questionList = new ArrayList<Question>();
		questionList.add(anQuestion);
		when(questionRepository.findQuestionByQuestionLevelId(num)).thenReturn(questionList);
		final String baseUrl = Q_FIND_BY_LEVEL_ID + num;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		Gson gson = new Gson();
		List<QuestionDTO> responseBody = gson.fromJson(result.getBody(),new TypeToken<List<QuestionDTO>>(){}.getType());
		assertEquals(1,responseBody.size());
	}

	/**
	 * test getting all Question by foreign key answerId
	 * @throws URISyntaxException
	*/
	@Test
	public void testGetQuestionByAnswerId() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		int num = 1;
		Question anQuestion = generateRandomQuestion();
		anQuestion.setAnswerId(num);
		List<Question> questionList = new ArrayList<Question>();
		questionList.add(anQuestion);
		when(questionRepository.findQuestionByAnswerId(num)).thenReturn(questionList);
		final String baseUrl = Q_FIND_BY_ANSWER_ID + num;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		Gson gson = new Gson();
		List<QuestionDTO> responseBody = gson.fromJson(result.getBody(),new TypeToken<List<QuestionDTO>>(){}.getType());
		assertEquals(1,responseBody.size());
	}
	
	/**
	 * test getting all Question by all foreign keys
	 * @throws URISyntaxException
	*/
	@Test
	public void testGetQuestionByQuestionCategoryIdAndQuestionLevelIdAndAnswerId() throws URISyntaxException
	{
		String bearerToken = Constants.TOKEN_PREFIX + usertoken;
		int num1 = 1;
		int num2 = 1;
		int num3 = 1;
		Question anQuestion = generateRandomQuestion();
		anQuestion.setQuestionCategoryId(num1);
		anQuestion.setQuestionLevelId(num2);
		anQuestion.setAnswerId(num3);
		List<Question> questionList = new ArrayList<Question>();
		questionList.add(anQuestion);
		when(questionRepository.findQuestionByQuestionCategoryIdAndQuestionLevelIdAndAnswerId(num1,num2,num3)).thenReturn(questionList);
		final String baseUrl = Q_FIND_BY_ALL_FKS + num1 + "/" + num2 + "/" + num3;
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.AUTHORIZATION, bearerToken);
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		Gson gson = new Gson();
		List<QuestionDTO> responseBody = gson.fromJson(result.getBody(),new TypeToken<List<QuestionDTO>>(){}.getType());
		assertEquals(1,responseBody.size());
	}
	
	/********************************************* EmailService *********************************************/
	
	/**
	 * this one test will test all of the public email methods in the EmailService class
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws Exception
	 */
	@Test
	public void testTestEmail() throws AddressException, MessagingException, Exception {
		emailService.testEmail();
		assertTrue(true);
	}
	
	/********************************************* UserServiceImpl *********************************************/
	
	
	/********************************************* JwtTokenProvider *********************************************/
	
	@Test
	public void testGenerateJwtToken() throws ParseException {
		InterviewUserDTO user = HelperUtility.generateRandomInterviewUserDTO();
		user.setUserActive(true);
		user.setUserNotLocked(true);
		HelperUtility.addRolesAndAuthorizationsToUser(Roles.ROLE_BASIC_USER,user);
		UserPrincipal principal = new UserPrincipal(user);
		String token = jwtTokenProvider.generateJwtToken(principal);
		assertTrue(token!=null);
	}
	
	@Test
	public void testGetClaimsFromPrincipal() throws ParseException {
		InterviewUserDTO user = HelperUtility.generateRandomInterviewUserDTO();
		user.setUserActive(true);
		user.setUserNotLocked(true);
		HelperUtility.addRolesAndAuthorizationsToUser(Roles.ROLE_BASIC_USER,user);
		UserPrincipal principal = new UserPrincipal(user);
		String token = jwtTokenProvider.generateJwtToken(principal);
		String[] claims = jwtTokenProvider.getClaimsFromToken(token);
		assertTrue(token!=null);
		assertTrue(claims.length>0);
	}
	
	/********************************************* local methods *********************************************/
	
	public static AnswerDTO generateRandomAnswerDTO() {
		AnswerDTO record = new AnswerDTO();
		record.setAnswerTxt(Randomizer.randomString(20));
		record.setEvntTmestmp(Randomizer.randomDate());
		record.setEvntOperId(Randomizer.randomString(10));
		return record;
	}
	
	public static Answer generateRandomAnswer() {
		Answer record = new Answer();
		record.setAnswerTxt(Randomizer.randomString(20));
		record.setEvntTmestmp(Randomizer.randomDate());
		record.setEvntOperId(Randomizer.randomString(10));
		return record;
	}
	
	public static QuestionCategoryDTO generateRandomQuestionCategoryDTO() {
		QuestionCategoryDTO record = new QuestionCategoryDTO();
		record.setQuestionCategoryDesc(Randomizer.randomString(20));
		record.setEvntTmestmp(Randomizer.randomDate());
		record.setEvntOperId(Randomizer.randomString(10));
		return record;
	}
	
	public static QuestionCategory generateRandomQuestionCategory() {
		QuestionCategory record = new QuestionCategory();
		record.setQuestionCategoryDesc(Randomizer.randomString(20));
		record.setEvntTmestmp(Randomizer.randomDate());
		record.setEvntOperId(Randomizer.randomString(10));
		return record;
	}
	
	public static QuestionLevelDTO generateRandomQuestionLevelDTO() {
		QuestionLevelDTO record = new QuestionLevelDTO();
		record.setQuestionLevelDesc(Randomizer.randomString(20));
		record.setEvntTmestmp(Randomizer.randomDate());
		record.setEvntOperId(Randomizer.randomString(10));
		return record;
	}
	
	public static QuestionLevel generateRandomQuestionLevel() {
		QuestionLevel record = new QuestionLevel();
		record.setQuestionLevelDesc(Randomizer.randomString(20));
		record.setEvntTmestmp(Randomizer.randomDate());
		record.setEvntOperId(Randomizer.randomString(10));
		return record;
	}
	
	public static QuestionDTO generateRandomQuestionDTO() {
		QuestionDTO record = new QuestionDTO();
		record.setQuestionTxt(Randomizer.randomString(20));
		record.setEvntTmestmp(Randomizer.randomDate());
		record.setEvntOperId(Randomizer.randomString(10));
		return record;
	}
	
	public static Question generateRandomQuestion() {
		Question record = new Question();
		record.setQuestionTxt(Randomizer.randomString(20));
		record.setEvntTmestmp(Randomizer.randomDate());
		record.setEvntOperId(Randomizer.randomString(10));
		return record;
	}
	
	private QuestionAnswerItemDTO generateRandomQuestionAnswerItem() {
		QuestionAnswerItemDTO item = new QuestionAnswerItemDTO();
		item.setQuestionCategoryId(ONE);
		item.setQuestionLevelId(ONE);
		item.setEvntOperId(A);
		item.setQuestionTxt(A);
		item.setAnswerTxt(B);
		return item;
	}
	
	/***************************************************************************************************************/
}
