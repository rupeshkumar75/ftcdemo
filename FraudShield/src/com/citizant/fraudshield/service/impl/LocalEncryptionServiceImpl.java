package com.citizant.fraudshield.service.impl;

import java.security.GeneralSecurityException;



import javax.crypto.NoSuchPaddingException;

import com.citizant.fraudshield.security.EncryptionUtil;
import com.citizant.fraudshield.service.EncryptionService;

public class LocalEncryptionServiceImpl implements EncryptionService {
	
	public String encryptString(String plainText) {
		try {
			return EncryptionUtil.encryptString(plainText);
		} catch (GeneralSecurityException e) {
	
			e.printStackTrace();
		}
		return null;
	}
	
	public String encryptBinary(byte[] plainData) {
		return EncryptionUtil.encryptImage(plainData);
	}
	
	public String decryptString(String cipherText) {
		try {
			return EncryptionUtil.decryptString(cipherText);
		} catch (NoSuchPaddingException e) {
			
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
		
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] decryptBinary(String cipetText) {
		return EncryptionUtil.decryptImage(cipetText);
	}
}
