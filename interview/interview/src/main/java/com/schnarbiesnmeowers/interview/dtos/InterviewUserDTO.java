package com.schnarbiesnmeowers.interview.dtos;

import com.schnarbiesnmeowers.interview.pojos.InterviewUser;
import javax.validation.constraints.*;
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
public class InterviewUserDTO implements Serializable {
	// default serial version id, required for serializable classes
	private static final long serialVersionUID = 1L;

	//private static final Logger applicationLogger = LogManager.getLogger("FileAppender");

	/**
	 * 
	 */
	private Integer userId;

	/**
	 * 
	 */
	private String[] authorizations;

	/**
	 * 
	 */
	private String emailaddr;

	/**
	 * 
	 */
	private String firstname;

	/**
	 * 
	 */
	private boolean isuseractive;

	/**
	 * 
	 */
	private boolean isusernotlocked;

	/**
	 * 
	 */
	private Date joindate;

	/**
	 * 
	 */
	private Date lastlogindate;

	/**
	 * 
	 */
	private Date lastlogindatedisplay;

	/**
	 * 
	 */
	private String lastname;

	/**
	 * 
	 */
	private String password;

	/**
	 * 
	 */
	private String profileimage;

	/**
	 * 
	 */
	private String roles;

	/**
	 * 
	 */
	private String useridentifier;

	/**
	 * 
	 */
	private String username;

	/**
	 * default constructor
	 */
	public InterviewUserDTO() {
		super();
	}

	public InterviewUserDTO(Integer userId, String[] authorizations, String emailaddr, String firstname, boolean isuseractive, boolean isusernotlocked, Date joindate, Date lastlogindate, Date lastlogindatedisplay, String lastname, String password, String profileimage, String roles, String useridentifier, String username) {
		super();
		this.userId = userId;
		this.authorizations = authorizations;
		this.emailaddr = emailaddr;
		this.firstname = firstname;
		this.isuseractive = isuseractive;
		this.isusernotlocked = isusernotlocked;
		this.joindate = joindate;
		this.lastlogindate = lastlogindate;
		this.lastlogindatedisplay = lastlogindatedisplay;
		this.lastname = lastname;
		this.password = password;
		this.profileimage = profileimage;
		this.roles = roles;
		this.useridentifier = useridentifier;
		this.username = username;
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

	public String getEmailaddr() {
		return emailaddr;
	}

	public void setEmailaddr(String emailaddr) {
		this.emailaddr=emailaddr;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname=firstname;
	}

	public boolean getIsuseractive() {
		return isuseractive;
	}

	public void setIsuseractive(boolean isuseractive) {
		this.isuseractive=isuseractive;
	}

	public boolean getIsusernotlocked() {
		return isusernotlocked;
	}

	public void setIsusernotlocked(boolean isusernotlocked) {
		this.isusernotlocked=isusernotlocked;
	}

	public Date getJoindate() {
		return joindate;
	}

	public void setJoindate(Date joindate) {
		this.joindate=joindate;
	}

	public Date getLastlogindate() {
		return lastlogindate;
	}

	public void setLastlogindate(Date lastlogindate) {
		this.lastlogindate=lastlogindate;
	}

	public Date getLastlogindatedisplay() {
		return lastlogindatedisplay;
	}

	public void setLastlogindatedisplay(Date lastlogindatedisplay) {
		this.lastlogindatedisplay=lastlogindatedisplay;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname=lastname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password=password;
	}

	public String getProfileimage() {
		return profileimage;
	}

	public void setProfileimage(String profileimage) {
		this.profileimage=profileimage;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles=roles;
	}

	public String getUseridentifier() {
		return useridentifier;
	}

	public void setUseridentifier(String useridentifier) {
		this.useridentifier=useridentifier;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username=username;
	}

	@Override
	public String toString() {
		return "InterviewUserDTO [userId=" + userId + ", authorizations=" + authorizations + ", emailaddr=" + emailaddr + ", firstname=" + firstname + ", isuseractive=" + isuseractive + ", isusernotlocked=" + isusernotlocked + ", joindate=" + joindate + ", lastlogindate=" + lastlogindate + ", lastlogindatedisplay=" + lastlogindatedisplay + ", lastname=" + lastname + ", password=" + password + ", profileimage=" + profileimage + ", roles=" + roles + ", useridentifier=" + useridentifier + ", username=" + username + "]";
	}

	public static InterviewUserDTO fromJson(String input) {
		Gson gson = new Gson();
		return gson.fromJson(input, InterviewUserDTO.class );
	}
	public InterviewUser toEntity() {
		return new InterviewUser(this.getUserId(),this.getAuthorizations(),this.getEmailaddr(),this.getFirstname(),this.getIsuseractive(),this.getIsusernotlocked(),this.getJoindate(),this.getLastlogindate(),this.getLastlogindatedisplay(),this.getLastname(),this.getPassword(),this.getProfileimage(),this.getRoles(),this.getUseridentifier(),this.getUsername());
	}
}
