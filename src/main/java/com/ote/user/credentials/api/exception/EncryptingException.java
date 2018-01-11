package com.ote.user.credentials.api.exception;

public class EncryptingException extends Exception {

    public EncryptingException(Exception cause) {
        super("Error while encrypting data", cause);
    }
}
