package com.schnarbiesnmeowers.interview.services;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.web.multipart.MultipartFile;

import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTOWrapper;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.EmailExistsException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.EmailNotFoundException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.NotAnImageFileException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.PasswordIncorrectException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UserNotFoundException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UsernameExistsException;
import com.schnarbiesnmeowers.interview.pojos.InterviewUser;



/**
 * 
 * @author Dylan I. Kessler
 *
 */
public interface UserService {

	InterviewUser register(String firstName, String lastName, String username, String email) throws UserNotFoundException, UsernameExistsException, EmailExistsException, AddressException, MessagingException;
	public void setPassword(String username, String password);
	public void setRole(String username);
	List<InterviewUser> getAllUsers();
	InterviewUser findUserByUsername(String username);
	List<InterviewUser> getUsersByRole(String role);
	List<InterviewUser> getJustUsers();
	List<InterviewUser> getAdmins();
	InterviewUser findUserByEmail(String email);
	public String encodePassword(String password);
	public String generateUserIdentifier();
	public InterviewUser validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistsException, EmailExistsException ;
	InterviewUser addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNotLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistsException, EmailExistsException, IOException, NotAnImageFileException;
	//InterviewUser updateUser(String currentUserName, String firstName, String lastName, String username, String email, String role, boolean isNotLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistsException, EmailExistsException, IOException, NotAnImageFileException;
	InterviewUser updateUserByUser(InterviewUserDTOWrapper user) throws UserNotFoundException, UsernameExistsException, EmailExistsException, IOException, PasswordIncorrectException;
	void deleteUser(String username) throws IOException;
	void resetPassword(String email) throws AddressException, MessagingException, EmailNotFoundException;
	void forgotUsername(String email) throws AddressException, MessagingException, EmailNotFoundException;
	InterviewUser updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExistsException, EmailExistsException, IOException, NotAnImageFileException;
	public String getTemporaryImageUrl(String username);
	public void testEmail();
}
