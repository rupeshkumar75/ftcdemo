package com.citizant.fraudshield.bean;

import java.io.Serializable;

public class SearchBean implements Serializable {
	private String fraudShiledId;
	private String firstName;
	private String lastName;
	private String ssn;
	private String address;
	private String idDocType;
	private String dob;
	
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
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFraudShiledId() {
		return fraudShiledId;
	}
	public void setFraudShiledId(String fraudShiledId) {
		this.fraudShiledId = fraudShiledId;
	}
	public String getIdDocType() {
		return idDocType;
	}
	public void setIdDocType(String idDocType) {
		this.idDocType = idDocType;
	}
	
	
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public void resetCriteria() {
		firstName = null;
		lastName = null;
		ssn = null;
		address = null;
		fraudShiledId = null;
		idDocType = null;
	}
}
