package com.citizant.fraudshield.dao.impl;

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
		super.saveOrUpdate(request);
	}

	@Override
	public void init() {
		
	}
	
	@Override
	public RenewRequest getRenewRequestByCode(String requestCode) {
		Criteria criteria = super.getCurrentSession().createCriteria(RenewRequest.class);
	    criteria.add(Restrictions.eq("requestCode", requestCode));
	    RenewRequest req = (RenewRequest)criteria.uniqueResult();	 
	    return req;		
	}
	
	@Override
	public RenewRequest getRenewRequestByFid(String fid) {
		Criteria criteria = super.getCurrentSession().createCriteria(RenewRequest.class);
	    criteria.add(Restrictions.eq("origFid", fid));
	    criteria.add(Restrictions.eq("status", "NEW"));
	    RenewRequest req = (RenewRequest)criteria.uniqueResult();	 
	    return req;				
	}
	
	@Override
	public List<RenewRequest> getAllRequests() {
		Criteria criteria = super.getCurrentSession().createCriteria(RenewRequest.class);
		return (List<RenewRequest>)criteria.list();
	}
}
