package com.citizant.fraudshield.domain;

import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.Date;

import javax.crypto.NoSuchPaddingException;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.citizant.fraudshield.security.EncryptionUtil;
import com.citizant.fraudshield.util.StringUtil;

@Entity
@Table(name="person")
public class Person extends DomainBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9000009454583815367L;
	
	@Id
	@GeneratedValue
	@Column(name="PERSON_ID")
	private Long personId;
	
	@Column(name="FIRST_NAME")
	private String firstName;
	
	@Column(name="MIDDLE_INITIAL")
	private String middleInitial;
	
	@Column(name="LAST_NAME")
	private String lastName;
	
	@Column(name="PREFIX")
	private String prefix;
	
	@Column(name="SUFFIX")
	private String suffix;
	
	@Column(name="SSN")
	private String ssn;
	
	@Column(name="DOB")
	private String dob;
	
	@Column(name="PHONE_1")
	private String phone1;
	
	@Column(name="PHONE_2")
	private String phone2;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="FRAUD_SHIELD_ID")
	private String fraudShieldID;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="ACTIVE_IND")
	private boolean active = true;
	
	//bi-directional one-to-many association to personaddress
	@OneToOne(fetch = FetchType.EAGER,orphanRemoval=true, mappedBy="person", cascade = CascadeType.ALL)
	private PersonAddress personAddress;

	public PersonAddress getPersonAddress() {
		return personAddress;
	}

	public void setPersonAddress(PersonAddress personAddress) {
		this.personAddress = personAddress;
	}

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

	@Override
	public Long getId() {
		
		return this.personId;
	}
	
	public String getFullName()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(firstName);
		sb.append(" ");
		if (!StringUtil.isEmpty(this.middleInitial))
		{
			sb.append(middleInitial);
			sb.append(" ");
		}
		sb.append(lastName);
		if (!StringUtil.isEmpty(this.suffix))
		{
			sb.append(" ");
			sb.append(suffix);
		}
		
		return sb.toString();
	}
	
	public String getAddress()
	{
		if (this.personAddress != null)
		{
			StringBuilder sb = new StringBuilder();
			sb.append(personAddress.getAddress1());
			sb.append(", ");
			if (!StringUtil.isEmpty(personAddress.getAddress2()))
			{
				sb.append(personAddress.getAddress2());
				sb.append(", ");
			}
			sb.append(personAddress.getCity());
			sb.append(", ");
			sb.append(personAddress.getState());
			sb.append(" ");
			sb.append(personAddress.getZip());
			return sb.toString();
		}
		
		return "";
	}
	
	public String getDecryptedSSN() throws NoSuchPaddingException, GeneralSecurityException
	{
		return EncryptionUtil.decryptString(this.ssn);
	}
	
	public String getDecryptedDOB() throws NoSuchPaddingException, GeneralSecurityException
	{
		return EncryptionUtil.decryptString(this.dob);
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


}
