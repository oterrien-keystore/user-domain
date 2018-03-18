package com.ote.user.rights.api.exception;

public class SecurityGroupNotFoundException extends Exception {

    private static final String NOT_FOUND_MESSAGE = "SecurityGroup '%s' not found";

    public SecurityGroupNotFoundException(String securityGroup) {
        super(String.format(NOT_FOUND_MESSAGE, securityGroup));
    }
}
