package com.ote.user.rights.api.exception;

public class UserNotFoundException extends Exception {

    private static final String NOT_FOUND_MESSAGE = "User '%s' not found";

    public UserNotFoundException(String user) {
        super(String.format(NOT_FOUND_MESSAGE, user));
    }
}
