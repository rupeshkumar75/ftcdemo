package com.citizant.fraudshield.dao.impl;

import java.security.GeneralSecurityException;
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
		Criteria criteria = super.getCurrentSession().createCriteria(Person.class);
	    criteria.add(Restrictions.eq("id", id));
	    Person user = (Person)criteria.uniqueResult();
	    return user;
	}

	@Override
	public Boolean saveCustomer(Person customer) {
		super.saveOrUpdate(customer);
		return Boolean.TRUE;
		
	}
	
	@Override
	public void saveDocument(PersonIdentification doc){
		super.saveOrUpdate(doc);
	}
	
	@Override
	public void deleteDocument(Long id){
		
		Criteria criteria = super.getCurrentSession().createCriteria(PersonIdentification.class);
	    criteria.add(Restrictions.eq("personIdentificationId", id));	
	    PersonIdentification doc = (PersonIdentification)criteria.uniqueResult();	 
	    super.delete(doc);
		
	}
	
	
	public List<PersonIdentification> getExpiredDocuments(Date exp) {
		Criteria criteria = super.getCurrentSession().createCriteria(PersonIdentification.class);
	    criteria.add(Restrictions.lt("identificationExpDate", exp));	
	    return criteria.list();	 
	}
	
	@Override
	public List<PersonIdentification> getPersonIds(Long customerId){
		Criteria criteria = super.getCurrentSession().createCriteria(PersonIdentification.class);
	    criteria.add(Restrictions.eq("personId", customerId));	
	    criteria.addOrder(Order.asc("personIdentificationId"));	
	    @SuppressWarnings("unchecked")
		List<PersonIdentification> docs = (List<PersonIdentification>)criteria.list();
		return docs;
	}

	@Override
	public void init() {
		
	}
	@Override
	public List<Person> getAllCustomers(List<Order> sort){
		Criteria criteria = super.getCurrentSession().createCriteria(Person.class)
				.createAlias("personAddress", "personAddress", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("active", Boolean.TRUE));
		if (sort != null && sort.size() > 0)
		{
			for (int i=0;i<sort.size();i++)
			{
				criteria.addOrder(sort.get(i));
			}
		}
		else
		{
			criteria.addOrder(Order.asc("createdDate"));
			criteria.addOrder(Order.asc("firstName"));
			criteria.addOrder(Order.asc("middleInitial"));
			criteria.addOrder(Order.asc("lastName"));
		}
		return criteria.list();
	}
	
	public List<Person> getCustomerBySsn(String ssn) throws NoSuchPaddingException, GeneralSecurityException {
		Criteria criteria = super.getCurrentSession().createCriteria(Person.class);
		criteria.add(Restrictions.eq("ssn", EncryptionUtil.encryptString(ssn)));
		criteria.add(Restrictions.eq("active", Boolean.TRUE));
		return criteria.list();
	}

	
	
	public List<Person> getExpiredActiveCustomers(Date endday) {
		Criteria criteria = super.getCurrentSession().createCriteria(Person.class);
		criteria.add(Restrictions.lt("createdDate", endday));
		criteria.add(Restrictions.eq("active", Boolean.TRUE));
	    return criteria.list();	 
	}

	
	public boolean isFraudShieldIDUnique(String fsId) {
		Criteria criteria = super.getCurrentSession().createCriteria(Person.class);
	    criteria.add(Restrictions.eq("fraudShieldID", fsId));
	    Person user = (Person)criteria.uniqueResult();
	    if (user != null) {
	    	return false;
	    }
		return true;
	}
	
	
	public List<Person> getRegistrationsInTimeFrame(Date start, Date end) {
		Criteria criteria = super.getCurrentSession().createCriteria(Person.class);
		criteria.add(Restrictions.ge("createdDate", start));
		criteria.add(Restrictions.le("createdDate", end));
		return criteria.list();
	}

}
