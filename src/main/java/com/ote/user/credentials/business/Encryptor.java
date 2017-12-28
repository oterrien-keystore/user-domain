package com.ote.user.credentials.business;

import lombok.Getter;

import java.security.MessageDigest;

final class Encryptor {

    @Getter
    private final static Encryptor Instance = new Encryptor();

    private static final String SALT = "125d6d03b32c84d492747f79cf0bf6e179d287f341384eb5d6d3197525ad6be8e6df0116032935698f99a09e265073d1d6c32c274591bf1d0a20ad67cba921bc";

    private final MessageDigest messageDigest;

    private Encryptor() {
        try {
            messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(SALT.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException("Unable to instantiate Encryptor", e);
        }
    }

    public String encrypt(String value) {
        try {
            byte[] bytes;
            synchronized (messageDigest) {
                bytes = messageDigest.digest(value.getBytes("UTF-8"));
            }
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Unable to encrypt", e);
        }
    }
}
