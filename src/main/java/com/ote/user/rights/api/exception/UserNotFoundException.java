package com.ote.user.rights.api.exception;

public class UserNotFoundException extends UserRightServiceException {

    private static final String USR_NOT_FOUND_MESSAGE = "User '%s' not found";

    public UserNotFoundException(String user) {
        super(String.format(USR_NOT_FOUND_MESSAGE, user));
    }
}
