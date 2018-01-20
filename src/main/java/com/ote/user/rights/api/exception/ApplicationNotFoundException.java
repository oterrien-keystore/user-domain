package com.ote.user.rights.api.exception;

public class ApplicationNotFoundException extends UserRightServiceException {

    private static final String APP_NOT_FOUND_MESSAGE = "Application '%s' not found";

    public ApplicationNotFoundException(String application) {
        super(String.format(APP_NOT_FOUND_MESSAGE, application));
    }
}
