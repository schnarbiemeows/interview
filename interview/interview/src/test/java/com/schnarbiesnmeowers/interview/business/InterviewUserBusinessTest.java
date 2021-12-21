package com.schnarbiesnmeowers.interview.business;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.schnarbiesnmeowers.interview.dtos.InterviewUserTempDTO;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UserFieldsNotValidException;

/**
 * this class only tests the validateFields method
 * all other methods in this class are tested indirectly via the InterviewUserControllerTest class
 * @author dylan
 *
 */
//@RunWith(SpringRunner.class)
public class InterviewUserBusinessTest {

	public static final String A = "a";
	public static final String VALID_EMAIL = "dummy@email.com";
	public static final String INVALID_EMAIL1 = "dummy@";
	public static final String INVALID_EMAIL2 = "@email.com";
	public static final String INVALID_EMAIL3 = "dummy.email.com";
	public static final String INVALID_EMAIL4 = "dummy@email.com.otherthing";
	
	
	private InterviewUserBusiness interviewUserBusiness = new InterviewUserBusiness();
	private InterviewUserTempDTO interviewUser = new InterviewUserTempDTO();
	
	@Test(expected = UserFieldsNotValidException.class)
	public void testValidateFields_nullUserName() throws UserFieldsNotValidException {
		setupUser(interviewUser);
		interviewUser.setUserName(null);
		interviewUserBusiness.validateFields(interviewUser);
	}
	
	@Test(expected = UserFieldsNotValidException.class)
	public void testValidateFields_blankUserName() throws UserFieldsNotValidException {
		setupUser(interviewUser);
		interviewUser.setUserName("");
		interviewUserBusiness.validateFields(interviewUser);
	}
	
	@Test(expected = UserFieldsNotValidException.class)
	public void testValidateFields_nullFirstName() throws UserFieldsNotValidException {
		setupUser(interviewUser);
		interviewUser.setFirstName(null);
		interviewUserBusiness.validateFields(interviewUser);
	}
	
	@Test(expected = UserFieldsNotValidException.class)
	public void testValidateFields_blankFirstName() throws UserFieldsNotValidException {
		setupUser(interviewUser);
		interviewUser.setFirstName("");
		interviewUserBusiness.validateFields(interviewUser);
	}
	
	@Test(expected = UserFieldsNotValidException.class)
	public void testValidateFields_nullLastName() throws UserFieldsNotValidException {
		setupUser(interviewUser);
		interviewUser.setLastName(null);
		interviewUserBusiness.validateFields(interviewUser);
	}
	
	@Test(expected = UserFieldsNotValidException.class)
	public void testValidateFields_blankLastName() throws UserFieldsNotValidException {
		setupUser(interviewUser);
		interviewUser.setLastName("");
		interviewUserBusiness.validateFields(interviewUser);
	}
	
	@Test(expected = UserFieldsNotValidException.class)
	public void testValidateFields_nullEmailAddr() throws UserFieldsNotValidException {
		setupUser(interviewUser);
		interviewUser.setEmailAddr(null);
		interviewUserBusiness.validateFields(interviewUser);
	}
	
	@Test(expected = UserFieldsNotValidException.class)
	public void testValidateFields_blankEmailAddr() throws UserFieldsNotValidException {
		setupUser(interviewUser);
		interviewUser.setEmailAddr("");
		interviewUserBusiness.validateFields(interviewUser);
	}
	
	@Test(expected = UserFieldsNotValidException.class)
	public void testValidateFields_invalidEmailAddr_1() throws UserFieldsNotValidException {
		setupUser(interviewUser);
		interviewUser.setEmailAddr(INVALID_EMAIL1);
		interviewUserBusiness.validateFields(interviewUser);
	}
	
	@Test(expected = UserFieldsNotValidException.class)
	public void testValidateFields_invalidEmailAddr_2() throws UserFieldsNotValidException {
		setupUser(interviewUser);
		interviewUser.setEmailAddr(INVALID_EMAIL2);
		interviewUserBusiness.validateFields(interviewUser);
	}
	
	@Test(expected = UserFieldsNotValidException.class)
	public void testValidateFields_invalidEmailAddr_3() throws UserFieldsNotValidException {
		setupUser(interviewUser);
		interviewUser.setEmailAddr(INVALID_EMAIL3);
		interviewUserBusiness.validateFields(interviewUser);
	}
	
	@Test(expected = UserFieldsNotValidException.class)
	public void testValidateFields_invalidEmailAddr_4() throws UserFieldsNotValidException {
		setupUser(interviewUser);
		interviewUser.setEmailAddr(INVALID_EMAIL4);
		interviewUserBusiness.validateFields(interviewUser);
	}
	
	@Test
	public void testValidateFields_validUser() throws UserFieldsNotValidException {
		setupUser(interviewUser);
		interviewUserBusiness.validateFields(interviewUser);
		assertTrue(true);
	}
	
	private void setupUser(InterviewUserTempDTO dto) {
		dto.setEmailAddr(VALID_EMAIL);
		dto.setFirstName(A);
		dto.setLastName(A);
		dto.setUserName(A);
	}
}
