package com.citizant.fraudshield.dao;

import com.citizant.fraudshield.domain.TsoUser;

public interface TsoUserDao {
	
	public TsoUser getUserByuserName(String username);
	public void saveUser(TsoUser user);
	public TsoUser getUserByEmail(String email);
	public TsoUser getUserByResetCode(String resetCode);
	public int getNumOfUsers();
	
}
