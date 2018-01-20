package com.ote.user.rights.business;

import com.ote.user.rights.api.IUserRightService;
import com.ote.user.rights.api.Path;
import com.ote.user.rights.api.Privilege;
import com.ote.user.rights.api.exception.*;
import com.ote.user.rights.spi.IUserRightRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class UserRightService implements IUserRightService {

    private final IUserRightRepository userRightRepository;

    @Override
    public boolean doesUserOwnPrivilegeForApplicationOnPerimeter(String user, String application, String perimeter, String privilege)
            throws UserRightServiceException {

        if (!userRightRepository.isUserDefined(user)) {
            throw new UserNotFoundException(user);
        }

        if (!userRightRepository.isApplicationDefined(application)) {
            throw new ApplicationNotFoundException(application);
        }

        if (!userRightRepository.isPerimeterDefined(perimeter)) {
            throw new PerimeterNotFoundException(application);
        }

        if (!userRightRepository.isPrivilegeDefined(privilege)) {
            throw new PrivilegeNotFoundException(application);
        }

        if (!userRightRepository.isRoleDefined(user, application)) {
            throw new RoleNotFoundException(user, application);
        }

        List<String> perimeterPaths = getPerimeterPaths(perimeter);

        Privilege privilegeHierarchy = userRightRepository.getPrivilegeHierarchy(privilege);

        return userRightRepository.getPerimeters(user, application).
                stream().
                filter(p -> perimeterPaths.stream().anyMatch(r -> r.equalsIgnoreCase(p.getCode()))). // get only Perimeter which belongs to perimeterPaths
                flatMap(p -> p.getPrivileges().stream()). // get their privileges
                anyMatch(p -> privilegeHierarchy.isDefined(p)); // return true only if given privilege is defined in privilege hierarchy
    }

    /**
     * If perimeter = "DEAL/GLE/SUB" then create thee level of perimeter
     * - DEAL
     * - DEAL/GLE
     * - DEAL/GLE/SUB
     */
    private List<String> getPerimeterPaths(String perimeter) {
        Path.Builder builder = new Path.Builder();
        return new Path.Parser(perimeter).get().
                stream().
                peek(p -> builder.then(p)).
                map(p -> builder.get().toString()).
                collect(Collectors.toList());
    }

    @RequiredArgsConstructor
    private class Role {
        private final String perimeter;
        private final String privilege;
    }
}
