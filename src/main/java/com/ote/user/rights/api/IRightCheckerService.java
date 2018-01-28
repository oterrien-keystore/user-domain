package com.ote.user.rights.api;

import com.ote.user.rights.api.exception.UserRightServiceException;

public interface IRightCheckerService {

    boolean doesUserOwnPrivilegeForApplicationOnPerimeter(String user, String application, String perimeter, String privilege)
            throws UserRightServiceException;
}
