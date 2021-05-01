package com.schnarbiesnmeowers.interview.dtos;

import java.util.Date;

public class InterviewUserDTOWrapper extends InterviewUserDTO {

	String newEmailAddr;
	String newFirstName;
	String newLastName;
	String newPassword;
	String newUserName;
	public InterviewUserDTOWrapper() {
		super();
		// TODO Auto-generated constructor stub
	}
	public InterviewUserDTOWrapper(InterviewUserDTO record) {
		this.setAuthorizations(record.getAuthorizations());
		this.setEmailAddr(record.getEmailAddr());
		this.setFirstName(record.getFirstName());
		this.setJoinDate(record.getJoinDate());
		this.setLastLoginDate(record.getLastLoginDate());
		this.setLastLoginDateDisplay(record.getLastLoginDateDisplay());
		this.setLastName(record.getLastName());
		this.setPassword(record.getPassword());
		this.setProfileImage(record.getProfileImage());
		this.setRoles(record.getRoles());
		this.setUserActive(record.isUserActive());
		this.setUserNotLocked(record.isUserNotLocked());
		this.setUserIdentifier(record.getUserIdentifier());
		this.setUserName(record.getUserName());
	}
	
	public InterviewUserDTOWrapper(Integer userId, String[] authorizations, String emailAddr, String firstName,
			boolean userActive, boolean userNotLocked, Date joinDate, Date lastLoginDate, Date lastLoginDateDisplay,
			String lastName, String password, String profileImage, String roles, String userIdentifier,
			String userName) {
		super(userId, authorizations, emailAddr, firstName, userActive, userNotLocked, joinDate, lastLoginDate,
				lastLoginDateDisplay, lastName, password, profileImage, roles, userIdentifier, userName);
		// TODO Auto-generated constructor stub
	}
	public String getNewEmailAddr() {
		return newEmailAddr;
	}
	public void setNewEmailAddr(String newEmailAddr) {
		this.newEmailAddr = newEmailAddr;
	}
	public String getNewFirstName() {
		return newFirstName;
	}
	public void setNewFirstName(String newFirstName) {
		this.newFirstName = newFirstName;
	}
	public String getNewLastName() {
		return newLastName;
	}
	public void setNewLastName(String newLastName) {
		this.newLastName = newLastName;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getNewUserName() {
		return newUserName;
	}
	public void setNewUserName(String newUserName) {
		this.newUserName = newUserName;
	}
	
}
