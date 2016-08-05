package com.citizant.fraudshield.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.citizant.fraudshield.common.ActivityType;
import com.citizant.fraudshield.dao.ActivityLogDao;
import com.citizant.fraudshield.domain.ActivityLog;
import com.citizant.fraudshield.service.ActivityLogService;

public class ActivityLogServiceImpl implements ActivityLogService {
	
	@Autowired
	ActivityLogDao activityLogDao;

	@Override
	public void log(String username, ActivityType activityType, String activity, Long target) {
		
		ActivityLog log = new ActivityLog();
		log.setActivity(activity);
		log.setActivityDate(new Date());
		log.setActivityBy(username);
		log.setActivityTarget(target);
		log.setActivityType(activityType.getKey());
		activityLogDao.log( log );
	}
	
	
	public int getNumOfActivities(ActivityType activityTye, Date start, Date end) {
		List<ActivityLog> logs = activityLogDao.getActivities(activityTye.getKey(), start, end);
		return logs.size();
	}

}
