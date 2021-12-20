package com.schnarbiesnmeowers.interview.services.impl;

//import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.schnarbiesnmeowers.interview.dtos.CheckPasswordResetResponseDTO;
import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTO;
import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTOWrapper;
import com.schnarbiesnmeowers.interview.dtos.PasswordResetDTO;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.EmailExistsException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.EmailNotFoundException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.ExpiredLinkException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.NotAnImageFileException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.PasswordIncorrectException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.PasswordResetException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UserNotFoundException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UsernameExistsException;
import com.schnarbiesnmeowers.interview.pojos.InterviewUser;
import com.schnarbiesnmeowers.interview.pojos.InterviewUserTemp;
import com.schnarbiesnmeowers.interview.pojos.PasswordReset;
import com.schnarbiesnmeowers.interview.security.UserPrincipal;
import com.schnarbiesnmeowers.interview.services.InterviewUserRepository;
import com.schnarbiesnmeowers.interview.services.InterviewUserTempRepository;
import com.schnarbiesnmeowers.interview.services.PasswordResetRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = "spring.datasource.hikari.maximum-pool-size=1")
public class UserServiceImplTest {

	public static final String A = "a";
	public static final String B = "b";
	public static final int ONE = 1;
	
	@Autowired
	private UserServiceImpl userService;
	
	@MockBean
	private InterviewUserRepository interviewUserRepository;
	
	@MockBean
	private InterviewUserTempRepository interviewUserTempRepository;

	@MockBean
	private BCryptPasswordEncoder passwordEncoder;

	@MockBean
	private LoginAttemptService loginAttemptService;

	@MockBean
	private PasswordResetRepository passwordResetRepository;

	@MockBean
	private EmailService emailService;
	
	@Test(expected = UsernameNotFoundException.class)
	public void testLoadUserByUsername_UsernameNotFoundException() {
		when(interviewUserRepository.findUserByUserName(A)).thenReturn(null);
		userService.loadUserByUsername(A);
	}
	
	@Test
	public void testLoadUserByUsername_AddressException() throws AddressException, NoSuchProviderException, SendFailedException, MessagingException {
		InterviewUser user = new InterviewUser();
		user.setPassword(A);
		user.setUserName(A);
		user.setUserNotLocked(true);
		when(interviewUserRepository.findUserByUserName(A)).thenReturn(user);
		doReturn(true).when(loginAttemptService).hasExceededMaxAttempts(Mockito.anyString());
		doThrow(new AddressException()).when(emailService).sendManagementEmail(Mockito.anyString(), Mockito.anyString());
		doReturn(null).when(interviewUserRepository).save(Mockito.any(InterviewUser.class));
		UserDetails userPrincipal = userService.loadUserByUsername(A);
		assertTrue(userPrincipal.getPassword().equals(A));
		assertTrue(userPrincipal.getUsername().equals(A));
	}
	
	@Test
	public void testLoadUserByUsername_MessagingException() throws AddressException, NoSuchProviderException, SendFailedException, MessagingException {
		InterviewUser user = new InterviewUser();
		user.setPassword(A);
		user.setUserName(A);
		user.setUserNotLocked(true);
		when(interviewUserRepository.findUserByUserName(A)).thenReturn(user);
		doReturn(true).when(loginAttemptService).hasExceededMaxAttempts(Mockito.anyString());
		doThrow(new MessagingException()).when(emailService).sendManagementEmail(Mockito.anyString(), Mockito.anyString());
		doReturn(null).when(interviewUserRepository).save(Mockito.any(InterviewUser.class));
		UserDetails userPrincipal = userService.loadUserByUsername(A);
		assertTrue(userPrincipal.getPassword().equals(A));
		assertTrue(userPrincipal.getUsername().equals(A));
	}
	
	@Test
	public void testLoadUserByUsername_loadUnlockedUser() throws AddressException, NoSuchProviderException, SendFailedException, MessagingException {
		InterviewUser user = new InterviewUser();
		user.setPassword(A);
		user.setUserName(A);
		user.setUserNotLocked(true);
		when(interviewUserRepository.findUserByUserName(A)).thenReturn(user);
		doReturn(false).when(loginAttemptService).hasExceededMaxAttempts(Mockito.anyString());
		doReturn(null).when(interviewUserRepository).save(Mockito.any(InterviewUser.class));
		UserDetails userPrincipal = userService.loadUserByUsername(A);
		assertTrue(userPrincipal.getPassword().equals(A));
		assertTrue(userPrincipal.getUsername().equals(A));
		assertTrue(userPrincipal.isAccountNonLocked()==true);
	}
	
	@Test
	public void testLoadUserByUsername_loadLockedUser() throws AddressException, NoSuchProviderException, SendFailedException, MessagingException {
		InterviewUser user = new InterviewUser();
		user.setPassword(A);
		user.setUserName(A);
		user.setUserNotLocked(false);
		when(interviewUserRepository.findUserByUserName(A)).thenReturn(user);
		doReturn(false).when(loginAttemptService).hasExceededMaxAttempts(Mockito.anyString());
		doReturn(null).when(interviewUserRepository).save(Mockito.any(InterviewUser.class));
		doNothing().when(loginAttemptService).evictUserFromLoginCache(Mockito.anyString());
		UserDetails userPrincipal = userService.loadUserByUsername(A);
		assertTrue(userPrincipal.getPassword().equals(A));
		assertTrue(userPrincipal.getUsername().equals(A));
		assertTrue(userPrincipal.isAccountNonLocked()==false);
	}
	
	@Test
	public void testRegister() throws AddressException, MessagingException, UserNotFoundException, UsernameExistsException, EmailExistsException {
		when(interviewUserRepository.findUserByUserName(Mockito.anyString())).thenReturn(null);
		when(interviewUserRepository.findUserByEmailAddr(Mockito.anyString())).thenReturn(null);
		doReturn(null).when(interviewUserRepository).save(Mockito.any(InterviewUser.class));
		doNothing().when(emailService).sendConfirmEmailEmail(Mockito.anyString(), Mockito.anyString());
		InterviewUser user = userService.register(A, A, A, B, A);
		assertTrue(user.getFirstName().equals(A));
		assertTrue(user.getLastName().equals(A));
		assertTrue(user.getUserName().equals(A));
		assertTrue(user.getEmailAddr().equals(B));
	}
	
	@Test(expected = UserNotFoundException.class)
	public void testConfirmEmail_UserNotFoundException() throws ExpiredLinkException, UserNotFoundException {
		when(interviewUserTempRepository.findUserByUniqueId(A)).thenReturn(null);
		userService.confirmEmail(A);
	}
	
	@Test(expected = ExpiredLinkException.class)
	public void testConfirmEmail_ExpiredLinkException() throws ParseException, ExpiredLinkException, UserNotFoundException {
		InterviewUserTemp tempUser = new InterviewUserTemp();
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String dateStr = "01/01/2020";
		Date expiredDate = formatter.parse(dateStr);
		tempUser.setCreatedDate(expiredDate);
		when(interviewUserTempRepository.findUserByUniqueId(A)).thenReturn(tempUser);
		doNothing().when(interviewUserTempRepository).delete(Mockito.any(InterviewUserTemp.class));
		userService.confirmEmail(A);
	}
	
	@Test
	public void testConfirmEmail() throws ExpiredLinkException, UserNotFoundException {
		InterviewUserTemp tempUser = new InterviewUserTemp();
		tempUser.setCreatedDate(new Date());
		tempUser.setEmailAddr(A);
		when(interviewUserTempRepository.findUserByUniqueId(A)).thenReturn(tempUser);
		doReturn(null).when(interviewUserRepository).save(Mockito.any(InterviewUser.class));
		doNothing().when(interviewUserTempRepository).delete(Mockito.any(InterviewUserTemp.class));
		InterviewUser user = userService.confirmEmail(A);
		assertTrue(user.getEmailAddr().equals(A));
	}
	
	@Test
	public void testSetPassword() {
		InterviewUser user = new InterviewUser();
		when(interviewUserRepository.findUserByUserName(A)).thenReturn(user);
		doReturn(null).when(interviewUserRepository).save(Mockito.any(InterviewUser.class));
		userService.setPassword(A,A);
		assertTrue(true);
	}
	
	@Test
	public void testSetRole() {
		InterviewUser user = new InterviewUser();
		when(interviewUserRepository.findUserByUserName(A)).thenReturn(user);
		doReturn(null).when(interviewUserRepository).save(Mockito.any(InterviewUser.class));
		userService.setRole(A);
		assertTrue(true);
	}
	
	@Test
	public void testGetTemporaryImageUrl() {
		// we are not using this method currently
		userService.getTemporaryImageUrl(A);
		assertTrue(true);
	}
	
	@Test
	public void testEncodePassword() {
		when(passwordEncoder.encode(Mockito.anyString())).thenReturn(B);
		String result = userService.encodePassword(A);
		assertTrue(result.equals(B));
	}
	
	@Test
	public void testGenerateUserIdentifier() {
		// cannot do the while(validUserIdentifier == false); part - not sure how to do that
		InterviewUser user = new InterviewUser();
		when(interviewUserRepository.findUserByUserName(A)).thenReturn(user);
		String result = userService.generateUserIdentifier();
		assertTrue(null!=result&&result.length()==12);
	}
	
	@Test(expected = UserNotFoundException.class)
	public void testValidateNewUsernameAndEmail_UserNotFoundException() throws UserNotFoundException, UsernameExistsException, EmailExistsException {
		when(interviewUserRepository.findUserByUserName(A)).thenReturn(null);
		userService.validateNewUsernameAndEmail(A, null, null);
	}
	
	@Test(expected = UsernameExistsException.class)
	public void testValidateNewUsernameAndEmail_UsernameExistsException_1() throws UserNotFoundException, UsernameExistsException, EmailExistsException {
		InterviewUser user1 = new InterviewUser();
		user1.setUserName(A);
		user1.setUserId(ONE);
		InterviewUser user2 = new InterviewUser();
		user2.setUserName(B);
		user2.setUserId(ONE+ONE);
		when(interviewUserRepository.findUserByUserName(user1.getUserName())).thenReturn(user1);
		when(interviewUserRepository.findUserByUserName(user2.getUserName())).thenReturn(user2);
		userService.validateNewUsernameAndEmail(A, B, null);
	}
	
	@Test(expected = EmailExistsException.class)
	public void testValidateNewUsernameAndEmail_EmailExistsException_1() throws UserNotFoundException, UsernameExistsException, EmailExistsException {
		InterviewUser user1 = new InterviewUser();
		user1.setUserName(A);
		user1.setUserId(ONE);
		when(interviewUserRepository.findUserByUserName(user1.getUserName())).thenReturn(user1);
		InterviewUser user2 = new InterviewUser();
		user2.setUserName(B);
		user2.setUserId(ONE+ONE);
		when(interviewUserRepository.findUserByUserName(B)).thenReturn(null);
		when(interviewUserRepository.findUserByEmailAddr(A)).thenReturn(user2);
		userService.validateNewUsernameAndEmail(A, B, A);
	}
	
	@Test
	public void testValidateNewUsernameAndEmail_currentUserValid() throws UserNotFoundException, UsernameExistsException, EmailExistsException {
		InterviewUser user1 = new InterviewUser();
		user1.setUserName(A);
		user1.setUserId(ONE);
		when(interviewUserRepository.findUserByUserName(user1.getUserName())).thenReturn(user1);
		when(interviewUserRepository.findUserByUserName(B)).thenReturn(null);
		when(interviewUserRepository.findUserByEmailAddr(A)).thenReturn(null);
		InterviewUser results = userService.validateNewUsernameAndEmail(A, B, A);
		assertTrue(results.toString().equals(user1.toString()));
	}
	
	@Test(expected = UsernameExistsException.class)
	public void testValidateNewUsernameAndEmail_UsernameExistsException_2() throws UserNotFoundException, UsernameExistsException, EmailExistsException {
		InterviewUser user = new InterviewUser();
		user.setUserName(A);
		when(interviewUserRepository.findUserByUserName(user.getUserName())).thenReturn(user);
		userService.validateNewUsernameAndEmail(null, A, A);
	}
	
	@Test(expected = EmailExistsException.class)
	public void testValidateNewUsernameAndEmail_EmailExistsException_2() throws UserNotFoundException, UsernameExistsException, EmailExistsException {
		InterviewUser user = new InterviewUser();
		when(interviewUserRepository.findUserByUserName(A)).thenReturn(null);
		when(interviewUserRepository.findUserByEmailAddr(A)).thenReturn(user);
		userService.validateNewUsernameAndEmail(null, A, A);
	}
	
	@Test
	public void testValidateNewUsernameAndEmail() throws UserNotFoundException, UsernameExistsException, EmailExistsException {
		InterviewUser user = new InterviewUser();
		when(interviewUserRepository.findUserByUserName(A)).thenReturn(null);
		when(interviewUserRepository.findUserByEmailAddr(A)).thenReturn(null);
		InterviewUser results = userService.validateNewUsernameAndEmail(null, A, A);
		assertTrue(results==null);
	}
	
	@Test
	public void testGetAllUsers() {
		when(interviewUserRepository.findAll()).thenReturn(new ArrayList<InterviewUser>());
		List<InterviewUser> users = userService.getAllUsers();
		assertTrue(users.size()==0);
	}
	
	@Test
	public void testGetUsersByRole() {
		String roles = "'" + A + "'";
		when(interviewUserRepository.findByRoleTypes(roles)).thenReturn(new ArrayList<InterviewUser>());
		List<InterviewUser> users = userService.getUsersByRole(A);
		assertTrue(users.size()==0);
	}
	
	@Test
	public void testGetJustUsers() {
		String roles = "'ROLE_BASIC_USER','ROLE_ADV_USER','ROLE_PREMIUM_USER'";
		when(interviewUserRepository.findByRoleTypes(roles)).thenReturn(new ArrayList<InterviewUser>());
		List<InterviewUser> users = userService.getJustUsers();
		assertTrue(users.size()==0);
	}
	
	@Test
	public void testGetAdmins() {
		String roles = "'ROLE_ADMIN','ROLE_SUPER'";
		when(interviewUserRepository.findByRoleTypes(roles)).thenReturn(new ArrayList<InterviewUser>());
		List<InterviewUser> users = userService.getAdmins();
		assertTrue(users.size()==0);
	}
	
	@Test
	public void testFindUserByUsername() {
		InterviewUser user = new InterviewUser();
		user.setUserName(A);
		when(interviewUserRepository.findUserByUserName(A)).thenReturn(user);
		InterviewUser result = userService.findUserByUsername(A);
		assertTrue(result.getUserName().equals(A));
	}
	
	@Test
	public void testFindUserByEmail() {
		InterviewUser user = new InterviewUser();
		user.setUserName(A);
		when(interviewUserRepository.findUserByEmailAddr(A)).thenReturn(user);
		InterviewUser result = userService.findUserByEmail(A);
		assertTrue(result.getUserName().equals(A));
	}
	
	@Test
	public void testAddNewUser() throws UserNotFoundException, UsernameExistsException, EmailExistsException, IOException, NotAnImageFileException {
		when(interviewUserRepository.findUserByUserName(A)).thenReturn(null);
		when(interviewUserRepository.findUserByEmailAddr(A)).thenReturn(null);
		InterviewUser user = new InterviewUser(null,null,A,A,true,true,null,null,null,A,A,A,A,A,A);
		when(interviewUserRepository.save(Mockito.any(InterviewUser.class))).thenReturn(user);
		InterviewUser newUser = userService.addNewUser(A, A, A, A, "ROLE_BASIC_USER", false, false, null);
		assertTrue(newUser.getUserName().equals(A));
	}
	
	@Test
	public void testUpdateUserByUser() throws UserNotFoundException, UsernameExistsException, EmailExistsException, IOException, PasswordIncorrectException {
		InterviewUserDTO dto2 = new InterviewUserDTO();
		dto2.setAuthorizations(null);
		dto2.setEmailAddr(A);
		dto2.setFirstName(A);
		Date today = new Date();
		dto2.setJoinDate(today);
		dto2.setLastLoginDate(today);
		dto2.setLastLoginDateDisplay(today);
		dto2.setLastName(A);
		dto2.setPassword(A);
		dto2.setProfileImage(A);
		dto2.setRoles(A);
		dto2.setUserActive(true);
		dto2.setUserId(ONE);
		dto2.setUserIdentifier(A);
		dto2.setUserName(A);
		dto2.setUserNotLocked(true);
		InterviewUserDTOWrapper dto3 = new InterviewUserDTOWrapper(dto2);
		dto3.setNewEmailAddr(B);
		dto3.setNewFirstName(B);
		dto3.setNewLastName(B);
		dto3.setNewPassword(B);
		dto3.setNewUserName(B);
		InterviewUser emptyUser = new InterviewUser();
		InterviewUser savedUser = new InterviewUser();
		savedUser.setEmailAddr(B);
		savedUser.setFirstName(B);
		savedUser.setLastName(B);
		savedUser.setPassword(B);
		savedUser.setUserName(B);
		when(interviewUserRepository.findUserByUserName(dto3.getUserName())).thenReturn(emptyUser);
		when(interviewUserRepository.findUserByUserName(B)).thenReturn(null);
		when(interviewUserRepository.findUserByEmailAddr(A)).thenReturn(null);
		when(interviewUserRepository.save(Mockito.any(InterviewUser.class))).thenReturn(savedUser);
		when(passwordEncoder.encode(Mockito.any(CharSequence.class))).thenReturn("X");
		InterviewUser results = userService.updateUserByUser(dto3);
		assertTrue(!results.getUserName().equals(dto3.getUserName()));
		assertTrue(!results.getFirstName().equals(dto3.getFirstName()));
		assertTrue(!results.getLastName().equals(dto3.getLastName()));
		assertTrue(!results.getPassword().equals(dto3.getPassword()));
		assertTrue(!results.getEmailAddr().equals(dto3.getEmailAddr()));
	}
	
	@Test
	public void testDeleteUser() throws IOException {
		InterviewUser emptyUser = new InterviewUser();
		emptyUser.setUserName(A);
		emptyUser.setUserId(ONE);
		when(interviewUserRepository.findUserByUserName(emptyUser.getUserName())).thenReturn(emptyUser);
		doNothing().when(interviewUserRepository).deleteById(emptyUser.getUserId());
		userService.deleteUser(A);
		assertTrue(true);
	}
	
	@Test
	public void testResetPasswordInitiation() throws AddressException, NoSuchProviderException, SendFailedException, MessagingException, EmailNotFoundException {
		// 3 tests, test 1: no user found
		when(interviewUserRepository.findUserByEmailAddr(null)).thenReturn(null);
		doNothing().when(emailService).sendNoAddressFoundEmail(Mockito.anyString(),Mockito.anyBoolean());
		userService.resetPasswordInitiation(A);
		assertTrue(true);
		// test 2: no PasswordReset record found
		InterviewUser user = new InterviewUser();
		when(interviewUserRepository.findUserByEmailAddr(Mockito.anyString())).thenReturn(user);
		when(passwordResetRepository.findUserByEmailAddr(Mockito.anyString())).thenReturn(null);
		doReturn(null).when(passwordResetRepository).save(Mockito.any(PasswordReset.class));
		doNothing().when(emailService).sendForgotPasswordEmail(Mockito.anyString(),Mockito.anyString());
		userService.resetPasswordInitiation(A);
		assertTrue(true);
		// test 3 PasswordReset record found
		PasswordReset reset = new PasswordReset();
		when(interviewUserRepository.findUserByEmailAddr(Mockito.anyString())).thenReturn(user);
		when(passwordResetRepository.findUserByEmailAddr(Mockito.anyString())).thenReturn(reset);
		doReturn(null).when(passwordResetRepository).save(Mockito.any(PasswordReset.class));
		doNothing().when(emailService).sendForgotPasswordEmail(Mockito.anyString(),Mockito.anyString());
		userService.resetPasswordInitiation(A);
		assertTrue(true);
	}
	
	@Test
	public void testForgotUsername() throws AddressException, NoSuchProviderException, SendFailedException, MessagingException, EmailNotFoundException {
		// 3 tests, test 1: no user found
		when(interviewUserRepository.findUserByEmailAddr(null)).thenReturn(null);
		doNothing().when(emailService).sendNoAddressFoundEmail(Mockito.anyString(),Mockito.anyBoolean());
		userService.forgotUsername(A);
		assertTrue(true);
		// test 2 - user found
		InterviewUser user = new InterviewUser();
		when(interviewUserRepository.findUserByEmailAddr(Mockito.anyString())).thenReturn(user);
		doNothing().when(emailService).sendEmailWithUsername(Mockito.anyString(),Mockito.anyString());
		assertTrue(true);
	}
	
	@Test
	public void testUpdateProfileImage() throws UserNotFoundException, UsernameExistsException, EmailExistsException, IOException, NotAnImageFileException {
		// TODO - MultipartFile profileImage is set to null, because we are not using this currently,
		// but we will want to fill out this MultipartFile later to fully test this functionality
		InterviewUser user = new InterviewUser();
		user.setUserName(A);
		when(interviewUserRepository.findUserByUserName(user.getUserName())).thenReturn(user);
		when(interviewUserRepository.findUserByEmailAddr(null)).thenReturn(null);
		InterviewUser returnUser = userService.updateProfileImage(A, null);
		assertTrue(returnUser.getUserName().equals(user.getUserName()));
	}
	
	@Test
	public void testTestEmail() throws AddressException, MessagingException, Exception {
		doNothing().when(emailService).testEmail();
		userService.testEmail();
		assertTrue(true);
	}
	
	@Test
	public void testCheckPasswordResetTableWithObject() {
		PasswordReset databaseResults = new PasswordReset(ONE,A,A,new Date());
		doNothing().when(passwordResetRepository).delete(databaseResults);
		assertTrue(true);
	}
	
	@Test
	public void testCheckPasswordResetTableWithString() throws AddressException, NoSuchProviderException, SendFailedException, MessagingException, ParseException {
		// check no record found
		when(passwordResetRepository.findUserByUniqueId(A)).thenReturn(null);
		CheckPasswordResetResponseDTO results = userService.checkPasswordResetTable(A);
		assertTrue(false==results.isFoundRecord());
		assertTrue(null==results.getEmailAddress());
		assertTrue(null==results.getUniqueId());
		// check expired record found
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String dateStr = "01/01/2020";
		Date expiredDate = formatter.parse(dateStr);
		PasswordReset databaseResults = new PasswordReset(ONE,A,A,expiredDate);
		when(passwordResetRepository.findUserByUniqueId(databaseResults.getUniqueId())).thenReturn(databaseResults);
		doNothing().when(passwordResetRepository).delete(databaseResults);
		results = userService.checkPasswordResetTable(A);
		assertTrue(false==results.isFoundRecord());
		assertTrue(results.getEmailAddress().equals(A));
		assertTrue(null==results.getUniqueId());
		// check non-expired record found
		Date today = new Date();
		databaseResults = new PasswordReset(ONE,A,A,today);
		when(passwordResetRepository.findUserByUniqueId(databaseResults.getUniqueId())).thenReturn(databaseResults);
		doReturn(null).when(passwordResetRepository).save(databaseResults);
		results = userService.checkPasswordResetTable(A);
		assertTrue(true==results.isFoundRecord());
		assertTrue(results.getEmailAddress().equals(A));
		assertTrue(null!=results.getUniqueId());
	}
	
	@Test
	public void testChangePassword() throws AddressException, NoSuchProviderException, SendFailedException, MessagingException, PasswordResetException {
		PasswordResetDTO input = new PasswordResetDTO(A,A,A);
		PasswordReset databaseResults = new PasswordReset(ONE,A,A,new Date());
		InterviewUser dummyUser = new InterviewUser();
		when(passwordResetRepository.findUserByUniqueId(input.getUniqueId())).thenReturn(databaseResults);
		when(interviewUserRepository.findUserByEmailAddr(input.getEmailAddress())).thenReturn(dummyUser);
		doReturn(null).when(interviewUserRepository).save(dummyUser);
		doNothing().when(passwordResetRepository).delete(databaseResults);
		userService.changePassword(input);
		assertTrue(true);
	}
	
	@Test(expected = PasswordResetException.class)
	public void testChangePassword_NoRecordFound() throws AddressException, NoSuchProviderException, SendFailedException, MessagingException, PasswordResetException {
		PasswordResetDTO input = new PasswordResetDTO(A,A,A);
		doNothing().when(emailService).sendManagementEmail(Mockito.anyString(),Mockito.anyString());
		when(passwordResetRepository.findUserByUniqueId(input.getUniqueId())).thenReturn(null);
		userService.changePassword(input);
	}
	
	@Test(expected = PasswordResetException.class)
	public void testChangePassword_EmailsDontMatch() throws AddressException, NoSuchProviderException, SendFailedException, MessagingException, PasswordResetException {
		PasswordResetDTO input = new PasswordResetDTO(A,A,A);
		PasswordReset databaseResults = new PasswordReset(ONE,A,B,new Date());
		doNothing().when(emailService).sendManagementEmail(Mockito.anyString(),Mockito.anyString());
		when(passwordResetRepository.findUserByUniqueId(input.getUniqueId())).thenReturn(databaseResults);
		userService.changePassword(input);
	}
	
}
