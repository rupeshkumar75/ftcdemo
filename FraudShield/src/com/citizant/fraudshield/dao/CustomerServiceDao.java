package com.citizant.fraudshield.dao;

import java.security.GeneralSecurityException;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import com.citizant.fraudshield.domain.Person;

public interface CustomerServiceDao {
	public List<Person> searchPerson(
			String fName, 
			String lName, 
			String ssn, 
			String address, 
			String docType) throws NoSuchPaddingException, GeneralSecurityException;
	
	public Person retrieveCustomer(
			String fName, 
			String lName, 
			String ssn, 
			String dob) throws NoSuchPaddingException, GeneralSecurityException;
}
