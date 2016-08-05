package com.citizant.fraudshield.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="renew_request")
public class RenewRequest  extends DomainBase implements Serializable {
	

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="REQUEST_ID")
	private Long renewId;
	
	@Column(name="CUSTOMER_ID")
	private Long customerId;
	
	@Column(name="ORIG_FID")
	private String origFid;
	
	@Column(name="NEW_FID")
	private String newFid;
	
	@Column(name="REQUEST_DATE")
	private Date requestDate;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="REQUEST_CODE")
	private String requestCode;
	
	@Column(name="INTERVIEW_DATE")
	private Date interviewDate;
	
	@Column(name="COMMENTS")
	private String comments;
	
	@Column(name="AGENT")
	private String agent;
	
	@Column(name="FACE_TEMPLATE")
	private String faceTemplate;
	
	@Column(name="FACE_IMAGE")
	private String faceImage;
	
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

	public Long getRenewId() {
		return renewId;
	}

	public void setRenewId(Long renewId) {
		this.renewId = renewId;
	}

	@Override
	public Long getId() {
		
		return renewId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getFaceTemplate() {
		return faceTemplate;
	}

	public void setFaceTemplate(String faceTemplate) {
		this.faceTemplate = faceTemplate;
	}

	public String getFaceImage() {
		return faceImage;
	}

	public void setFaceImage(String faceImage) {
		this.faceImage = faceImage;
	}
	
	
}
