package com.citizant.fraudshield.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.codec.Base64;

public class EncryptionUtil {

	private static SecretKeySpec skeySpec;

	static {

		try {
			ClassPathResource res = new ClassPathResource("testkeystore.key");
			// System.out.println(res.getURI());
			File file = new File(res.getURI());

			FileInputStream input = new FileInputStream(file);

			byte[] in = new byte[(int) file.length()];

			input.read(in);

			skeySpec = new SecretKeySpec(in, "AES");
			input.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	public static String encodePassword(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		return hashedPassword;
	}

	public static boolean passwordMatch(String password, String encrypedPass) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(password, encrypedPass);
	}

	public static byte[] encrypt(String input) throws GeneralSecurityException,
			NoSuchPaddingException {

		Cipher cipher = Cipher.getInstance("AES");

		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

		return cipher.doFinal(input.getBytes());

	}

	public static byte[] encrypt(byte[] input)

	throws GeneralSecurityException, NoSuchPaddingException {

		Cipher cipher = Cipher.getInstance("AES");

		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

		return cipher.doFinal(input);

	}

	public static String decrypt(String input) throws GeneralSecurityException,
			NoSuchPaddingException {

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		return new String(cipher.doFinal(input.getBytes()));

	}

	public static String decrypt(byte[] input) throws GeneralSecurityException,
			NoSuchPaddingException {

		Cipher cipher = Cipher.getInstance("AES");

		cipher.init(Cipher.DECRYPT_MODE, skeySpec);

		return new String(cipher.doFinal(input));

	}

	public static byte[] decryptBinary(byte[] input)
			throws GeneralSecurityException, NoSuchPaddingException {

		Cipher cipher = Cipher.getInstance("AES");

		cipher.init(Cipher.DECRYPT_MODE, skeySpec);

		return cipher.doFinal(input);

	}

	// Decrypt binary data from Base54 encoded
	public static byte[] decryptImage(String encryptData) {
		try {
			byte[] ds = Base64.decode(encryptData.getBytes());
			byte[] out = EncryptionUtil.decryptBinary(ds);
			return out;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Encrypt binary data and Base64 encode as String
	public static String encryptImage(byte[] imageData) {
		byte[] encryptData = null;
		String output = "";
		try {
			encryptData = EncryptionUtil.encrypt(imageData);
			output = new String(Base64.encode(encryptData));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	public static String encryptString(String input)
			throws GeneralSecurityException, NoSuchPaddingException {

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] ens = cipher.doFinal(input.getBytes());
		String output = new String(Base64.encode(ens));
		return output;
	}

	public static String decryptString(String input)
			throws GeneralSecurityException, NoSuchPaddingException {

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] ssd = Base64.decode(input.getBytes());
		return new String(cipher.doFinal(ssd));
	}
	
	
	public static String getRandomPasswordResetCode() {
		 SecureRandom random = new SecureRandom();		
		 return new BigInteger(130, random).toString(32);
	}

	public static void main(String[] args) {
		String in = "123-45-6789";

		try {
			String file = "c:/temp/docimage.PNG";
			Path p = FileSystems.getDefault()
					.getPath("c:/temp", "docimage.PNG");
			byte[] fileData = Files.readAllBytes(p);

			String eimage = encryptImage(fileData);
			System.out.println("Out : " + eimage.length());
			// String code = encryptString(in);
			// String out = decryptString(code);
			// System.out.println("In : " + in);
			// System.out.println("Code: " + code);
			// System.out.println("Out : " + out);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
