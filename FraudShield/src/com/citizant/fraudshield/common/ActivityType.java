package com.citizant.fraudshield.common;

public enum ActivityType {
	
	CREATE_TSO_ACCOUNT(101, "Create TSO account"),
	LOCK_TSO_ACCOUNT(102, "Lock TSO account"),
	UNLOCK_TSO_ACCOUNT(103, "Unlock TSO account"),
	
	CREATE_REGISTRATION(201, "Create new Registration"),
	INVALID_REGISTRATION(202, "Invalidate Registration"),
	VIEW_REGISTRATION(203, "View Registration Details"),
	PRINT_ID_CARD(204, "Print Registration ID"),
	SEARCH_EXISTING_CUSTOMER(205, "Search existing Registration"),
	EXPIRE_REGISTRATION(206, "Expire registration"),
	
	USER_LOGIN(301, "User login event"),
	USER_LOGIN_FAILED(302, "User login failed"),
	USER_REQUEST_PASSWORD_RESET(303, "User request password reset"),
	USER_PASSWORD_RESET(304, "User password reset"),
	
	REPORT_CUSTOMER(401, "Generate customer report"),
	REPORT_STATUS(402, "Generate status report");
	
	private int key;
	private String desc;
	
	private ActivityType(int key, String desc) {
		this.key = key;
		this.desc = desc;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
