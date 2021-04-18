package com.schnarbiesnmeowers.interview.pojos;

import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTO;
import javax.persistence.*;
import java.io.Serializable;
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
@Entity
@Table(name = "interview_user")
public class InterviewUser implements Serializable {
	// default serial version id, required for serializable classes
	private static final long serialVersionUID = 1L;

	//private static final Logger applicationLogger = LogManager.getLogger("FileAppender");

	/**
	 * 
	 */
	@Column(name = "user_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer userId;

	/**
	 * 
	 */
	@Column(name = "authorizations")
	private String[] authorizations;

	/**
	 * 
	 */
	@Column(name = "email_addr")
	private String emailAddr;

	/**
	 * 
	 */
	@Column(name = "first_name")
	private String firstName;

	/**
	 * 
	 */
	@Column(name = "user_active")
	private boolean userActive;

	/**
	 * 
	 */
	@Column(name = "user_not_locked")
	private boolean userNotLocked;

	/**
	 * 
	 */
	@Column(name = "join_date")
	private Date joinDate;

	/**
	 * 
	 */
	@Column(name = "last_login_date")
	private Date lastLoginDate;

	/**
	 * 
	 */
	@Column(name = "last_login_date_display")
	private Date lastLoginDateDisplay;

	/**
	 * 
	 */
	@Column(name = "last_name")
	private String lastName;

	/**
	 * 
	 */
	@Column(name = "password")
	private String password;

	/**
	 * 
	 */
	@Column(name = "profile_image")
	private String profileImage;

	/**
	 * 
	 */
	@Column(name = "roles")
	private String roles;

	/**
	 * 
	 */
	@Column(name = "user_identifier")
	private String userIdentifier;

	/**
	 * 
	 */
	@Column(name = "user_name")
	private String userName;

	/**
	 * default constructor
	 */
	public InterviewUser() {
		super();
	}

	public InterviewUser(Integer userId, String[] authorizations, String emailAddr, String firstName, boolean userActive, boolean userNotLocked, Date joinDate, Date lastLoginDate, Date lastLoginDateDisplay, String lastName, String password, String profileImage, String roles, String userIdentifier, String userName) {
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
		return "InterviewUser [userId=" + userId + ", authorizations=" + authorizations + ", emailAddr=" + emailAddr + ", firstName=" + firstName + ", userActive=" + userActive + ", userNotLocked=" + userNotLocked + ", joinDate=" + joinDate + ", lastLoginDate=" + lastLoginDate + ", lastLoginDateDisplay=" + lastLoginDateDisplay + ", lastName=" + lastName + ", password=" + password + ", profileImage=" + profileImage + ", roles=" + roles + ", userIdentifier=" + userIdentifier + ", userName=" + userName + "]";
	}

	public static InterviewUser fromJson(String input) {
		Gson gson = new Gson();
		return gson.fromJson(input, InterviewUser.class );
	}
	public InterviewUserDTO toDTO() {
		return new InterviewUserDTO(this.getUserId(),this.getAuthorizations(),this.getEmailAddr(),this.getFirstName(),this.isUserActive(),this.isUserNotLocked(),this.getJoinDate(),this.getLastLoginDate(),this.getLastLoginDateDisplay(),this.getLastName(),this.getPassword(),this.getProfileImage(),this.getRoles(),this.getUserIdentifier(),this.getUserName());
	}
}
