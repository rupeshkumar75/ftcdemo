package com.citizant.fraudshield.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="person_address")
public class PersonAddress extends DomainBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1704982932504651186L;
	
	@Id
	@GeneratedValue
	@Column(name="PERSON_ADDRESS_ID")
	private Long personAddressId;
	
	@OneToOne
    @JoinColumn(name="PERSON_ID")
	private Person person;
	
	@Column(name="ADDRESS_1")
	private String address1;
	
	@Column(name="ADDRESS_2")
	private String address2;
	
	@Column(name="CITY")
	private String city;
	
	@Column(name="STATE")
	private String state;
	
	@Column(name="ZIP")
	private String zip;
	
	@Column(name="ZIP_PLUS_FOUR")
	private String zipPlusFour;
	
	@Column(name="ACTIVE_IND")
	private boolean active = true;

	public Long getPersonAddressId() {
		return personAddressId;
	}

	public void setPersonAddressId(Long personAddressId) {
		this.personAddressId = personAddressId;
	}

	public Person getPerson() {
		return person;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
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

	public String getZipPlusFour() {
		return zipPlusFour;
	}

	public void setZipPlusFour(String zipPlusFour) {
		this.zipPlusFour = zipPlusFour;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public Long getId() {

		return this.personAddressId;
	}
}
