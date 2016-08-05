package com.citizant.fraudshield.dao.impl;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.citizant.fraudshield.dao.CustomerDao;
import com.citizant.fraudshield.domain.Person;

import com.citizant.fraudshield.domain.PersonIdentification;
import com.citizant.fraudshield.security.EncryptionUtil;


@Transactional
@Repository 
public class CustomerDaoImpl  extends DaoBaseImpl implements CustomerDao {

	@Override
	public Person getCustomerById(Long id) {
		
	    return new Person();
	}

	@Override
	public Boolean saveCustomer(Person customer) {
		
		return Boolean.TRUE;
		
	}
	
	@Override
	public void saveDocument(PersonIdentification doc){
		
	}
	
	@Override
	public void deleteDocument(Long id){
		
	
		
	}
	
	
	public List<PersonIdentification> getExpiredDocuments(Date exp) {
		return new ArrayList<PersonIdentification>();
	}
	
	@Override
	public List<PersonIdentification> getPersonIds(Long customerId){
		return new ArrayList<PersonIdentification>();
	}

	@Override
	public void init() {
		
	}
	@Override
	public List<Person> getAllCustomers(List<Order> sort){
	
		return  new ArrayList<Person>();
	}
	
	public List<Person> getCustomerBySsn(String ssn) throws NoSuchPaddingException, GeneralSecurityException {
		return  new ArrayList<Person>();
	}

	
	
	public List<Person> getExpiredActiveCustomers(Date endday) {
		return  new ArrayList<Person>();
	}

	
	public boolean isFraudShieldIDUnique(String fsId) {
		
		return true;
	}
	
	
	public List<Person> getRegistrationsInTimeFrame(Date start, Date end) {
		return  new ArrayList<Person>();
	}

}
