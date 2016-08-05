package com.citizant.fraudshield.dao.impl;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.citizant.fraudshield.dao.CustomerServiceDao;
import com.citizant.fraudshield.domain.Person;
import com.citizant.fraudshield.security.EncryptionUtil;
import com.citizant.fraudshield.util.StringUtil;

@Transactional
@Repository
public class CustomerServiceDaoImpl extends DaoBaseImpl implements
		CustomerServiceDao {

	
	@Override
	public List<Person> searchPerson(
			String fName, 
			String lName, 
			String ssn,
			String address,
			String docType) throws NoSuchPaddingException, GeneralSecurityException {
		
		//if (!StringUtil.isEmpty(docType)){
			return searcCustomers(fName, lName, ssn, address, docType);
//		}
//		else
//		{
//			Criteria criteria = super.getCurrentSession().createCriteria(Person.class);
//			if (!StringUtil.isEmpty(fName))
//			{
//				criteria.add(Restrictions.ilike("firstName", fName, MatchMode.ANYWHERE));
//			}
//			if (!StringUtil.isEmpty(lName))
//			{
//				criteria.add(Restrictions.ilike("lastName", lName, MatchMode.ANYWHERE));
//			}
//			if (!StringUtil.isEmpty(ssn))
//			{
//				criteria.add(Restrictions.eq("ssn", EncryptionUtil.encryptString(ssn)));
//			}
//			criteria.add(Restrictions.eq("active", Boolean.TRUE));
//			if (!StringUtil.isEmpty(address))
//			{
//				Criterion crit = Restrictions.or(Restrictions.ilike("address.address1", address, MatchMode.ANYWHERE),
//						Restrictions.ilike("address.address2", address, MatchMode.ANYWHERE));
//				criteria.createAlias("personAddress", "address");
//				criteria.add(crit);
//				//criteria.add(Restrictions.eq("active", Boolean.TRUE));
//			}
//			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//		    
//			return criteria.list();
//		}
	}
	
	private List<Person> searcCustomers(
			String fName, 
			String lName, 
			String ssn,
			String address,
			String docType) throws NoSuchPaddingException, GeneralSecurityException{
		StringBuilder hsql = new StringBuilder();
		hsql.append("select distinct p from Person p");
		if (!StringUtil.isEmpty(address))
		{
			hsql.append(", PersonAddress pa");
		}
		if (!StringUtil.isEmpty(docType))
		{
			hsql.append(", PersonIdentification pi");
		}
		hsql.append(" where p.active = :ai1");
		if (!StringUtil.isEmpty(fName))
		{
			hsql.append(" and UPPER(p.firstName) like :fn");
		}
		if (!StringUtil.isEmpty(lName))
		{
			hsql.append(" and UPPER(p.lastName) like :ln");
		}
		if (!StringUtil.isEmpty(ssn))
		{
			hsql.append(" and p.ssn = :ssn");
		}
		
		if (!StringUtil.isEmpty(address))
		{
			hsql.append(" and pa.person.personId = p.personId");
			hsql.append(" and (UPPER(pa.address1) like :a1 or UPPER(pa.address2) like :a2)");
			hsql.append(" and pa.active = :ai2)");
		}
		if (!StringUtil.isEmpty(docType))
		{
			hsql.append(" and pi.personId = p.personId");
			hsql.append(" and pi.identificationType = :docType");
			hsql.append(" and pi.active = :ai3)");
		}
		
		return  new ArrayList<Person>();
	}
	
	
	public Person retrieveCustomer(
			String fName, 
			String lName, 
			String encryptedSsn, 
			String encryptedDob) throws NoSuchPaddingException, GeneralSecurityException {
		
		return  new Person();
		
	}


	@Override
	public void init() {
		

	}
}
