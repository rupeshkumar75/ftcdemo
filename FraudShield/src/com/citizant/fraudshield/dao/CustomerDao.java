package com.citizant.fraudshield.dao;

import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import org.hibernate.criterion.Order;

import com.citizant.fraudshield.domain.Person;
import com.citizant.fraudshield.domain.PersonIdentification;

public interface CustomerDao {
	public Person getCustomerById(Long id);
	public Boolean saveCustomer(Person customer);
	public void deleteDocument(Long id);
	public List<PersonIdentification> getPersonIds(Long customerId);
	public void saveDocument(PersonIdentification doc);
	public List<Person> getAllCustomers(List<Order> sort);
	public List<Person> getExpiredActiveCustomers(Date today);
	public List<Person> getCustomerBySsn(String ssn) throws NoSuchPaddingException, GeneralSecurityException;
	public boolean isFraudShieldIDUnique(String fsId);
	
	public List<Person> getRegistrationsInTimeFrame(Date start, Date end);
	
	public List<PersonIdentification> getExpiredDocuments(Date exp);
}
