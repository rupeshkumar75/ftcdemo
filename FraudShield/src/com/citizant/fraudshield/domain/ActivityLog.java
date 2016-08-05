package com.citizant.fraudshield.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="activity_log")
public class ActivityLog extends DomainBase implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5186258904670061848L;
	
	@Id
	@GeneratedValue
	@Column(name="ACTIVITY_LOG_ID")
	private Long activityLogId;
	
	@Column(name="ACTIVITY_BY")
	private String activityBy;
	
	@Column(name="ACTIVITY_DATE")
	private Date activityDate;
	
	@Column(name="ACTIVITY")
	private String activity;
	
	@Column(name="ACTIVITY_TYPE")
	private Integer activityType;
	
	@Column(name="TARGET")
	private Long activityTarget;

	@Override
	public Long getId() {		
		return this.activityLogId;
	}

	public Long getActivityLogId() {
		return activityLogId;
	}

	public void setActivityLogId(Long activityLogId) {
		this.activityLogId = activityLogId;
	}

	public String getActivityBy() {
		return activityBy;
	}

	public void setActivityBy(String activityBy) {
		this.activityBy = activityBy;
	}

	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public Integer getActivityType() {
		return activityType;
	}

	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}

	public Long getActivityTarget() {
		return activityTarget;
	}

	public void setActivityTarget(Long activityTarget) {
		this.activityTarget = activityTarget;
	}
	
	
}
