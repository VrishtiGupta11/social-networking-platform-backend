package com.example.demo.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.stereotype.Service;

@Service
public class UserService {
	public String passwordToHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// PBKDF2 in Java
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

		byte[] hash = factory.generateSecret(spec).getEncoded();
		
		String stringhash = new String(hash);  
		
		return stringhash;
	}
}
