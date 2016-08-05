package com.citizant.fraudshield.security;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class EncryptionTest {

	public static void main(String[] args) {
		
		byte[] imageData = null;
		String encrypted = "";
		byte[] decryptData = null;
		try{
			File ifile = new File("c:\\dist\\dog.jpg");
			imageData = new byte[(int)ifile.length()];
			FileInputStream ins = new FileInputStream(ifile);
			ins.read(imageData);
			ins.close();
		}catch(Exception e1){
			
		}	
		encrypted = EncryptionUtil.encryptImage(imageData);
			
		decryptData = EncryptionUtil.decryptImage(encrypted);
		
		try{		
			System.out.println("Incrypted : " + encrypted);
			System.out.println("Incrypted String Length : " + encrypted.length());		
			File out3 = new File("c:\\dist\\dog-d.jpg");
			FileOutputStream ots = new FileOutputStream(out3);
			ots.write(decryptData);
			ots.flush();
			ots.close();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
		
		System.out.println(EncryptionUtil.encodePassword("pass01"));
	}

}
