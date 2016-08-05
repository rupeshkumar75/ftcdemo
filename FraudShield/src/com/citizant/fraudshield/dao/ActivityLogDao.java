package com.citizant.fraudshield.dao;

import java.util.Date;
import java.util.List;

import com.citizant.fraudshield.domain.ActivityLog;

public interface ActivityLogDao {
	public void log(ActivityLog log);
	
	public List<ActivityLog> getActivities(int type, Date start, Date end);
}
