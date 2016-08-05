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
		 
	    return new TsoUser();
	}
	
	@Override
	public TsoUser getUserByEmail(String email) {
		return new TsoUser();
	}
	
	@Override
	public TsoUser getUserByResetCode(String resetCode) {
		return new TsoUser();
	}
	
	
	@Override
	public void saveUser(TsoUser user) {
		
	}
	
	
	public int getNumOfUsers() {
		
		return 0;
	}

}
