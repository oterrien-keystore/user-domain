package com.ote.user.rights.spi;

import com.ote.user.rights.api.Perimeter;

import java.util.List;

public interface IUserRightRepository {

    boolean isUserDefined(String user);

    boolean isApplicationDefined(String application);

    boolean isRoleDefined(String user, String application);

    List<Perimeter> getPerimeters(String user, String application);
}
