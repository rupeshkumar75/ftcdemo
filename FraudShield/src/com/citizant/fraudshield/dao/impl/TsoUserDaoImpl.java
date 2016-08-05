package com.citizant.fraudshield.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.citizant.fraudshield.dao.TsoUserDao;
import com.citizant.fraudshield.domain.TsoUser;

@Transactional
@Repository 
public class TsoUserDaoImpl extends DaoBaseImpl implements TsoUserDao {

	@Override
	public void init() {
		
	}

	@Override
	public TsoUser getUserByuserName(String username) {
		Criteria criteria = super.getCurrentSession().createCriteria(TsoUser.class);
	    criteria.add(Restrictions.eq("username", username));	
	    TsoUser user = (TsoUser)criteria.uniqueResult();	 
	    return user;
	}
	
	@Override
	public TsoUser getUserByEmail(String email) {
		Criteria criteria = super.getCurrentSession().createCriteria(TsoUser.class);
	    criteria.add(Restrictions.eq("contactEmail", email));	
	    TsoUser user = (TsoUser)criteria.uniqueResult();	 
	    return user;
	}
	
	@Override
	public TsoUser getUserByResetCode(String resetCode) {
		Criteria criteria = super.getCurrentSession().createCriteria(TsoUser.class);
	    criteria.add(Restrictions.eq("resetCode", resetCode));	
	    TsoUser user = (TsoUser)criteria.uniqueResult();	 
	    return user;
	}
	
	
	@Override
	public void saveUser(TsoUser user) {
		super.saveOrUpdate(user);	
	}
	
	
	public int getNumOfUsers() {
		Criteria criteria = super.getCurrentSession().createCriteria(TsoUser.class);
		criteria.setProjection(Projections.rowCount());
		Number n = (Number)criteria.uniqueResult();
		return n.intValue();
	}

}
