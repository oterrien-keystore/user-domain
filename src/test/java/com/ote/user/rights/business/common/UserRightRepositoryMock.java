package com.ote.user.rights.business.common;

import com.ote.common.OptionalConsumer;
import com.ote.user.rights.api.Path;
import com.ote.user.rights.api.Perimeter;
import com.ote.user.rights.api.Privilege;
import com.ote.user.rights.spi.IRightCheckerRepository;
import lombok.Getter;
import org.junit.jupiter.api.Assumptions;

import java.util.*;

public class UserRightRepositoryMock implements IRightCheckerRepository {

    @Getter
    private List<UserRightTest> userRights = new ArrayList<>();

    @Getter
    private List<Privilege> privileges = new ArrayList<>();

    @Override
    public boolean isUserDefined(String user) {
        Assumptions.assumeTrue(userRights != null);
        return userRights.stream().
                anyMatch(p -> Objects.equals(p.getUser(), user));
    }

    @Override
    public boolean isApplicationDefined(String application) {
        Assumptions.assumeTrue(userRights != null);
        return userRights.stream().
                anyMatch(p -> Objects.equals(p.getApplication(), application));
    }

    @Override
    public boolean isRoleDefined(String user, String application) {
        Assumptions.assumeTrue(userRights != null);
        return userRights.stream().
                filter(p -> Objects.equals(p.getUser(), user)).
                anyMatch(p -> Objects.equals(p.getApplication(), application));
    }

    @Override
    public List<Perimeter> getPerimeters(String user, String application) {
        Assumptions.assumeTrue(userRights != null);
        Optional<UserRightTest> userRights = this.userRights.stream().
                filter(p -> Objects.equals(p.getUser(), user)).
                filter(p -> Objects.equals(p.getApplication(), application)).
                findAny();
        return userRights.map(UserRightTest::getPerimeters).orElse(Collections.emptyList());
    }

    @Override
    public boolean isPerimeterDefined(Path perimeterPath) {
        Assumptions.assumeTrue(userRights != null);
        return userRights.stream().
                flatMap(p -> p.getPerimeters().stream()).
                anyMatch(p -> p.getCode().equalsIgnoreCase(perimeterPath.toString()));
    }

    @Override
    public boolean isPrivilegeDefined(String privilege) {
        Assumptions.assumeTrue(userRights != null);
        return privileges.stream().anyMatch(p -> p.getCode().equalsIgnoreCase(privilege));
    }

    @Override
    public Privilege getPrivilegeHierarchy(String privilege) {
        Assumptions.assumeTrue(privileges != null);
        Assumptions.assumeFalse(privileges.isEmpty());
        return privileges.stream().
                filter(p -> p.getCode().equalsIgnoreCase(privilege)).
                findAny().
                orElse(null);
    }

    public void clean() {
        userRights.clear();
        privileges.clear();
    }

    public void addPrivileges(Privilege... privileges) {
        this.privileges.addAll(Arrays.asList(privileges));
    }

    public void addUserRights(UserRightTest... userRights) {
        this.userRights.addAll(Arrays.asList(userRights));
    }

/*    @Override
    public void put(String user, String application, Perimeter perimeter) {

        UserRightTest userRight = userRights.stream().
                filter(p -> p.getUser().equalsIgnoreCase(user)).
                filter(p -> p.getApplication().equalsIgnoreCase(application)).
                findAny().get();

        OptionalConsumer.of(userRight.getPerimeters().stream().
                filter(p -> p.getCode().equalsIgnoreCase(perimeter.getCode())).
                findAny()).
                ifPresent(p -> p.getPrivileges().addAll(p.getPrivileges())).
                ifNotPresent(() -> {
                    userRight.getPerimeters().add(perimeter);
                    this.addUserRights(userRight);
                });
    }*/
}
