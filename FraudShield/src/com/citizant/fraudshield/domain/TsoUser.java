package com.citizant.fraudshield.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tso_user")
public class TsoUser extends DomainBase implements Serializable {

	private static final long serialVersionUID = 172991661182119967L;

	@Id
	@GeneratedValue
	@Column(name="TSO_USER_ID")
	private Long tsoUserId;
	
	@Column(name="FIRST_NAME")
	private String firstName;
	
	@Column(name="LAST_NAME")
	private String lastName;
	
	@Column(name="CONTACT_PHONE")
	private String contactPhone;
	
	@Column(name="CONTACT_EMAIL")
	private String contactEmail;
	
	@Column(name="USERNAME")
	private String username;

	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="USER_ROLE")
	private String userRole;
	
	@Column(name="RESET_CODE")
	private String resetCode;
	
	@Column(name="RESET_DATE")
	private Date resetDate;
	
	@Column(name="ACTIVE_IND")
	private boolean active = true;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Long getTsoUserId() {
		return tsoUserId;
	}
	public void setTsoUserId(Long tsoUserId) {
		this.tsoUserId = tsoUserId;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	
	
	
	public String getResetCode() {
		return resetCode;
	}
	public void setResetCode(String resetCode) {
		this.resetCode = resetCode;
	}
	
	
	public Date getResetDate() {
		return resetDate;
	}
	public void setResetDate(Date resetDate) {
		this.resetDate = resetDate;
	}
	@Override
	public Long getId() {
		
		return this.tsoUserId;
	}
}
