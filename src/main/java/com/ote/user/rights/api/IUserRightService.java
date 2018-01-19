package com.ote.user.rights.api;

import com.ote.user.rights.api.exception.*;

public interface IUserRightService {

    boolean doesUserOwnPrivilegeForApplicationOnPerimeter(String user, String application, String perimeter, String privilege)
            throws UserNotFoundException, ApplicationNotFoundException, RoleNotFoundException, PerimeterNotFoundException, PrivilegeNotFoundException;
}
