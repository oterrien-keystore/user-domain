package com.ote.user.rights.api.exception;

public class ApplicationNotFoundException extends Exception {

    private static final String NOT_FOUND_MESSAGE = "Application '%s' not found";

    public ApplicationNotFoundException(String application) {
        super(String.format(NOT_FOUND_MESSAGE, application));
    }
}
