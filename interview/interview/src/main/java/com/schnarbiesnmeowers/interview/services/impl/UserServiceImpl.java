package com.schnarbiesnmeowers.interview.services.impl;

import static com.schnarbiesnmeowers.interview.utilities.Constants.A_USER_WITH_THIS_EMAIL_ALREADY_EXISTS;
import static com.schnarbiesnmeowers.interview.utilities.Constants.DIRECTORY_CREATED;
import static com.schnarbiesnmeowers.interview.utilities.Constants.DOT;
import static com.schnarbiesnmeowers.interview.utilities.Constants.EXPIRED_LINK;
import static com.schnarbiesnmeowers.interview.utilities.Constants.FILE_SAVED_IN_FILE_SYSTEM;
import static com.schnarbiesnmeowers.interview.utilities.Constants.FORWARD_SLASH;
import static com.schnarbiesnmeowers.interview.utilities.Constants.JPG_EXTENSION;
import static com.schnarbiesnmeowers.interview.utilities.Constants.NOT_AN_IMAGE_FILE;
import static com.schnarbiesnmeowers.interview.utilities.Constants.NO_USER_FOUND_BY_ID;
import static com.schnarbiesnmeowers.interview.utilities.Constants.NO_USER_FOUND_BY_USERNAME;
import static com.schnarbiesnmeowers.interview.utilities.Constants.USERNAME_ALREADY_EXISTS;
import static com.schnarbiesnmeowers.interview.utilities.Constants.USER_FOLDER;
import static com.schnarbiesnmeowers.interview.utilities.Constants.USER_IMAGE_PATH;
import static com.schnarbiesnmeowers.interview.utilities.Constants.USER_NOT_FOUND;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.MediaType.IMAGE_GIF_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
import com.schnarbiesnmeowers.interview.services.UserService;
import com.schnarbiesnmeowers.interview.utilities.Roles;

/**
 * 
 * @author Dylan I. Kessler
 *
 */
@Service
@Transactional
@Qualifier("UserDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");
	private static final Logger emailLogger = LogManager.getLogger("EmailAppender");
	
	private InterviewUserRepository repository;
	private InterviewUserTempRepository tempRepository;
	private BCryptPasswordEncoder passwordEncoder;
	private LoginAttemptService loginAttemptService;
	private PasswordResetRepository passwordResetRepository;
	private EmailService emailService;
	
	@Value("${success.email.expiration.minutes}")
	private int linkExpirationTime;
	
	/**
	 * constructor using constructor injection
	 * @param repository
	 * @param tempRepository
	 * @param passwordEncoder
	 * @param loginAttemptService
	 * @param emailService
	 */
	@Autowired
	public UserServiceImpl(InterviewUserRepository repository,
			InterviewUserTempRepository tempRepository,
			BCryptPasswordEncoder passwordEncoder, 
			LoginAttemptService loginAttemptService,
			PasswordResetRepository passwordResetRepository,
			EmailService emailService ) {
		super();
		this.repository = repository;
		this.tempRepository = tempRepository;
		this.passwordEncoder = passwordEncoder;
		this.loginAttemptService = loginAttemptService;
		this.passwordResetRepository = passwordResetRepository;
		this.emailService = emailService;
	}

	/**
	 * this is the main method that the Spring Security will call; our program itself does not directly call it
	 * @param username
	 * @returns UserDetails
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logAction("finding User - " + username);
		InterviewUser user = repository.findUserByUserName(username);
		if(user == null) {
			logAction("User - " + username + " not found!");
			throw new UsernameNotFoundException(USER_NOT_FOUND + " : " + username);
		} else {
			try {
				validateLoginAttempt(user);
			} catch (AddressException e) {
				logAction("AddressException when trying to send an email to administrators for a locked account");
				e.printStackTrace();
			} catch (MessagingException e) {
				logAction("MessagingException when trying to send an email to administrators for a locked account");
				e.printStackTrace();
			}
			user.setLastLoginDateDisplay(user.getLastLoginDate());
			user.setLastLoginDate(new Date());
			repository.save(user);
			UserPrincipal userPrincipal = new UserPrincipal(user.toDTO());
			logAction("returning User - " + username);
			return userPrincipal;
		}
	}


	/**
	 * this method will validate the login attempt and send management an email if the account gets locked
	 * @param user
	 * @throws MessagingException 
	 * @throws AddressException 
	 */
	private void validateLoginAttempt(InterviewUser user) throws AddressException, MessagingException {
		logAction("validateLoginAttempt for User - " + user.getUserName());
		if(user.isUserNotLocked()) {
			if(this.loginAttemptService.hasExceededMaxAttempts(user.getUserName())) {
				logAction("locking User - " + user.getUserName());
				user.setUserNotLocked(false);
				this.emailService.sendManagementEmail("Interview Program - Locked Account","the account for username = " + user.getUserName() + " was locked");
			} else {
				logAction("unlocking User - " + user.getUserName());
				user.setUserNotLocked(true);
			}
		} else {
			logAction("User - " + user.getUserName() + " is locked");
			this.loginAttemptService.evictUserFromLoginCache(user.getUserName());
		}
	}

	/**
	 * this is the registration main method
	 * it will put a record into the interview_user_temp table instead of the normal interview_user table
	 * the user needs to confirm their email in order for this record to get transferred over to the normal table.
	 * @param firstName
	 * @param lastName
	 * @param username
	 * @param email
	 * @return
	 * @throws UserNotFoundException
	 * @throws UsernameExistsException
	 * @throws EmailExistsException
	 * @throws AddressException
	 * @throws MessagingException
	 */
	@Override
	public InterviewUser register(String firstName, String lastName, String username, String email, String password) throws UserNotFoundException, UsernameExistsException, EmailExistsException, AddressException, MessagingException {
		logAction("inside register; validating username and email");
		validateNewUsernameAndEmail(StringUtils.EMPTY,username,email);
		logAction("username and email validation passed");
		InterviewUser user = new InterviewUser();
		InterviewUserTemp tempUser = new InterviewUserTemp();
		String uniqueId = generateUniqueId();
		String userIdentifier = generateUserIdentifier();
		String encodedPassword = encodePassword(password);
		
		tempUser.setUniqueId(uniqueId);
		tempUser.setUserIdentifier(userIdentifier);
		tempUser.setFirstName(firstName);
		tempUser.setLastName(lastName);
		tempUser.setUserName(username);
		tempUser.setEmailAddr(email);
		tempUser.setJoinDate(new Date());
		tempUser.setCreatedDate(new Date());
		tempUser.setPassword(encodedPassword);
		tempUser.setUserActive(true);
		tempUser.setUserNotLocked(true);
		tempUser.setRoles(Roles.ROLE_BASIC_USER.name());
		tempUser.setAuthorizations(Roles.ROLE_BASIC_USER.getAuthorizations());
		tempUser.setProfileImage(getTemporaryImageUrl(username));
		
		user.setUserIdentifier(userIdentifier);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUserName(username);
		user.setEmailAddr(email);
		user.setJoinDate(new Date());
		user.setUserActive(true);
		user.setUserNotLocked(true);
		user.setRoles(Roles.ROLE_BASIC_USER.name());
		user.setAuthorizations(Roles.ROLE_BASIC_USER.getAuthorizations());
		user.setProfileImage(getTemporaryImageUrl(username));
		tempRepository.save(tempUser);
		logAction("New user identifier = " + userIdentifier);
		logEmailAction("sending the confirm email for a new registrant");
		this.emailService.sendConfirmEmailEmail(email,uniqueId);
		return user;
	}
	
	/**
	 * this method gets called when the user confirms their email address, and will copy over the
	 * user's interview_user_temp record into the interview_user table, and then delte that record.
	 * @param id
	 * @return
	 * @throws ExpiredLinkException 
	 * @throws UserNotFoundException 
	 */
	@Override
	public InterviewUser confirmEmail(String id) throws ExpiredLinkException, UserNotFoundException {
		logEmailAction("inside the confirmEmail method after the user click the confirm email link");
		logEmailAction("id = " + id);
		InterviewUserTemp tempUser = tempRepository.findUserByUniqueId(id);
		if(tempUser == null) {
			logEmailAction("not record found in interview_user_temp for id = " + id);
    		throw new UserNotFoundException(NO_USER_FOUND_BY_ID);
    	}
		if(isTheRecordExpired(tempUser.getCreatedDate())) {
			logEmailAction("record found in interview_user_temp, but is EXPIRED for id = " + id);
			tempRepository.delete(tempUser);
			throw new ExpiredLinkException(EXPIRED_LINK);
		}
		logEmailAction("record found in interview_user_temp, copying over for id = " + id);
		InterviewUser newUser = copyOverFromTempRecord(tempUser);
		repository.save(newUser);
		tempRepository.delete(tempUser);
		logEmailAction("leaving the confirmEmail method");
		return newUser;
	}
	
	/**
	 * method to determine if the temp record is expired
	 * the default time = 5 minutes
	 * @param tempUser
	 * @return
	 */
	private boolean isTheRecordExpired(Date recordDate) {
		Date today = new Date();
		long difference = today.getTime() - recordDate.getTime();
		logEmailAction("difference = " + difference);
		if(difference > linkExpirationTime*60000) {
			return true;
		}
		return false;
	}
	
	/**
	 * this method will copy over the values from the temporary record into a permanent InterviewUser record
	 * @param tempUser
	 * @return
	 */
	private InterviewUser copyOverFromTempRecord(InterviewUserTemp tempUser) {
		InterviewUser newUser = new InterviewUser(null,tempUser.getAuthorizations(),tempUser.getEmailAddr(),
		tempUser.getFirstName(),tempUser.isUserActive(),tempUser.isUserNotLocked(),
		tempUser.getJoinDate(),tempUser.getLastLoginDate(),tempUser.getLastLoginDateDisplay(),
		tempUser.getLastName(),tempUser.getPassword(),tempUser.getProfileImage(),
		tempUser.getRoles(),tempUser.getUserIdentifier(),tempUser.getUserName());
		return newUser;
	}
	/**
	 * TEMPORARY method to manually set passwords to what I want them to be - for testing only
	 * @param username
	 * @param password
	 * @return
	 */
	 public void setPassword(String username, String password) {
		logAction("New user identifier = " + username + " to --> " + password);
	 	InterviewUser user = repository.findUserByUserName(username); 
	 	String encodedPassword = encodePassword(password);
	 	user.setPassword(encodedPassword); 
	 	repository.save(user); 
	 }
	 
	/**
	 * this method set's the role and authorizations on the interview_user record
	 * @param username
	 */
	 public void setRole(String username) { 
		InterviewUser user = repository.findUserByUserName(username);
		user.setRoles(Roles.ROLE_SUPER.name());
		user.setAuthorizations(Roles.ROLE_SUPER.getAuthorizations());
		repository.save(user);
	 }
	 
	
	/**
	 * this method will retrieve a temporary image for the user's profile
	 * @param username
	 * @return
	 */
	public String getTemporaryImageUrl(String username) {
		return null;
		// this will later need to be some generic image either in the resources folder, or in S3
		//return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + username).toUriString();
	}

	/**
	 * this method will encode a new user's password
	 * @param password
	 * @return
	 */
	public String encodePassword(String password) {
		return this.passwordEncoder.encode(password);
	}

	/**
	 * this method generates a random password for a new user
	 * @return
	 */
	private String generateUniqueId() {
		return RandomStringUtils.randomAlphanumeric(255);
	}

	/**
	 * this method creates a random userIdentifier for a Basic User(BU)
	 * it first checks to make sure there is not already a user with that identifier
	 * @return
	 */
	public String generateUserIdentifier() {
		String userIdentifier = "BU" ;
		boolean validUserIdentifier = false;
		do {
			userIdentifier += RandomStringUtils.randomNumeric(10);
			InterviewUser user = repository.findUserByUserIdentifier(userIdentifier);
			if(user==null) {
				validUserIdentifier = true;
			} else {
				userIdentifier = "BU" ;
			}
		} while(validUserIdentifier == false);
		return userIdentifier; 
	}

	/**
	 * this method checks that a new user is not registering with a username or email that already exists, and or, for a user who is changing their username,
	 * the old username exists
	 * @param currentUsername
	 * @param newUsername
	 * @param newEmail
	 * @return InterviewUser
	 * @throws UserNotFoundException
	 * @throws UsernameExistsException
	 * @throws EmailExistsException
	 */
	public InterviewUser validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistsException, EmailExistsException {
        if(StringUtils.isNotBlank(currentUsername)) {
        	// this is not a new person trying to register
        	InterviewUser currentUser = findUserByUsername(currentUsername);
        	if(currentUser == null) {
        		logAction("currentUser is null");
        		throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
        	}
        	InterviewUser newUser = findUserByUsername(newUsername);
        	if(newUser != null && !newUser.getUserId().equals(currentUser.getUserId())) {
        		logAction("login: username is already taken");
        		throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
        	}
        	InterviewUser userByEmail = findUserByEmail(newEmail);
        	logAction("email address is already taken");
        	if(userByEmail != null && !userByEmail.getUserId().equals(currentUser.getUserId())) {
        		throw new EmailExistsException(A_USER_WITH_THIS_EMAIL_ALREADY_EXISTS);
        	}
        	return currentUser;
        } else {
        	// this is a new person trying to register
        	InterviewUser newUser = findUserByUsername(newUsername);
        	if(newUser != null) {
        		logAction("registration: username is already taken");
        		throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
        	}
        	InterviewUser userByEmail = findUserByEmail(newEmail);
        	if(userByEmail != null) {
        		logAction("registration: email address is already taken");
        		throw new EmailExistsException(A_USER_WITH_THIS_EMAIL_ALREADY_EXISTS);
        	}
        	return null;
        }
    }
	
	/**
	 * method to get all users
	 * @ return List<InterviewUser>
	 */
	@Override
	public List<InterviewUser> getAllUsers() {
		return repository.findAll();
	}

	/**
	 * method to find a list of users/admins by role
	 * @param role
	 * @return List<InterviewUser>
	 */
	@Override
	public List<InterviewUser> getUsersByRole(String role) {
		String roles = "'" + role + "'";
		return repository.findByRoleTypes(roles);
	}

	/**
	 * method to get just users
	 * @return List<InterviewUser>
	 */
	@Override
	public List<InterviewUser> getJustUsers() {
		String roles = "'ROLE_BASIC_USER','ROLE_ADV_USER','ROLE_PREMIUM_USER'";
		return repository.findByRoleTypes(roles);
	}

	/**
	 * method to get just admins
	 * @return
	 */
	@Override
	public List<InterviewUser> getAdmins() {
		String roles = "'ROLE_ADMIN','ROLE_SUPER'";
		return repository.findByRoleTypes(roles);
	}

	/**
	 * find the user in the interview_user table using the username field
	 * @param username
	 * @return
	 */
	@Override
	public InterviewUser findUserByUsername(String username) {
		return repository.findUserByUserName(username);
	}

	/**
	 * find the user in the interview_user table using the emailAddr field
	 * @param email
	 * @return
	 */
	@Override
	public InterviewUser findUserByEmail(String email) {
		return repository.findUserByEmailAddr(email);
	}

	/**
	 * this method will check to see if the randomly generated userIdentifier already exists in the database
	 * @param userIdentifier
	 * @return
	 */
	private InterviewUser findUserByUserIdentifier(String userIdentifier) {
		return repository.findUserByUserIdentifier(userIdentifier);
	}
	/**
	 * this is an Admin method to add a new user to the interview_user table.
	 * This method is only to be used by administrators
	 * @param firstName
	 * @param lastName
	 * @param username
	 * @param email
	 * @param role
	 * @param isNotLocked
	 * @param isActive
	 * @param profileImage
	 * @return
	 * @throws UserNotFoundException
	 * @throws UsernameExistsException
	 * @throws EmailExistsException
	 * @throws IOException
	 * @throws NotAnImageFileException
	 */
	@Override
	public InterviewUser addNewUser(String firstName, String lastName, String username, String email, String role,
			boolean isNotLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistsException, EmailExistsException, IOException, NotAnImageFileException {
		validateNewUsernameAndEmail(StringUtils.EMPTY,username,email);
		InterviewUser user = new InterviewUser();
		String userIdentifier = generateUserIdentifier();
		user.setUserIdentifier(userIdentifier);
		String password = generateUniqueId();
		String encodedPassword = encodePassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUserName(username);
		user.setEmailAddr(email);
		user.setJoinDate(new Date());
		user.setPassword(encodedPassword);
		user.setUserActive(isActive);
		user.setUserNotLocked(isNotLocked);
		user.setRoles(getRoleEnumName(role).name());
		user.setAuthorizations(getRoleEnumName(role).getAuthorizations());
		user.setProfileImage(getTemporaryImageUrl(username));
		repository.save(user);
		saveProfileImage(user, profileImage);
		return user;
	}

	/**
	 * this is an Admin method to update an interview_user record.
	 * This method is only to be used by administrators
	 * @param currentUserName
	 * @param firstName
	 * @param lastName
	 * @param username
	 * @param email
	 * @return
	 * @throws UserNotFoundException
	 * @throws UsernameExistsException
	 * @throws EmailExistsException
	 * @throws IOException
	 */
	@Override
	public InterviewUser updateUserByUser(InterviewUserDTOWrapper userInput) throws UserNotFoundException, UsernameExistsException, EmailExistsException, IOException, PasswordIncorrectException {
		InterviewUser user = null;
		/*
		 * I am doing this below JUST IN CASE any of the "new" fields might be null; we don't want to accidently
		 * wipe any of these fields in the database
		 * also, this allows us flexibility on the front-end if we want to have pages/sections
		 * where we are just updating specific fields
		 */
		String newUsername = userInput.getUserName();
		String newEmail = userInput.getEmailAddr();
		String newFirstName = userInput.getFirstName();
		String newLastName = userInput.getLastName();
		if(userInput.getNewUserName()!=null&&!userInput.getNewUserName().isEmpty()) {
			newUsername = userInput.getNewUserName();
		}
		if(userInput.getNewEmailAddr()!=null&&!userInput.getNewEmailAddr().isEmpty()) {
			newEmail = userInput.getNewEmailAddr();
		}
		if(userInput.getNewFirstName()!=null&&!userInput.getNewFirstName().isEmpty()) {
			newFirstName = userInput.getNewFirstName();
		}
		if(userInput.getNewLastName()!=null&&!userInput.getNewLastName().isEmpty()) {
			newLastName = userInput.getNewLastName();
		}
		user = validateNewUsernameAndEmail(userInput.getUserName(),newUsername,newEmail);
		if(userInput.getNewPassword()!=null) {
			String encodedPassword = encodePassword(userInput.getNewPassword());
			/*String oldPasswordUserEntered = encodePassword(userInput.getPassword());
			String oldPasswordFromDB = user.getPassword();
			if(!oldPasswordFromDB.equals(oldPasswordUserEntered)) {
				throw new PasswordIncorrectException(INCORRECT_OLD_PASSWORD);
			}*/
			user.setPassword(encodedPassword);
		}
		user.setEmailAddr(newEmail);
		user.setUserName(newUsername);
		user.setFirstName(newFirstName);
		user.setLastName(newLastName);
		repository.save(user);
		return user;
	}

	/**
	 * this is an Admin method to delete an interview_user record.
	 * This method is only to be used by administrators
	 * @param username
	 * @throws IOException
	 */
	@Override
	public void deleteUser(String username) throws IOException {
		InterviewUser user = this.repository.findUserByUserName(username);
        Path userFolder = Paths.get(USER_FOLDER + user.getUserName()).toAbsolutePath().normalize();
        FileUtils.deleteDirectory(new File(userFolder.toString()));
        this.repository.deleteById(user.getUserId());
    }

	/**
	 * this method will initiate a user's password reset, and send them an email with a link for them to click to
	 * reset their password
	 * if no account with that email is found, a different email will be sent to the address informing them of this
	 * 
	 * @param email
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws EmailNotFoundException
	 */
	@Override
	public void resetPasswordInitiation(String email) throws AddressException, MessagingException, EmailNotFoundException {
		// first check to see if there is a record in interview_user for that email
		InterviewUser user = this.findUserByEmail(email);
		if(user == null) {
			// if not, send the NoAddressFoundPREmailTemplate email
			this.emailService.sendNoAddressFoundEmail(email, true);
		}
		else {
			// check to see if there is already a record in password_reset
			String uniqueId = generateUniqueId();
			Date today = new Date();
			PasswordReset passwordResetRecord = this.passwordResetRepository.findUserByEmailAddr(email);
			if(passwordResetRecord == null) {
				passwordResetRecord = new PasswordReset(null,uniqueId,email,today);
			} else {
				passwordResetRecord.setUniqueId(uniqueId);
				passwordResetRecord.setCreatedDate(today);
			}
			this.passwordResetRepository.save(passwordResetRecord);
			this.emailService.sendForgotPasswordEmail(email,uniqueId);
		}
	}

	/**
	 * this method will send the user an email with their username in it
	 * @param email
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws EmailNotFoundException
	 */
	@Override
	public void forgotUsername(String email) throws AddressException, MessagingException, EmailNotFoundException {
		InterviewUser user = this.findUserByEmail(email);
		if(user == null) {
			// if not, send the NoAddressFoundPREmailTemplate email
			this.emailService.sendNoAddressFoundEmail(email, false);
		} else {
			this.emailService.sendEmailWithUsername(email,user.getUserName());
		}	
	}
	
	
	/**
	 * method to update a user's profile image
	 * @param username
	 * @param profileImage
	 * @return
	 * @throws UserNotFoundException
	 * @throws UsernameExistsException
	 * @throws EmailExistsException
	 * @throws IOException
	 * @throws NotAnImageFileException
	 */
	@Override
	public InterviewUser updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExistsException, EmailExistsException, IOException, NotAnImageFileException {
		InterviewUser user = validateNewUsernameAndEmail(username, null, null);
		saveProfileImage(user, profileImage);
		return user;
	}

	/**
	 * method to save a user's profile image to storage
	 * @param user
	 * @param profileImage
	 * @throws IOException
	 * @throws NotAnImageFileException
	 */
	private void saveProfileImage(InterviewUser user, MultipartFile profileImage) throws IOException, NotAnImageFileException {
		if (profileImage != null) {
            if(!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
                throw new NotAnImageFileException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
            }
            Path userFolder = Paths.get(USER_FOLDER + user.getUserName()).toAbsolutePath().normalize();
            if(!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                logAction(DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getUserName() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUserName() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setProfileImage(setProfileImageUrl(user.getUserName()));
            this.repository.save(user);
            logAction(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
	}

	/**
	 * method to set the user's profileImage URL
	 * @param username
	 * @return
	 */
	private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PATH + username + FORWARD_SLASH
        + username + DOT + JPG_EXTENSION).toUriString();
    }
	
	/**
	 * this method gets the value of the Role using the key
	 * @param role
	 * @return
	 */
	private Roles getRoleEnumName(String role) {
		return Roles.valueOf(role.toUpperCase());
	}

	/**
	 * test method for testing the email functionality
	 * TODO - remove
	 * @throws Exception 
	 * @throws MessagingException 
	 * @throws AddressException 
	 */
	@Override
	public void testEmail() throws AddressException, MessagingException, Exception {
		this.emailService.testEmail();
	}

	/**
	 * this method checks the password_reset table first to see if there actually is a password reset request record
	 * associated with the given unique Id
	 * @param id
	 * @return
	 * @throws MessagingException 
	 * @throws SendFailedException 
	 * @throws NoSuchProviderException 
	 * @throws AddressException 
	 */
	@Override
	public CheckPasswordResetResponseDTO checkPasswordResetTable(String id) throws AddressException, NoSuchProviderException, SendFailedException, MessagingException {
		logBothAction("inside checkPasswordResetTable for id = " + id);
		CheckPasswordResetResponseDTO  results = null;
		PasswordReset resetRecord = passwordResetRepository.findUserByUniqueId(id);
		if(resetRecord == null) {
			logBothAction("no record found in password_reset for id = " + id);
			this.emailService.sendManagementEmail("Interview Program: checkPasswordResetTable issue", "A check on the password_reset table for the unique id = " + id + " has failed!");
			results = new CheckPasswordResetResponseDTO(false,null,null);
		} else {
			String emailAdddress = resetRecord.getEmailAddr();
			// check to see if the timestamp has expired
			boolean expiredRecord = isTheRecordExpired(resetRecord.getCreatedDate());
			if(expiredRecord) {
				logBothAction("record found in password_reset, but is EXPIRED for id = " + id);
				passwordResetRepository.delete(resetRecord);
				results = new CheckPasswordResetResponseDTO(false,emailAdddress,null);
			} else {
				logBothAction("record found in password_reset for id = " + id);
				// if a valid record is found, change the unique ID, and send back (true,emailAddress,newUniqueId)
				String newUniqueId = generateUserIdentifier();
				resetRecord.setUniqueId(newUniqueId);
				logBothAction("changing the unique Id to : " + newUniqueId);
				passwordResetRepository.save(resetRecord);
				results = new CheckPasswordResetResponseDTO(true,emailAdddress,newUniqueId);
			}
		}
		logBothAction("leaving checkPasswordResetTable for id = " + id);
		return results;
	}

	/**
	 * this method is for the scenario where a customer had a rest password email sent to them, but then decided to try
	 * to login anyways, and was successful.
	 * This would lead to a hanging password_reset record, so we need to find it and delete it.
	 * @param loggedInUser
	 */
	@Override
	public void checkPasswordResetTable(InterviewUserDTO loggedInUser) {
		PasswordReset resetRecord = passwordResetRepository.findUserByEmailAddr(loggedInUser.getEmailAddr());
		if(null!=resetRecord) {
			passwordResetRepository.delete(resetRecord);
		}
	}

	/**
	 * method to actually change the user's password
	 * @param input
	 * @return
	 * @throws AddressException
	 * @throws NoSuchProviderException
	 * @throws SendFailedException
	 * @throws MessagingException
	 * @throws PasswordResetException
	 */
	@Override
	public InterviewUserDTO changePassword(PasswordResetDTO input) throws AddressException, NoSuchProviderException, SendFailedException, MessagingException, PasswordResetException {
		logAction("inside changePassword, changing the password for unique Id = " + input.getUniqueId());
		PasswordReset resetRecord = passwordResetRepository.findUserByUniqueId(input.getUniqueId());
		if(resetRecord == null) {
			logAction("inside changePassword, no record was found for unique Id = " + input.getUniqueId());
			this.emailService.sendManagementEmail("Interview Program: checkPasswordResetTable issue", "A final check on the password_reset table for the unique id = " + input.getUniqueId() + " has failed, the record was not found!");
			throw new PasswordResetException("We're sorry, but there was an issue with your request");
		}
		if(!resetRecord.getEmailAddr().equals(input.getEmailAddress())) {
			logAction("inside changePassword, email address did not match the one found in the record for unique Id = " + input.getUniqueId());
			this.emailService.sendManagementEmail("Interview Program: checkPasswordResetTable issue", "A final check on the password_reset table for the unique id = " + input.getUniqueId() + " has failed, the email addresses did not match!");
			throw new PasswordResetException("We're sorry, but there was an issue with your request");
		}
		logAction("inside changePassword, password_reset record was found unique Id = " + input.getUniqueId());
		InterviewUser user = repository.findUserByEmailAddr(input.getEmailAddress());
		logAction("inside changePassword, user : " + user.getUserName() + " was retrieved, changing their password");
		String encodedPassword = encodePassword(input.getPassword());
		user.setPassword(encodedPassword);
		repository.save(user);
		passwordResetRepository.delete(resetRecord);
		logAction("leaving changePassword, user : " + user.getUserName());
		return user.toDTO();
	}
	
	/**
	 * logger method
	 * @param message
	 */
	private static void logBothAction(String message) {
		System.out.println("UserServiceImpl: " + message);
		applicationLogger.debug("UserServiceImpl: " + message);
		emailLogger.debug("UserServiceImpl: " + message);
	}
	/**
	 * logger method
	 * @param message
	 */
	private static void logAction(String message) {
    	System.out.println("UserServiceImpl: " + message);
    	applicationLogger.debug("UserServiceImpl: " + message);
    }
	
	/**
	 * logging method
	 * @param message
	 */
	private static void logEmailAction(String message) {
		System.out.println("UserServiceImpl: " + message);
		emailLogger.debug("UserServiceImpl: " + message);
	}

}
