package com.citizant.fraudshield.bean;

import java.util.Date;

public class RenewRequestBean {

	private Long renewId;
	
	private Long customerId;
	
	private String origFid;
	
	private String newFid;
	
	private Date requestDate;

	private String status;
	
	private String requestCode;
	
	private Date interviewDate;
	
	private String comments;
	
	private String firstName;
	
	private String lastName;
	
	private String agent;

	public Long getRenewId() {
		return renewId;
	}

	public void setRenewId(Long renewId) {
		this.renewId = renewId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getOrigFid() {
		return origFid;
	}

	public void setOrigFid(String origFid) {
		this.origFid = origFid;
	}

	public String getNewFid() {
		return newFid;
	}

	public void setNewFid(String newFid) {
		this.newFid = newFid;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}

	public Date getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(Date interviewDate) {
		this.interviewDate = interviewDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}
	
	
}
