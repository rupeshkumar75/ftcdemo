package com.citizant.fraudshield.service;

public interface EmailService {
	public boolean sendMail(String address, String title, String contents);
}
