package com.ote.user.rights.api.exception;

public class PrivilegeNotFoundException extends Exception {

    private static final String NOT_FOUND_MESSAGE = "Privilege '%s' not found";

    public PrivilegeNotFoundException(String application) {
        super(String.format(NOT_FOUND_MESSAGE, application));
    }
}
