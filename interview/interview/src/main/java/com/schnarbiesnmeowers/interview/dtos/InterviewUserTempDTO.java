package com.schnarbiesnmeowers.interview.dtos;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.schnarbiesnmeowers.interview.pojos.InterviewUserTemp;
/**
 *
 * @author Dylan I. Kessler
 *
 */
public class InterviewUserTempDTO implements Serializable {
	// default serial version id, required for serializable classes
	private static final long serialVersionUID = 1L;

	//private static final Logger applicationLogger = LogManager.getLogger("FileAppender");

	/**
	 * 
	 */
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Integer userTempId;

	/**
	 * 
	 */
	private String uniqueId;
	
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
	 * 
	 */
	private Date createdDate;
	
	/**
	 * default constructor
	 */
	public InterviewUserTempDTO() {
		super();
	}

	public InterviewUserTempDTO(Integer userTempId, String uniqueId,String[] authorizations, String emailAddr, String firstName, boolean userActive, boolean userNotLocked, Date joinDate, Date lastLoginDate, Date lastLoginDateDisplay, String lastName, String password, String profileImage, String roles, String userIdentifier, String userName, Date createdDate) {
		super();
		this.userTempId = userTempId;
		this.uniqueId = uniqueId;
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
		this.createdDate = createdDate;
	}

	public Integer getUserTempId() {
		return userTempId;
	}

	public void setUserTempId(Integer userTempId) {
		this.userTempId=userTempId;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "InterviewUserTempDTO [userId=" + userTempId + ", uniqueId = " + uniqueId + ", authorizations=" + authorizations + ", emailAddr=" + emailAddr + ", firstName=" + firstName + ", userActive=" + userActive + ", userNotLocked=" + userNotLocked + ", joinDate=" + joinDate + ", lastLoginDate=" + lastLoginDate + ", lastLoginDateDisplay=" + lastLoginDateDisplay + ", lastName=" + lastName + ", password=" + password + ", profileImage=" + profileImage + ", roles=" + roles + ", userIdentifier=" + userIdentifier + ", userName=" + userName + ", createdDate=" + createdDate + "]";
	}

	public static InterviewUserTempDTO fromJson(String input) {
		Gson gson = new Gson();
		return gson.fromJson(input, InterviewUserTempDTO.class );
	}
	public InterviewUserTemp toEntity() {
		return new InterviewUserTemp(this.getUserTempId(),this.getUniqueId(), this.getAuthorizations(),this.getEmailAddr(),this.getFirstName(),this.isUserActive(),this.isUserNotLocked(),this.getJoinDate(),this.getLastLoginDate(),this.getLastLoginDateDisplay(),this.getLastName(),this.getPassword(),this.getProfileImage(),this.getRoles(),this.getUserIdentifier(),this.getUserName(),this.getCreatedDate());
	}
}
