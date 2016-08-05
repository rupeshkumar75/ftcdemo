package com.citizant.fraudshield.service;

import com.citizant.fraudshield.domain.TsoUser;

public interface TsoUserService {
	
	public TsoUser getTsoUserByIdPassword(String username, String password);
	
	public TsoUser createTsoUser (
			String firstName,
			String lastName,
			String contactPhone,
			String contactEmail,
			String username, 
			String password, 
			String role);
	
	public TsoUser getTsoUserByUsername(String username);
	
	public TsoUser getTsoUserByEmail(String email);
	
	public TsoUser getTsoUserByResetCode(String resetCode);
	
	public boolean createPasswordRestCode(TsoUser user);
	
	public boolean setUserPassword(TsoUser user, String password);

	public void lockUserAccount(String username);
	
	public void unLockUserAccount(String username);
	
	public int getNumOfUsers();
}
