/********************************************************************** 
 *
 *            Copyright (c) 2014 Citizant, Inc.
 * 
 **********************************************************************/
package com.citizant.fraudshield.bean;

import java.io.Serializable;
import java.util.List;


public class CustomerBean implements Serializable {
	
	private Long personId;	
	private String firstName;	
	private String middleInitial;
	private String lastName;
	private String prefix;
	private String suffix;
	private String ssn;
	private String dob;
	private String phone1;
	private String phone2;
	private String email;
	private String fraudShieldID;
	private boolean active;
	
	private Long addressId;
	private String address;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String zip4;
	
	List<ScannedIdentityBean> idDocs;
	
	private String tsoUser; 

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getFraudShieldID() {
		return fraudShieldID;
	}

	public void setFraudShieldID(String fraudShieldID) {
		this.fraudShieldID = fraudShieldID;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	

	
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getZip4() {
		return zip4;
	}

	public void setZip4(String zip4) {
		this.zip4 = zip4;
	}

	public List<ScannedIdentityBean> getIdDocs() {
		return idDocs;
	}

	public void setIdDocs(List<ScannedIdentityBean> idDocs) {
		this.idDocs = idDocs;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	
	public void setTsoUser(String tsoUser){
		this.tsoUser = tsoUser;
	}
	
	public String getTsoUser(){
		return this.tsoUser;
	}
	
	public boolean isValid() {
		if(!isExist(this.firstName) || !isExist(this.lastName) || !isExist(this.dob)
				|| !isExist(this.ssn) || !isExist(this.address) || !isExist(this.state) 
				|| !isExist(this.city) || !isExist(this.zip) || !isExist(this.email)
		) {
			return false;
		}
		
		return true;
	}
	
	private boolean isExist(String s) {
		if(s!=null && s.trim().length()>0) {
			return true;
		}
		return false;
	}
}
