package com.schnarbiesnmeowers.interview.dtos;

import com.schnarbiesnmeowers.interview.pojos.InterviewUser;
import javax.validation.constraints.*;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import java.math.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 *
 * @author Dylan I. Kessler
 *
 */
public class InterviewUserDTO implements Serializable {
	// default serial version id, required for serializable classes
	private static final long serialVersionUID = 1L;

	//private static final Logger applicationLogger = LogManager.getLogger("FileAppender");

	/**
	 * 
	 */
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Integer userId;

	/**
	 * 
	 */
	private String[] authorizations;

	/**
	 * 
	 */
	private String emailAddr;

	/**
	 * 
	 */
	private String firstName;

	/**
	 * 
	 */
	private boolean userActive;

	/**
	 * 
	 */
	private boolean userNotLocked;

	/**
	 * 
	 */
	private Date joinDate;

	/**
	 * 
	 */
	private Date lastLoginDate;

	/**
	 * 
	 */
	private Date lastLoginDateDisplay;

	/**
	 * 
	 */
	private String lastName;

	/**
	 * 
	 */
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	/**
	 * 
	 */
	private String profileImage;

	/**
	 * 
	 */
	private String roles;

	/**
	 * 
	 */
	private String userIdentifier;

	/**
	 * 
	 */
	private String userName;

	/**
	 * default constructor
	 */
	public InterviewUserDTO() {
		super();
	}

	public InterviewUserDTO(Integer userId, String[] authorizations, String emailAddr, String firstName, boolean userActive, boolean userNotLocked, Date joinDate, Date lastLoginDate, Date lastLoginDateDisplay, String lastName, String password, String profileImage, String roles, String userIdentifier, String userName) {
		super();
		this.userId = userId;
		this.authorizations = authorizations;
		this.emailAddr = emailAddr;
		this.firstName = firstName;
		this.userActive = userActive;
		this.userNotLocked = userNotLocked;
		this.joinDate = joinDate;
		this.lastLoginDate = lastLoginDate;
		this.lastLoginDateDisplay = lastLoginDateDisplay;
		this.lastName = lastName;
		this.password = password;
		this.profileImage = profileImage;
		this.roles = roles;
		this.userIdentifier = userIdentifier;
		this.userName = userName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId=userId;
	}

	public String[] getAuthorizations() {
		return authorizations;
	}

	public void setAuthorizations(String[] authorizations) {
		this.authorizations=authorizations;
	}

	public String getEmailAddr() {
		return emailAddr;
	}

	public void setEmailAddr(String emailAddr) {
		this.emailAddr=emailAddr;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName=firstName;
	}

	public boolean isUserActive() {
		return userActive;
	}

	public void setUserActive(boolean userActive) {
		this.userActive=userActive;
	}

	public boolean isUserNotLocked() {
		return userNotLocked;
	}

	public void setUserNotLocked(boolean userNotLocked) {
		this.userNotLocked=userNotLocked;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate=joinDate;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate=lastLoginDate;
	}

	public Date getLastLoginDateDisplay() {
		return lastLoginDateDisplay;
	}

	public void setLastLoginDateDisplay(Date lastLoginDateDisplay) {
		this.lastLoginDateDisplay=lastLoginDateDisplay;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName=lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password=password;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage=profileImage;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles=roles;
	}

	public String getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier=userIdentifier;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName=userName;
	}

	@Override
	public String toString() {
		return "InterviewUserDTO [userId=" + userId + ", authorizations=" + authorizations + ", emailAddr=" + emailAddr + ", firstName=" + firstName + ", userActive=" + userActive + ", userNotLocked=" + userNotLocked + ", joinDate=" + joinDate + ", lastLoginDate=" + lastLoginDate + ", lastLoginDateDisplay=" + lastLoginDateDisplay + ", lastName=" + lastName + ", password=" + password + ", profileImage=" + profileImage + ", roles=" + roles + ", userIdentifier=" + userIdentifier + ", userName=" + userName + "]";
	}

	public static InterviewUserDTO fromJson(String input) {
		Gson gson = new Gson();
		return gson.fromJson(input, InterviewUserDTO.class );
	}
	public InterviewUser toEntity() {
		return new InterviewUser(this.getUserId(),this.getAuthorizations(),this.getEmailAddr(),this.getFirstName(),this.isUserActive(),this.isUserNotLocked(),this.getJoinDate(),this.getLastLoginDate(),this.getLastLoginDateDisplay(),this.getLastName(),this.getPassword(),this.getProfileImage(),this.getRoles(),this.getUserIdentifier(),this.getUserName());
	}
}
