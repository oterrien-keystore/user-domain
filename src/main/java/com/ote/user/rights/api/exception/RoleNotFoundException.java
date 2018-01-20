package com.ote.user.rights.api.exception;

public class RoleNotFoundException extends UserRightServiceException {

    private static final String ROLE_APP_NOT_FOUND_MESSAGE = "Role not found for user '%s' and application '%s'";

    public RoleNotFoundException(String user, String application) {
        super(String.format(ROLE_APP_NOT_FOUND_MESSAGE, user, application));
    }
}
