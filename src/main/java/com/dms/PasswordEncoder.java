package com.dms;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {

	public static void main(String[] args) {
		System.out.println(encodePassword("Welcome@DMS2020"));

	}

	public static String encodePassword(String password) {
		// System.out.println("######## encoding: " + password);
		return new BCryptPasswordEncoder().encode(password);
	}

}
