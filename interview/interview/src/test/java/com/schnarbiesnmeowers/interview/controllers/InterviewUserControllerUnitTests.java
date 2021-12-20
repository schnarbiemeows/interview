package com.schnarbiesnmeowers.interview.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.schnarbiesnmeowers.interview.business.InterviewUserBusiness;
import com.schnarbiesnmeowers.interview.dtos.CheckPasswordResetResponseDTO;
import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTO;
import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTOWrapper;
import com.schnarbiesnmeowers.interview.dtos.InterviewUserTempDTO;
import com.schnarbiesnmeowers.interview.dtos.PasswordResetDTO;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.EmailExistsException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.EmailNotFoundException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.ExpiredLinkException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.PasswordIncorrectException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.PasswordResetException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UserFieldsNotValidException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UserNotFoundException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UsernameExistsException;
import com.schnarbiesnmeowers.interview.pojos.InterviewUser;
import com.schnarbiesnmeowers.interview.pojos.InterviewUserTemp;
import com.schnarbiesnmeowers.interview.pojos.PasswordReset;
import com.schnarbiesnmeowers.interview.pojos.ResponseMessage;
import com.schnarbiesnmeowers.interview.security.JwtTokenProvider;
import com.schnarbiesnmeowers.interview.security.UserPrincipal;
import com.schnarbiesnmeowers.interview.services.InterviewUserRepository;
import com.schnarbiesnmeowers.interview.services.InterviewUserTempRepository;
import com.schnarbiesnmeowers.interview.services.PasswordResetRepository;
import com.schnarbiesnmeowers.interview.services.UserService;
import com.schnarbiesnmeowers.interview.services.impl.EmailService;
import com.schnarbiesnmeowers.interview.services.impl.UserServiceImpl;
import com.schnarbiesnmeowers.interview.utilities.Constants;
import com.schnarbiesnmeowers.interview.utilities.HelperUtility;
import com.schnarbiesnmeowers.interview.utilities.Randomizer;
import com.schnarbiesnmeowers.interview.utilities.Roles;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;

@RunWith(SpringRunner.class)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@TestPropertySource(properties = "spring.datasource.hikari.maximum-pool-size=1")
public class InterviewUserControllerUnitTests {

	public static final String REGISTRATION_URL = "/interviewuser/register";
	public static final String LOGIN_URL = "/interviewuser/login";
	public static final String CONFIRM_EMAIL_URL = "/interviewuser/confirmemail";
	public static final String FORGOT_PASSWORD_URL = "/interviewuser/forgotpassword";
	public static final String CHECK_PWD_RESET_URL = "/interviewuser/checkreset";
	public static final String FINALIZE_PASSWORD_URL = "/interviewuser/finalizepassword";
	public static final String FORGOT_USERNAME_URL = "/interviewuser/forgotusername";
	public static final String SET_ROLE_URL = "/interviewuser/setrole";
    public static final String SET_PWD_URL = "/interviewuser/setpwd";
    public static final String TEST_EMAIL_URL = "/interviewuser/testemail";
	public static final String A = "a";
	public static final String B = "b";
	public static final int ONE = 1;
	public static final String VALID_EMAIL = "dummy@email.com";
	
	/**
	 * admin user for testing
	 */
	@Value("${aaaaaaaaaaaa.token}")
	private String admintoken;

	
	
	/**
     * inject the Mock into the RestTemplate
     */
	@Autowired
    private TestRestTemplate restTemplate;
	
	@MockBean
	private InterviewUserBusiness interviewUserBusiness;
	
	@MockBean
	private EmailService emailService;
	
	@MockBean
	private InterviewUserRepository interviewUserRepository;
	
	@MockBean
	private InterviewUserTempRepository interviewUserTempRepository;
	
	@MockBean
	private AuthenticationManager authManager;
	
	@MockBean
	private PasswordResetRepository passwordResetRepository;
	
	@MockBean
	private JwtTokenProvider jwtTokenProvider;
	
	@MockBean
	private UserServiceImpl userService;
	
	@Test
	public void testRegister() throws UserFieldsNotValidException, AddressException, UserNotFoundException, UsernameExistsException, EmailExistsException, MessagingException {
		final String baseUrl = REGISTRATION_URL;
		InterviewUserTempDTO dto = setupUser();
		InterviewUser resultingUser = new InterviewUser();
		doNothing().when(interviewUserBusiness).validateFields(dto);
		when(userService.register(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(resultingUser);
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<InterviewUserTempDTO> request = new HttpEntity<InterviewUserTempDTO>(dto, headers);
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.POST, request, String.class);
		assertEquals(200, result.getStatusCodeValue());
		assertTrue(true);
	}
	
	@Test
	public void testConfirmEmail() throws ExpiredLinkException, UserNotFoundException {
		final String baseUrl = CONFIRM_EMAIL_URL;
		InterviewUser tempUser = new InterviewUser();
		when(userService.confirmEmail(Mockito.anyString())).thenReturn(tempUser);
		HttpEntity<String> request = new HttpEntity<String>(A);
		ResponseEntity<InterviewUser> result = restTemplate.exchange(baseUrl, HttpMethod.POST, request, InterviewUser.class);
	    assertEquals(200, result.getStatusCodeValue());
	}

	@Test
	public void testLogin() throws ParseException {
		final String baseUrl = LOGIN_URL;
		InterviewUserDTO userDTO = HelperUtility.generateRandomInterviewUserDTO();
		InterviewUser user = userDTO.toEntity();
		doReturn(null).when(authManager).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
//		when(interviewUserRepository.findUserByUserName(Mockito.anyString())).thenReturn(user);
//		when(passwordResetRepository.findUserByEmailAddr(Mockito.anyString())).thenReturn(null);
		when(jwtTokenProvider.generateJwtToken(Mockito.any(UserPrincipal.class))).thenReturn(A);
		when(userService.findUserByUsername(Mockito.anyString())).thenReturn(user);
		doNothing().when(userService).checkPasswordResetTable(Mockito.any(InterviewUserDTO.class));
		HttpEntity<InterviewUserDTO> request = new HttpEntity<InterviewUserDTO>(userDTO);
		ResponseEntity<InterviewUserDTO> result = restTemplate.exchange(baseUrl, HttpMethod.POST, request, InterviewUserDTO.class);
	    assertEquals(200, result.getStatusCodeValue());
		assertNotNull(result.getHeaders());
		List<String> tokens = result.getHeaders().get(Constants.JWT_TOKEN_HEADER);
		assertTrue(tokens.size()==1);
		String token = tokens.get(0);
		assertTrue(token.equals(A));
	}
	
	@Test
	public void testForgotPassword() throws AddressException, NoSuchProviderException, SendFailedException, MessagingException {
		final String baseUrl = FORGOT_PASSWORD_URL;
		InterviewUser user = new InterviewUser();
		when(interviewUserRepository.findUserByEmailAddr(A)).thenReturn(user);
		when(passwordResetRepository.findUserByEmailAddr(A)).thenReturn(null);
		doReturn(null).when(passwordResetRepository).save(Mockito.any(PasswordReset.class));
		doNothing().when(emailService).sendForgotPasswordEmail(Mockito.anyString(), Mockito.anyString());
		HttpEntity<String> request = new HttpEntity<String>(A);
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.POST, request, String.class);
		assertEquals(200, result.getStatusCodeValue());
		assertTrue(true);
	}
	
	@Test
	public void checkPasswordReset() throws AddressException, NoSuchProviderException, SendFailedException, MessagingException {
		final String baseUrl = CHECK_PWD_RESET_URL;
		when(userService.checkPasswordResetTable(Mockito.anyString())).thenReturn(new CheckPasswordResetResponseDTO(true,A,A));
		HttpEntity<String> request = new HttpEntity<String>(A);
		ResponseEntity<CheckPasswordResetResponseDTO> result = restTemplate.exchange(baseUrl, HttpMethod.POST, request, CheckPasswordResetResponseDTO.class);
		assertEquals(200, result.getStatusCodeValue());
		CheckPasswordResetResponseDTO results = result.getBody();
		assertTrue(results.getUniqueId().equals(A));
		assertTrue(results.getEmailAddress().equals(A));
		assertTrue(results.isFoundRecord()==true);
	}
	
	@Test
	public void finalizePasswordReset() throws AddressException, NoSuchProviderException, SendFailedException, MessagingException, PasswordResetException {
		final String baseUrl = FINALIZE_PASSWORD_URL;
		InterviewUserDTO dto = new InterviewUserDTO();
		dto.setUserName(A);
		when(userService.changePassword(Mockito.any(PasswordResetDTO.class))).thenReturn(dto);
		HttpEntity<PasswordResetDTO> request = new HttpEntity<PasswordResetDTO>(new PasswordResetDTO());
		ResponseEntity<InterviewUserDTO> result = restTemplate.exchange(baseUrl, HttpMethod.POST, request, InterviewUserDTO.class);
		assertEquals(200, result.getStatusCodeValue());
		assertTrue(result.getBody().getUserName().equals(A));
	}
	
	@Test
	public void forgotUsername() throws AddressException, MessagingException, EmailNotFoundException {
		final String baseUrl = FORGOT_USERNAME_URL;
		InterviewUserDTO dto = new InterviewUserDTO();
		dto.setUserName(A);
		doNothing().when(userService).forgotUsername(Mockito.anyString());
		HttpEntity<String> request = new HttpEntity<String>(A);
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.POST, request, String.class);
		assertEquals(200, result.getStatusCodeValue());
	}
	
	@Test
	public void setRole() {
		InterviewUserDTO dto = HelperUtility.generateRandomInterviewUserDTO();
		final String baseUrl = SET_ROLE_URL;
		doNothing().when(userService).setRole(Mockito.anyString());
		HttpEntity<InterviewUserDTO> request = new HttpEntity<InterviewUserDTO>(dto);
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.POST, request, String.class);
		assertEquals(200, result.getStatusCodeValue());
	}
	
	@Test
	public void setPassword() {
		InterviewUserDTO dto = HelperUtility.generateRandomInterviewUserDTO();
		final String baseUrl = SET_PWD_URL;
		doNothing().when(userService).setPassword(Mockito.anyString(),Mockito.anyString());
		HttpEntity<InterviewUserDTO> request = new HttpEntity<InterviewUserDTO>(dto);
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.POST, request, String.class);
		assertEquals(200, result.getStatusCodeValue());
	}
	
	@Test
	public void testEmail() throws AddressException, MessagingException, Exception {
		final String baseUrl = TEST_EMAIL_URL;
		doNothing().when(userService).testEmail();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
		assertEquals(200, result.getStatusCodeValue());
	}
	
	private InterviewUserTempDTO setupUser() {
		InterviewUserTempDTO dto = new InterviewUserTempDTO();
		dto.setEmailAddr(VALID_EMAIL);
		dto.setFirstName(A);
		dto.setLastName(A);
		dto.setUserName(A);
		dto.setPassword(A);
		return dto;
	}
}
