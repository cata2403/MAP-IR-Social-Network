package com.ubb.infrastructure_layer.utils;

import java.security.MessageDigest;

public class EncryptionAlgorithms {

    public static String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder encryptedPassword = new StringBuilder();
            for (byte b : hash) {
                encryptedPassword.append(String.format("%02x", b));
            }
            return encryptedPassword.toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
