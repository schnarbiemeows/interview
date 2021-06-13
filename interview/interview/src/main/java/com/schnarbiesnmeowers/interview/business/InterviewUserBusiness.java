package com.schnarbiesnmeowers.interview.business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTO;
import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTOWrapper;
import com.schnarbiesnmeowers.interview.dtos.InterviewUserTempDTO;
import com.schnarbiesnmeowers.interview.exceptions.ResourceNotFoundException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.EmailExistsException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UserFieldsNotValidException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UserNotFoundException;
import com.schnarbiesnmeowers.interview.exceptions.interviewuser.UsernameExistsException;
import com.schnarbiesnmeowers.interview.pojos.InterviewUser;
import com.schnarbiesnmeowers.interview.services.InterviewUserRepository;
import com.schnarbiesnmeowers.interview.services.UserService;
import com.schnarbiesnmeowers.interview.utilities.Roles;
/**
 * this class retrieves data from the controller class
 * most business logic should be put in this class
 * @author Dylan I. Kessler
 *
 */
@Component
public class InterviewUserBusiness {

	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");
    public static final String ID_EQUALS = "id = ";
    public static final String NOT_FOUND = " not found";
    public static final String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
	/**
	 * JPA Repository handle
	 */
	@Autowired
	private InterviewUserRepository service;

	@Autowired
	UserService userService;
	/**
	 * get all InterviewUser records
	 * @return
	 * @throws Exception
	 */
	public List<InterviewUserDTO> getAllInterviewUser() throws Exception {
		Iterable<InterviewUser> interviewuser = service.findAll();
		Iterator<InterviewUser> interviewusers = interviewuser.iterator();
		List<InterviewUserDTO> interviewuserdto = new ArrayList();
		while(interviewusers.hasNext()) {
			InterviewUser item = interviewusers.next();
			interviewuserdto.add(item.toDTO());
		}
		return interviewuserdto;
	}

	/**
	 * get InterviewUser by primary key
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public InterviewUserDTO findInterviewUserById(int id) throws Exception {
		Optional<InterviewUser> interviewuserOptional = service.findById(id);
		if(interviewuserOptional.isPresent()) {
			InterviewUser results = interviewuserOptional.get();
			return results.toDTO();
		} else {
			throw new ResourceNotFoundException(ID_EQUALS + id + NOT_FOUND);
		}
	}

	/**
	 * create a new InterviewUser
	 * @param data
	 * @return
	 * @throws EmailExistsException 
	 * @throws UsernameExistsException 
	 * @throws UserNotFoundException 
	 */
	public InterviewUserDTO createInterviewUser(InterviewUserDTO data, String[] authorizations, String adminUser) throws UserNotFoundException, UsernameExistsException, EmailExistsException {
		userService.validateNewUsernameAndEmail(StringUtils.EMPTY,data.getUserName(),data.getEmailAddr());
		InterviewUser user = new InterviewUser();
		InterviewUserDTOWrapper userwrapper = new InterviewUserDTOWrapper(data);
		userwrapper.setNewUserName(userwrapper.getUserName());
		copyFormPropertiesToUserRecord(user,userwrapper,authorizations, adminUser, user.getUserName());
		String userId = userService.generateUserIdentifier();
		user.setUserIdentifier(userId);
		user.setJoinDate(new Date());
		user = service.save(user);
		return user.toDTO();
	}

	/**
	 * update a InterviewUser
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public InterviewUserDTO updateInterviewUser(InterviewUserDTOWrapper data, String[] authorizations, String adminUser) throws UserNotFoundException, UsernameExistsException, EmailExistsException, IOException, AccessDeniedException {
		InterviewUser user = userService.validateNewUsernameAndEmail(data.getUserName(),data.getNewUserName(),data.getEmailAddr());
		copyFormPropertiesToUserRecord(user,data,authorizations, adminUser, user.getUserName());
		user = service.save(user);
		return user.toDTO();
	}

	/**
	 * this method will copy all of the new properties to the InterviewUser record
	 * for this method, the only one of the "new" fields that I am going to use is the
	 * newUserName, because I need to retain the old one to make sure I know which record to look for
	 * in the database, since the UserId is never passed out to the UI
	 * @param user
	 * @param data
	 */
	private void copyFormPropertiesToUserRecord(InterviewUser user, InterviewUserDTOWrapper data, String[] authorizations, String adminUser, String userName) throws AccessDeniedException {
		if(userEqualsUser(adminUser, userName) || adminRoleHigherThanUserRole(data.getRoles(),authorizations)) {
			user.setUserIdentifier(data.getUserIdentifier());
			if(data.getPassword()!=null&&!data.getPassword().isEmpty()) {
				String encodedPassword = userService.encodePassword(data.getPassword());
				user.setPassword(encodedPassword);
			}
			user.setFirstName(data.getFirstName());
			user.setLastName(data.getLastName());
			user.setUserName(data.getNewUserName());
			user.setEmailAddr(data.getEmailAddr());
			user.setUserActive(data.isUserActive());
			user.setUserNotLocked(data.isUserNotLocked());
			setRolesAndAuthorization(user, data);
			user.setUserIdentifier(data.getUserIdentifier());
			user.setProfileImage(userService.getTemporaryImageUrl(data.getProfileImage()));
		} else {
			throw new AccessDeniedException("You do not have enough permissions to perform this action.");
		}
	}

	/**
	 * this method sets the user's roles and authorizations
	 * @param user
	 * @param data
	 * @throws AccessDeniedException
	 */
	private void setRolesAndAuthorization(InterviewUser user, InterviewUserDTOWrapper data) throws AccessDeniedException {
		String role = data.getRoles();
		switch(role) {
			case "ROLE_BASIC_USER" : 
				user.setRoles(Roles.ROLE_BASIC_USER.name());
				user.setAuthorizations(Roles.ROLE_BASIC_USER.getAuthorizations());
				break;
			case "ROLE_ADV_USER" :
				user.setRoles(Roles.ROLE_ADV_USER.name());
				user.setAuthorizations(Roles.ROLE_ADV_USER.getAuthorizations());
				break;
			case "ROLE_PREMIUM_USER" :
				user.setRoles(Roles.ROLE_PREMIUM_USER.name());
				user.setAuthorizations(Roles.ROLE_PREMIUM_USER.getAuthorizations());
				break;
			case "ROLE_ADMIN" :
				user.setRoles(Roles.ROLE_ADMIN.name());
				user.setAuthorizations(Roles.ROLE_ADMIN.getAuthorizations());
				break;
			case "ROLE_SUPER" :
				user.setRoles(Roles.ROLE_SUPER.name());
				user.setAuthorizations(Roles.ROLE_SUPER.getAuthorizations());
				break;
			default :
				throw new AccessDeniedException("You do not have enough permissions to perform this action.");
		}
				
	}

	/**
	 * this method determines if the person changing/deleting a record is the same as the record
	 * - if the person is changing their own record, they can change it, but not delete it
	 * @param adminUser
	 * @param userName
	 * @return
	 */
	private boolean userEqualsUser(String adminUser, String userName) {
		if(adminUser.equals(userName)) return true;
		return false;
	}
	/**
	 * this method determines if the person changing the record has the right permissions to change it
	 * we need this method because this could be an admin changing a user's record, a super admin
	 * changing another admin's record, or even an admin changing their OWN record
	 * @param roles = the role level of the record to be changed
	 * @param authorizations = the authorizations of the person making the change
	 * @param adminUser = the username of the person making the change
	 * @param userName = the username of the record making the change
	 * @return
	 */
	private boolean adminRoleHigherThanUserRole(String roles, String[] authorizations) {
		// super admins can change any record
		String authJoin = String.join(",", authorizations);
		if(authJoin.contains("admin:create") || authJoin.contains("admin:update") || authJoin.contains("admin:delete")) return true;
		// finally, if it is an admin, changing a user record, they can
		boolean hasAuthority = false;
		switch(roles) {
			case "ROLE_BASIC_USER" : 
				hasAuthority = true;
				break;
			case "ROLE_ADV_USER" :
				hasAuthority = true;
				break;
			case "ROLE_PREMIUM_USER" :
				hasAuthority = true;
				break;
			default :
				hasAuthority = false;
				break;
		}
		return hasAuthority;
	}

	/**
	 * delete a InterviewUser by primary key
	 * we are either deleting an admin, or deleting a user
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String deleteInterviewUser(String username, String[] authorizations, String adminUser) throws ResourceNotFoundException {
		try {
			InterviewUser user = userService.findUserByUsername(username);
			if(!userEqualsUser(adminUser, username) && adminRoleHigherThanUserRole(user.getRoles(),authorizations)) {
				service.deleteById(user.getUserId());
			}
		}
		catch(ResourceNotFoundException e) {
			throw new ResourceNotFoundException(ID_EQUALS + username + NOT_FOUND);
		}
		return "Successfully Deleted";
	}
	
	private static void logAction(String message) {
    	System.out.println(message);
    	applicationLogger.debug(message);
    }

	/**
	 * this method checks to make sure the restration data that a new user has entered is valid
	 * @param user
	 * @throws UserFieldsNotValidException
	 */
	public void validateFields(InterviewUserTempDTO user) throws UserFieldsNotValidException {
		if(user.getUserName()==null||user.getUserName().isEmpty()) {
			throw new UserFieldsNotValidException("username must have a value");
		}
		if(user.getEmailAddr()==null||user.getEmailAddr().isEmpty()) {
			throw new UserFieldsNotValidException("email must have a value");
		}
		if(user.getFirstName()==null||user.getFirstName().isEmpty()) {
			throw new UserFieldsNotValidException("first name must have a value");
		}
		if(user.getLastName()==null||user.getLastName().isEmpty()) {
			throw new UserFieldsNotValidException("last name must have a value");
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(user.getEmailAddr());
		if(!matcher.matches()) {
			throw new UserFieldsNotValidException("this email address is not valid");
		}
	}

}
