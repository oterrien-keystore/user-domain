package com.ote.user.rights.api.exception;

import com.ote.user.rights.api.PerimeterPath;

public class RoleNotFoundException extends Exception {

    private static final String ROLE_APP_NOT_FOUND_MESSAGE = "Role not found for user '%s' and application '%s'";
    private static final String ROLE_APP_PATH_NOT_FOUND_MESSAGE = ROLE_APP_NOT_FOUND_MESSAGE + " and perimeters '%s'";

    public RoleNotFoundException(String user, String application) {
        super(String.format(ROLE_APP_NOT_FOUND_MESSAGE, user, application));
    }

    public RoleNotFoundException(String user, String application, PerimeterPath perimeterPath) {
        super(String.format(ROLE_APP_PATH_NOT_FOUND_MESSAGE, user, application, perimeterPath.toString()));
    }
}
