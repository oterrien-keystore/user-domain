package com.ote.user.rights.api.exception;

public class RightNotFoundException extends Exception {

    private static final String NOT_FOUND_MESSAGE = "Role not found for user '%s' and application '%s'";

    public RightNotFoundException(String user, String application) {
        super(String.format(NOT_FOUND_MESSAGE, user, application));
    }
}
