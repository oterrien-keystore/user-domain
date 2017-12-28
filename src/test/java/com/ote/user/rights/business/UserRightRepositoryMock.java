package com.ote.user.rights.business;

import com.ote.user.rights.api.Perimeter;
import com.ote.user.rights.spi.IUserRightRepository;
import lombok.Getter;

import java.util.*;

public class UserRightRepositoryMock implements IUserRightRepository {

    @Getter
    private final List<UserRight> userRightList = new ArrayList<>();

    @Override
    public boolean isUserDefined(String user) {
        return userRightList.stream().anyMatch(p -> Objects.equals(p.getUser(), user));
    }

    @Override
    public boolean isApplicationDefined(String application) {
        return userRightList.stream().
                anyMatch(p -> Objects.equals(p.getApplication(), application));
    }

    @Override
    public boolean isRoleDefined(String user, String application) {
        return userRightList.stream().
                filter(p -> Objects.equals(p.getUser(), user)).
                anyMatch(p -> Objects.equals(p.getApplication(), application));
    }

    @Override
    public List<Perimeter> getPerimeters(String user, String application) {
        Optional<UserRight> userRights = userRightList.stream().
                filter(p -> Objects.equals(p.getUser(), user)).
                filter(p -> Objects.equals(p.getApplication(), application)).
                findAny();
        return userRights.map(UserRight::getPerimeters).orElse(Collections.emptyList());
    }
}
