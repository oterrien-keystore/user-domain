package com.ote.user.rights.api;

import com.ote.user.rights.api.exception.ApplicationNotFoundException;
import com.ote.user.rights.api.exception.RoleNotFoundException;
import com.ote.user.rights.api.exception.UserNotFoundException;

public interface IUserRightService {

    boolean doesUserOwnPrivilegeForApplicationOnPerimeter(String user, String application, PerimeterPath perimeterPath, String privilege)
            throws UserNotFoundException, ApplicationNotFoundException, RoleNotFoundException;
}
