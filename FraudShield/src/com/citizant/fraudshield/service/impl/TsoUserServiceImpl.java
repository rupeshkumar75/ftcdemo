package com.citizant.fraudshield.service.impl;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.citizant.fraudshield.common.SystemConfig;
import com.citizant.fraudshield.dao.TsoUserDao;
import com.citizant.fraudshield.domain.TsoUser;
import com.citizant.fraudshield.security.EncryptionUtil;
import com.citizant.fraudshield.service.EmailService;
import com.citizant.fraudshield.service.TsoUserService;

public class TsoUserServiceImpl implements TsoUserService {
	
	@Autowired
	TsoUserDao tsoUserDao;
	
	@Autowired
	EmailService emailService;

	@Override
	public TsoUser getTsoUserByIdPassword(String username, String password) {
		
		TsoUser user = tsoUserDao.getUserByuserName(username);
		if(user != null){
			if(EncryptionUtil.passwordMatch(password, user.getPassword())){
				return user;
			}
		}
		return null;
	}

	@Override
	public TsoUser createTsoUser (
			String firstName,
			String lastName,
			String contactPhone,
			String contactEmail,
			String username, 
			String password, 
			String role){
		TsoUser user = tsoUserDao.getUserByuserName(username);
		if(user == null){
			user = new TsoUser();
			user.setActive(true);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setContactPhone(contactPhone);
			user.setContactEmail(contactEmail);
			user.setUsername(username);
			user.setUserRole(role);
			user.setPassword(EncryptionUtil.encodePassword(password));
			user.setTsoUserId(null);
			tsoUserDao.saveUser(user);
		}
		
		return user;
	}

	@Override
	public TsoUser getTsoUserByUsername(String username) {
		return tsoUserDao.getUserByuserName(username);
	}
	
	@Override
	public TsoUser getTsoUserByEmail(String email) {
		return tsoUserDao.getUserByEmail(email);
	}
	
	@Override
	public TsoUser getTsoUserByResetCode(String resetCode) {
		return tsoUserDao.getUserByResetCode(resetCode);
	}
	
	
	@Override
	public void lockUserAccount(String username) {
		TsoUser user =  getTsoUserByUsername(username);
		if( user!= null) {
			user.setActive(false);
			tsoUserDao.saveUser(user);
		}
	}
	
	@Override
	public void unLockUserAccount(String username) {
		TsoUser user =  getTsoUserByUsername(username);
		if( user!= null) {
			user.setActive(true);
			tsoUserDao.saveUser(user);
		}		
	}
	
	public boolean setUserPassword(TsoUser user, String password) {
		boolean res = true;
		try{
			user.setResetCode(null);;
			user.setResetDate(null);
			user.setPassword(EncryptionUtil.encodePassword(password));
			tsoUserDao.saveUser(user);
			res = true;
		} catch (Exception e) {
			res = false;
		}
		if(res) {
			emailService.sendMail(user.getContactEmail(), SystemConfig.CONFIRM_EMAIL_SUBJECT, SystemConfig.CONFIRM_EMAIL_CONTENT);
		}
		return res;
	}
	
	/*
	 * 
	 * Set the password reset process. generate a code 
	 * and sent a link with email.
	 */
	@Override
	public boolean createPasswordRestCode(TsoUser user) {
		String code = EncryptionUtil.getRandomPasswordResetCode();
		user.setResetCode(code);
		user.setResetDate(new Date());
		tsoUserDao.saveUser(user);
		
		String contents = SystemConfig.RESET_EMAIL_CONTENT;
		
		contents = contents.replace("RESETCODE", code);
		
		Date d = new Date();
		
		d.setTime(d.getTime() + 1 * 1000 * 60 * 60 * 24);
		
		contents = contents.replace("EXPIRETIME", d.toString());
		
		return emailService.sendMail(user.getContactEmail(), SystemConfig.RESET_EMAIL_SUBJECT, contents);
	}
	
	
	public int getNumOfUsers() {
		return tsoUserDao.getNumOfUsers();
	}
}
