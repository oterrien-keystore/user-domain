package com.ote.user.rights.spi;

import com.ote.user.rights.api.Path;
import com.ote.user.rights.api.Perimeter;
import com.ote.user.rights.api.Privilege;
import com.ote.user.rights.api.exception.PrivilegeNotFoundException;

import java.util.List;

public interface IUserRightRepository {

    boolean isUserDefined(String user);

    boolean isApplicationDefined(String application);

    default boolean isPerimeterDefined(String perimeter) {
        return isPerimeterDefined(new Path.Parser(perimeter).get());
    }

    boolean isPerimeterDefined(Path perimeterPath);

    boolean isPrivilegeDefined(String privilege);

    boolean isRoleDefined(String user, String application);

    List<Perimeter> getPerimeters(String user, String application);

    Privilege getPrivilegeHierarchy(String privilege);

    void put(String user, String application, Perimeter perimeter);
}
