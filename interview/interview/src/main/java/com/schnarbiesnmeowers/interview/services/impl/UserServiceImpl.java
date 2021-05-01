package com.schnarbiesnmeowers.interview.services.impl;

import static com.schnarbiesnmeowers.interview.utilities.Constants.A_USER_WITH_THIS_EMAIL_ALREADY_EXISTS;
import static com.schnarbiesnmeowers.interview.utilities.Constants.DIRECTORY_CREATED;
import static com.schnarbiesnmeowers.interview.utilities.Constants.DOT;
import static com.schnarbiesnmeowers.interview.utilities.Constants.FILE_SAVED_IN_FILE_SYSTEM;
import static com.schnarbiesnmeowers.interview.utilities.Constants.FORWARD_SLASH;
import static com.schnarbiesnmeowers.interview.utilities.Constants.JPG_EXTENSION;
import static com.schnarbiesnmeowers.interview.utilities.Constants.NOT_AN_IMAGE_FILE;
import static com.schnarbiesnmeowers.interview.utilities.Constants.NO_USER_FOUND_BY_EMAIL;
import static com.schnarbiesnmeowers.interview.utilities.Constants.NO_USER_FOUND_BY_USERNAME;
import static com.schnarbiesnmeowers.interview.utilities.Constants.USERNAME_ALREADY_EXISTS;
import static com.schnarbiesnmeowers.interview.utilities.Constants.USER_FOLDER;
import static com.schnarbiesnmeowers.interview.utilities.Constants.USER_IMAGE_PATH;
import static com.schnarbiesnmeowers.interview.utilities.Constants.USER_NOT_FOUND;
import static com.schnarbiesnmeowers.interview.utilities.Constants.INCORRECT_OLD_PASSWORD;
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
import javax.mail.internet.AddressException;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTOWrapper;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.EmailExistsException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.EmailNotFoundException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.NotAnImageFileException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.PasswordIncorrectException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UserNotFoundException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UsernameExistsException;
import com.schnarbiesnmeowers.interview.pojos.InterviewUser;
import com.schnarbiesnmeowers.interview.security.UserPrincipal;
import com.schnarbiesnmeowers.interview.services.InterviewUserRepository;
import com.schnarbiesnmeowers.interview.services.UserService;
import com.schnarbiesnmeowers.interview.utilities.Roles;

/**
 * 
 * @author dylan
 *
 */
@Service
@Transactional
@Qualifier("UserDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");
	
	private InterviewUserRepository repository;
	private BCryptPasswordEncoder passwordEncoder;
	private LoginAttemptService loginAttemptService;
	private EmailService emailService;
	
	/**
	 * constructor using 
	 * @param repository
	 * @param passwordEncoder
	 * @param loginAttemptService
	 * @param emailService
	 */
	@Autowired
	public UserServiceImpl(InterviewUserRepository repository,BCryptPasswordEncoder passwordEncoder, LoginAttemptService loginAttemptService,EmailService emailService ) {
		super();
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.loginAttemptService = loginAttemptService;
		this.emailService = emailService;
	}

	/**
	 * this is the main method that the Spring Security will call; our program itself does not directly call it
	 * @param username
	 * @returns UserDetails
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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
	 * 
	 * @param user
	 * @throws MessagingException 
	 * @throws AddressException 
	 */
	private void validateLoginAttempt(InterviewUser user) throws AddressException, MessagingException {
		if(user.isUserNotLocked()) {
			if(this.loginAttemptService.hasExceededMaxAttempts(user.getUserName())) {
				user.setUserNotLocked(false);
				this.emailService.sendEmailForLockedAccount(user.getUserName());
			} else {
				user.setUserNotLocked(true);
			}
		} else {
			this.loginAttemptService.evictUserFromLoginCache(user.getUserName());
		}
	}

	/**
	 * 
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
	public InterviewUser register(String firstName, String lastName, String username, String email) throws UserNotFoundException, UsernameExistsException, EmailExistsException, AddressException, MessagingException {
		validateNewUsernameAndEmail(StringUtils.EMPTY,username,email);
		InterviewUser user = new InterviewUser();
		String userIdentifier = generateUserIdentifier();
		user.setUserIdentifier(userIdentifier);
		String password = generatePassword();
		String encodedPassword = encodePassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUserName(username);
		user.setEmailAddr(email);
		user.setJoinDate(new Date());
		user.setPassword(encodedPassword);
		user.setUserActive(true);
		user.setUserNotLocked(true);
		user.setRoles(Roles.ROLE_BASIC_USER.name());
		user.setAuthorizations(Roles.ROLE_BASIC_USER.getAuthorizations());
		user.setProfileImage(getTemporaryImageUrl(username));
		repository.save(user);
		logAction("New user identifier = " + userIdentifier);
		logAction("New user password = " + password);
		this.emailService.sendNewPasswordEmail(firstName, password, email);
		return user;
	}
	
	/**
	 * TEMPORARY method to manually set passwords to what I want them to be - for testing only
	 * @param username
	 * @param password
	 * @return
	 */
	
	 public void setPassword(String username, String password) {
		 logAction("New user identifier = " + username + " to --> " + password);
	 	InterviewUser user = repository.findUserByUserName(username); String
	 	encodedPassword = encodePassword(password);
	 	user.setPassword(encodedPassword); 
	 	repository.save(user); 
	 }
	 
	
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
	private String generatePassword() {
		return RandomStringUtils.randomAlphanumeric(10);
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
        		throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
        	}
        	InterviewUser newUser = findUserByUsername(newUsername);
        	if(newUser != null && !newUser.getUserId().equals(currentUser.getUserId())) {
        		throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
        	}
        	InterviewUser userByEmail = findUserByEmail(newEmail);
        	if(userByEmail != null && !userByEmail.getUserId().equals(currentUser.getUserId())) {
        		throw new EmailExistsException(A_USER_WITH_THIS_EMAIL_ALREADY_EXISTS);
        	}
        	return currentUser;
        } else {
        	// this is a new person trying to register
        	InterviewUser newUser = findUserByUsername(newUsername);
        	if(newUser != null) {
        		throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
        	}
        	InterviewUser userByEmail = findUserByEmail(newEmail);
        	if(userByEmail != null) {
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
	 * 
	 * @param username
	 * @return
	 */
	@Override
	public InterviewUser findUserByUsername(String username) {
		return repository.findUserByUserName(username);
	}

	/**
	 * 
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
	 * 
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
		String password = generatePassword();
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
	 * 
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
	 * 
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
	 * this method will change a user's password, and send them an email with the new password in it
	 * @param email
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws EmailNotFoundException
	 */
	@Override
	public void resetPassword(String email) throws AddressException, MessagingException, EmailNotFoundException {
		InterviewUser user = this.findUserByEmail(email);
		if(user == null) {
			throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
		}
		String password = generatePassword();
		user.setPassword(this.encodePassword(password));
		this.repository.save(user);
		this.emailService.sendNewPasswordEmail(user.getFirstName(),password , email);	
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
			throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
		}
		this.emailService.sendEmailWithUsername(user.getFirstName(),user.getUserName() , email);		
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
	 * 
	 * @param username
	 * @return
	 */
	private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PATH + username + FORWARD_SLASH
        + username + DOT + JPG_EXTENSION).toUriString();
    }
	
	/**
	 * 
	 * @param role
	 * @return
	 */
	private Roles getRoleEnumName(String role) {
		return Roles.valueOf(role.toUpperCase());
	}
	
	/**
	 * 
	 * @param message
	 */
	private static void logAction(String message) {
    	System.out.println(message);
    	applicationLogger.debug(message);
    }

	

}
