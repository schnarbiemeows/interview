package com.schnarbiesnmeowers.interview.utilities;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
public class ConstantsTest {

	@Test
	public void testConstants() {
		assertTrue(0!=Constants.EXPIRATION_TIME); 
	    assertTrue(null!=Constants.TOKEN_PREFIX);
	    assertTrue(null!=Constants.AUTHORIZATION);
	    assertTrue(null!=Constants.JWT_TOKEN_HEADER);
	    assertTrue(null!=Constants.TOKEN_CANNOT_BE_VERIFIED);
	    assertTrue(null!=Constants.COMPANY);
	    assertTrue(null!=Constants.COMPANY_ADMINISTRATION);
	    assertTrue(null!=Constants.AUTHORITIES);
	    assertTrue(null!=Constants.FORBIDDEN_MESSAGE);
	    assertTrue(null!=Constants.ACCESS_DENIED_MESSAGE);
	    assertTrue(null!=Constants.OPTIONS_HTTP_METHOD);
	    assertTrue(null!=Constants.PUBLIC_URLS);
	    assertTrue(null!=Constants.USER_NOT_FOUND);
	    assertTrue(null!=Constants.NO_USER_FOUND_BY_USERNAME);
		assertTrue(null!=Constants.NO_USER_FOUND_BY_ID);
		assertTrue(null!=Constants.EXPIRED_LINK);
		assertTrue(null!=Constants.NO_USER_FOUND_BY_EMAIL);
		assertTrue(null!=Constants.INCORRECT_OLD_PASSWORD);
		assertTrue(null!=Constants.USERNAME_ALREADY_EXISTS);
		assertTrue(null!=Constants.A_USER_WITH_THIS_EMAIL_ALREADY_EXISTS);
		assertTrue(null!=Constants.SIMPLE_MAIL_TRANSFER_PROTOCOL);
	    assertTrue(null!=Constants.FROM_EMAIL);
	    assertTrue(null!=Constants.EMAIL_SUBJECT_FORGOT_USERNAME_OR_PASSWORD);
	    assertTrue(null!=Constants.EMAIL_SUBJECT_CONFIRM_EMAIL);
	    assertTrue(null!=Constants.EMAIL_SUBJECT_LOCKED);
	    assertTrue(null!=Constants.EMAIL_TESTING);
	    assertTrue(null!=Constants.GMAIL_SMTP_SERVER);
	    assertTrue(null!=Constants.SMTP_HOST);
	    assertTrue(null!=Constants.SMTP_AUTH);
	    assertTrue(null!=Constants.SMTP_PORT);
	    assertTrue(0!=Constants.DEFAULT_PORT);
	    assertTrue(null!=Constants.SMTP_STARTTLS_ENABLE);
	    assertTrue(null!=Constants.SMTP_STARTTLS_REQUIRED);
	    assertTrue(null!=Constants.USER_IMAGE_PATH);
	    assertTrue(null!=Constants.JPG_EXTENSION);
	    assertTrue(null!=Constants.USER_FOLDER);
	    assertTrue(null!=Constants.DIRECTORY_CREATED);
	    assertTrue(null!=Constants.DEFAULT_USER_IMAGE_PATH);
	    assertTrue(null!=Constants.FILE_SAVED_IN_FILE_SYSTEM);
	    assertTrue(null!=Constants.DOT);
	    assertTrue(null!=Constants.FORWARD_SLASH);
	    assertTrue(null!=Constants.NOT_AN_IMAGE_FILE);
	    assertTrue(null!=Constants.TEMP_PROFILE_IMAGE_BASE_URL);
	    assertTrue(null!=Constants.DELETED_MSG);
	}
}
