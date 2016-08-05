package com.citizant.fraudshield.service;

import java.util.Date;

import com.citizant.fraudshield.common.ActivityType;

public interface ActivityLogService {
	public void log(String username, ActivityType activityType, String activity, Long target);
	public int getNumOfActivities(ActivityType activityTye, Date start, Date end);
}
