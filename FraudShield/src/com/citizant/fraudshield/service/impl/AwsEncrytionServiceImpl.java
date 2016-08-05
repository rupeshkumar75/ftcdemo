package com.citizant.fraudshield.service.impl;

import java.nio.ByteBuffer;
import org.springframework.security.crypto.codec.Base64;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.citizant.fraudshield.common.SystemConfig;
import com.citizant.fraudshield.service.EncryptionService;

/*
 * This implementation using AWS Key Management Service for
 * data encryption
 */

public class AwsEncrytionServiceImpl implements EncryptionService {
	
	private final AWSKMSClient kms;
	
	public static void main(String[] args) {
		
		SystemConfig.DATA_KEY_ID = "arn:aws:kms:us-east-1:434141935580:key/365aca6a-a109-446f-a62d-c77e2cfb887b";
		SystemConfig.KMS_END_POINT = "https://kms.us-east-1.amazonaws.com";
		AwsEncrytionServiceImpl imp = new AwsEncrytionServiceImpl();
		String cipherText = imp.encryptString("Hello World!");
		System.out.println(cipherText);
		System.out.println(imp.decryptString(cipherText));
	}
	
	public AwsEncrytionServiceImpl() {
		 final AWSCredentials creds = new ProfileCredentialsProvider().getCredentials();
	     kms = new AWSKMSClient(creds);	
	     kms.setEndpoint(SystemConfig.KMS_END_POINT);
	}
	
	public String encryptString(String plainText) {
		String output = null;
		try{
			ByteBuffer pt = ByteBuffer.wrap(plainText.getBytes());
			EncryptRequest req = new EncryptRequest().withKeyId(SystemConfig.DATA_KEY_ID).withPlaintext(pt);
			ByteBuffer ciphertext = kms.encrypt(req).getCiphertextBlob();		
			output = new String(Base64.encode(ciphertext.array()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
	
	public String encryptBinary(byte[] plainData) {
		String output = null;
		try{
			ByteBuffer pt = ByteBuffer.wrap(plainData);
			EncryptRequest req = new EncryptRequest().withKeyId(SystemConfig.DATA_KEY_ID).withPlaintext(pt);
			ByteBuffer ciphertext = kms.encrypt(req).getCiphertextBlob();		
			output = new String(Base64.encode(ciphertext.array()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
	
	public String decryptString(String cipherText) {
		String output = null;
		try{
			byte[] ds = Base64.decode(cipherText.getBytes());		
			ByteBuffer ciphertextBlob = ByteBuffer.wrap(ds);
			DecryptRequest req = new DecryptRequest().withCiphertextBlob(ciphertextBlob);
			ByteBuffer plainText = kms.decrypt(req).getPlaintext();
			output = new String(plainText.array());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
	
	public byte[] decryptBinary(String ciperText) {
		byte[] output = null;
		try {
			byte[] ds = Base64.decode(ciperText.getBytes());
			ByteBuffer ciphertextBlob = ByteBuffer.wrap(ds);
			DecryptRequest req = new DecryptRequest().withCiphertextBlob(ciphertextBlob);
			ByteBuffer plainText = kms.decrypt(req).getPlaintext();
			output = plainText.array();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
}
