package com.ote.user.rights.api;

import com.ote.user.rights.api.exception.UserRightServiceException;

public interface IUserRightService {

    boolean doesUserOwnPrivilegeForApplicationOnPerimeter(String user, String application, String perimeter, String privilege)
            throws UserRightServiceException;

    void addRights(String user, String application, String perimeter, String privilege) throws UserRightServiceException;
}
