package com.dms.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Encryptor {

	final static String key = "C+YaVwM3UlVvjhOlQzis2w==";

	public static String encrypt(String value) {
		byte[] raw;
		String encryptedString;
		SecretKeySpec skeySpec;
		byte[] encryptText = value.getBytes();
		Cipher cipher;
		try {
			raw = Base64.decodeBase64(key);
			skeySpec = new SecretKeySpec(raw, "AES");
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			encryptedString = Base64.encodeBase64String(cipher.doFinal(encryptText));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return encryptedString;
	}

	public static String decrypt(String encrypted) {
		Cipher cipher;
		String encryptedString;
		byte[] encryptText = null;
		byte[] raw;
		SecretKeySpec skeySpec;
		try {
			encrypted = encrypted.replaceAll("\\s+", "+");
			raw = Base64.decodeBase64(key);
			skeySpec = new SecretKeySpec(raw, "AES");
			encryptText = Base64.decodeBase64(encrypted);
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			encryptedString = new String(cipher.doFinal(encryptText));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return encryptedString;
	}

	public static String generateSecretKey() throws Exception {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128);
		return Base64.encodeBase64String(keyGen.generateKey().getEncoded());

	}

	public static void main(String[] args) throws Exception {
		System.out.println(generateSecretKey());
		// String cc = "Hell0WOrld!o$";
		// System.out.println(encrypt(cc));
		// System.out.println(decrypt("mDLfK2ZYkSHggL1OPHD2rWoLK403aKdelERpTuQFXyQ="));
	}
}