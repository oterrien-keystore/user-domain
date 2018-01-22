package com.ote.user.rights.api;

import com.ote.user.rights.api.exception.UserRightServiceException;

import java.util.List;

public interface IUserRightService {

    List<Perimeter> getRights(String user, String application) throws UserRightServiceException;

    boolean doesUserOwnPrivilegeForApplicationOnPerimeter(String user, String application, String perimeter, String privilege)
            throws UserRightServiceException;

    void addRights(String user, String application, String perimeter, String privilege) throws UserRightServiceException;

    void removeRights(String user, String application, String perimeter, String privilege) throws UserRightServiceException;
}
