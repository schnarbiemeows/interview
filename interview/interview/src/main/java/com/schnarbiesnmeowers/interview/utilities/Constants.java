package com.schnarbiesnmeowers.interview.utilities;

/**
 * our main constants utility class
 * @author dylan
 *
 */
public class Constants {
	
	public static final long EXPIRATION_TIME = 432_000_000; // 5 days expressed in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String COMPANY = "schnarbies-n-meowers";
    public static final String COMPANY_ADMINISTRATION = "User Management Portal";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "Please log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = { "/recaptcha/post",
    		"/answer/all",
    		"/interviewuser/confirmemail/**",
    		"/questioncategory/all",
    		"/questionlevel/all",
    		"/question/all",
    		"/question/totals",
    		"/healthcheck/ping", 
    		"/interviewuser/login", 
    		"/interviewuser/register", 
    		"/interviewuser/forgotpassword/**",
    		"/interviewuser/forgotusername/**", 
    		"/interviewuser/image/**", 
    		"/interviewuser/setpwd",
    		"/interviewuser/setrole",
    		"/interviewuser/testemail",
    		"/interviewuser/checkreset",
    		"/interviewuser/finalizepassword" };
    public static final String USER_NOT_FOUND = "User not found";
    public static final String NO_USER_FOUND_BY_USERNAME = "No user found with that username : ";
	public static final String NO_USER_FOUND_BY_ID = "No user found with that id";
	public static final String EXPIRED_LINK = "The confirm email link has expired, please try registering again.";
	public static final String NO_USER_FOUND_BY_EMAIL = "No user found with that email address : ";
	public static final String INCORRECT_OLD_PASSWORD = "The current password that you entered was incorrect.";
	public static final String USERNAME_ALREADY_EXISTS = "Username already exists!";
	public static final String A_USER_WITH_THIS_EMAIL_ALREADY_EXISTS = "A user with this email already exists!";
	public static final String SIMPLE_MAIL_TRANSFER_PROTOCOL = "smtps";
    public static final String FROM_EMAIL = "support@schnarbies-n-meowers.com";
    public static final String EMAIL_SUBJECT_FORGOT_USERNAME_OR_PASSWORD = "schnarbies-n-meowers.com - your recent inquiry...";
    public static final String EMAIL_SUBJECT_CONFIRM_EMAIL = "schnarbies-n-meowers.com - Email Confirmation";
    public static final String EMAIL_SUBJECT_LOCKED = "Interview - Locked Account";
    public static final String EMAIL_TESTING = "Interview - testing";
    public static final String GMAIL_SMTP_SERVER = "smtp.gmail.com";
    public static final String SMTP_HOST = "mail.smtp.host";
    public static final String SMTP_AUTH = "mail.smtp.auth";
    public static final String SMTP_PORT = "mail.smtp.port";
    public static final int DEFAULT_PORT = 465;
    public static final String SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    public static final String SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required";
    public static final String USER_IMAGE_PATH = "/user/image/";
    public static final String JPG_EXTENSION = "jpg";
    public static final String USER_FOLDER = System.getProperty("user.home") + "/supportportal/user/";
    public static final String DIRECTORY_CREATED = "Created directory for: ";
    public static final String DEFAULT_USER_IMAGE_PATH = "/user/image/profile/";
    public static final String FILE_SAVED_IN_FILE_SYSTEM = "Saved file in file system by name: ";
    public static final String DOT = ".";
    public static final String FORWARD_SLASH = "/";
    public static final String NOT_AN_IMAGE_FILE = " is not an image file. Please upload an image file";
    public static final String TEMP_PROFILE_IMAGE_BASE_URL = "https://robohash.org/";
    public static final String DELETED_MSG = "successfully deleted";
}
