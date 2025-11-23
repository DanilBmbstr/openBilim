package com.openBilim.Users.Authorization;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Hash {
    public static String hashString(String input, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashBytes = digest.digest(input.getBytes());
            
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateSalt(){
        String salt = "";
         String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJCLMNOPQRTSTUCWXYZ!@#$%^&*()_+=':;,.-/|*<>?^~`[]{}1234567890";
         SecureRandom c = new SecureRandom();
        for(int i = 0; i < 25; i++){
            
            int next = c.nextInt();
            next = Math.abs(next);
            salt+= alphabet.charAt(next % (alphabet.length()) );
        }
        
        return salt;
        
    }
    
   
}