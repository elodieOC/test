package com.musers.microserviceusers.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * <p>Class for encoding passwords</p>
 */
public class Encryption {
    /**
     * <p>Method takes a string and encrypts it with MD5 function</p>
     * @param source password from registration user form or edit user form
     * @return encrypted password
     */
    public static String encrypt(String source) {
        String md5 = null;
        if(source.equals("")){
            return null;
        }
        else {
            try {
                MessageDigest mdEnc = MessageDigest.getInstance("MD5"); // Encryption algorithm
                mdEnc.update(source.getBytes(), 0, source.length());
                md5 = new BigInteger(1, mdEnc.digest()).toString(16); // Encrypted string
            } catch (Exception ex) {
                return null;
            }
            return md5;
        }
    }
}
