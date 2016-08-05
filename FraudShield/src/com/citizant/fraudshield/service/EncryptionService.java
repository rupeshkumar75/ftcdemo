package com.citizant.fraudshield.service;

public interface EncryptionService {
	
	public String encryptString(String plainText);
	
	public String encryptBinary(byte[] plainData);
	
	public String decryptString(String cipherText);
	
	public byte[] decryptBinary(String cipetText);
	
}
