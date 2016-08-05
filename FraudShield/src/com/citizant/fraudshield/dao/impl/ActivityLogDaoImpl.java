package com.citizant.fraudshield.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.citizant.fraudshield.dao.ActivityLogDao;
import com.citizant.fraudshield.domain.ActivityLog;


@Transactional
@Repository
public class ActivityLogDaoImpl extends DaoBaseImpl implements ActivityLogDao {

	@Override
	public void log(ActivityLog log) {
		
	}

	@Override
	public List<ActivityLog> getActivities(int type, Date start, Date end) {
		return new ArrayList<ActivityLog>();
	}
	
	@Override
	public void init() {


	}

}
