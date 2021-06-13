package com.schnarbiesnmeowers.interview.pojos;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.Gson;
import com.schnarbiesnmeowers.interview.dtos.InterviewUserTempDTO;
/**
 *
 * @author Dylan I. Kessler
 *
 */
@Entity
@Table(name = "password_reset")
public class PasswordReset {

	// default serial version id, required for serializable classes
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	@Column(name = "password_reset_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer passwordResetId;

	/**
	 * 
	 */
	@Column(name = "unique_id")
	private String uniqueId;
	
	/**
	 * 
	 */
	@Column(name = "email_addr")
	private String emailAddr;
	
	/**
	 * 
	 */
	@Column(name = "created_date")
	private Date createdDate;

	public PasswordReset() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PasswordReset(Integer passwordResetId, String uniqueId, String emailAddr, Date createdDate) {
		super();
		this.passwordResetId = passwordResetId;
		this.uniqueId = uniqueId;
		this.emailAddr = emailAddr;
		this.createdDate = createdDate;
	}

	public Integer getPasswordResetId() {
		return passwordResetId;
	}

	public void setPasswordResetId(Integer passwordResetId) {
		this.passwordResetId = passwordResetId;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getEmailAddr() {
		return emailAddr;
	}

	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	
}
