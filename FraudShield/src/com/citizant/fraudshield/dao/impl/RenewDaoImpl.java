package com.citizant.fraudshield.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.citizant.fraudshield.dao.RenewDao;
import com.citizant.fraudshield.domain.RenewRequest;

@Transactional
@Repository 
public class RenewDaoImpl extends DaoBaseImpl implements RenewDao {
	
	public void saveRequest(RenewRequest request) {
		
	}

	@Override
	public void init() {
		
	}
	
	@Override
	public RenewRequest getRenewRequestByCode(String requestCode) {
		return new RenewRequest();	
	}
	
	@Override
	public RenewRequest getRenewRequestByFid(String fid) {
		return new RenewRequest();
	}
	
	@Override
	public List<RenewRequest> getAllRequests() {
	return new ArrayList<RenewRequest>();
	}
}
