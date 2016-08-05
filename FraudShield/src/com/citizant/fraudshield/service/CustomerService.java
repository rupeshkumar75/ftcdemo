package com.citizant.fraudshield.service;

import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import org.hibernate.criterion.Order;

import com.citizant.fraudshield.domain.Person;
import com.citizant.fraudshield.exception.CustomerServiceException;
import com.citizant.fraudshield.bean.CustomerBean;

public interface CustomerService {
	public List<Person> searchPerson(
			String fName, 
			String lName, 
			String ssn, 
			String address,
			String docType) throws NoSuchPaddingException, GeneralSecurityException;
	
	public Person retrieveCustomer (
			String fName, 
			String lName, 
			String ssn, 
			String dob) throws NoSuchPaddingException, GeneralSecurityException;
	
	public CustomerBean getCustomerById(Long id);
	public Boolean saveCustomer(CustomerBean customer) throws CustomerServiceException;
	public String createFraudShieldID(String ssn);
	public void deleteDocument(Long docId);
	public List<CustomerBean> getAllCustomers(List<Order> sort);
	public void setCustomerRecordInactive(Long id);
	public Person isSSNExits(String ssn);
	public String getCustomerPictureById(Long personId);
	public Person getPersonById(Long id);
	
	public List<Person> getAllRegistrations(Date start, Date end);
}
