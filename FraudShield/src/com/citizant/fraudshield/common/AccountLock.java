package com.citizant.fraudshield.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/*
 * This is a manager that controls user 
 * login attempts. 
 * 1. If there are 3 failed attempts with the same username in 1 minute, lock the account.
 * 2. If there are 5 failed attempts from the sampe IP, but not the same username, deny further attempts.
 * 
 */

public class AccountLock {
	
	private List<LoginAttempt> attempts = null;
	private String attackUsername = null;
	
	public AccountLock() {
		attempts = new ArrayList<LoginAttempt>();
	}
	
	public void addAttempt(String username, String ip, Date time) {
		LoginAttempt att = new LoginAttempt();
		att.setUsername(username);
		att.setIp(ip);
		att.setTime(time);
		attempts.add(att);
	}
	
	/*
	 * Check if there is a suspect attack on the server
	 * 
	 */
	public boolean isUnderAttack() {
		attackUsername = null;
		HashMap<String, Integer> userAtts = new HashMap<String, Integer>();
		Date now = new Date();
		int count = 0;
		for(LoginAttempt att : attempts) {
			if(now.getTime() - att.getTime().getTime() < 60000) {
				count++;
				
				Integer uc = userAtts.get(att.getUsername());
				if(uc == null) {
					uc = new Integer(0);
				}
				Integer nuc = new Integer(uc.intValue() + 1);
				userAtts.put(att.getUsername(), nuc);			
			}
		}
		Iterator<String> its = userAtts.keySet().iterator();
		while(its.hasNext()){
			String user = its.next();
			Integer in = userAtts.get(user);
			if(in.intValue()>=3) {
				attackUsername = user;
				break;
			}
		}
		
		
		if(count >=5 ) {
			return true;
		}
		return false;
	}
	
	
	public String getAttackUsername() {
		return attackUsername;
	}


	public class LoginAttempt {
		private String ip;
		private String username;
		private Date time;
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public Date getTime() {
			return time;
		}
		public void setTime(Date time) {
			this.time = time;
		}
	}
}
