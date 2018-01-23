package com.ote.user.rights.business;

import com.ote.user.rights.api.IUserRightService;
import com.ote.user.rights.api.Path;
import com.ote.user.rights.api.Perimeter;
import com.ote.user.rights.api.Privilege;
import com.ote.user.rights.api.exception.*;
import com.ote.user.rights.spi.IUserRightRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
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

        if (!userRightRepository.isRoleDefined(user, application)) {
            throw new RoleNotFoundException(user, application);
        }

        if (!userRightRepository.isUserDefined(user)) {
            throw new UserNotFoundException(user);
        }

        if (!userRightRepository.isApplicationDefined(application)) {
            throw new ApplicationNotFoundException(application);
        }

        if (!userRightRepository.isPerimeterDefined(perimeter)) {
            throw new PerimeterNotFoundException(perimeter);
        }

        if (!userRightRepository.isPrivilegeDefined(privilege)) {
            throw new PrivilegeNotFoundException(privilege);
        }

        if (!userRightRepository.isRoleDefined(user, application)) {
            throw new RoleNotFoundException(user, application);
        }

        List<Perimeter> userRights = userRightRepository.getPerimeters(user, application);

        List<String> perimeterPaths = getPerimeterPaths(perimeter);

        Privilege privilegeHierarchy = userRightRepository.getPrivilegeHierarchy(privilege);

        return userRights.
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

    @Override
    public void addRights(String user, String application, String perimeter, String privilege) throws UserRightServiceException {

        if (!userRightRepository.isUserDefined(user)) {
            throw new UserNotFoundException(user);
        }

        if (!userRightRepository.isApplicationDefined(application)) {
            throw new ApplicationNotFoundException(application);
        }

        if (!userRightRepository.isPerimeterDefined(perimeter)) {
            throw new PerimeterNotFoundException(perimeter);
        }

        if (!userRightRepository.isPrivilegeDefined(privilege)) {
            throw new PrivilegeNotFoundException(application);
        }

        List<Perimeter> rights = userRightRepository.getPerimeters(user, application);

        if (rights.stream().noneMatch(p -> p.getCode().equalsIgnoreCase(perimeter))) {
            Perimeter perimeterToAdd = new Perimeter(perimeter);
            perimeterToAdd.getPrivileges().add(privilege);
            userRightRepository.put(user, application, perimeterToAdd);
        } else {
            Perimeter perimeterToAdd = rights.stream().filter(p -> p.getCode().equalsIgnoreCase(perimeter)).findAny().get();
            Privilege privilegeHierarchy = userRightRepository.getPrivilegeHierarchy(privilege);
            Set<Privilege> privilegeToAddHierarchy = perimeterToAdd.getPrivileges().stream().
                    map(p -> userRightRepository.getPrivilegeHierarchy(p)).
                    collect(Collectors.toSet());

            if (privilegeToAddHierarchy.stream().noneMatch(p -> p.isSameHierarchy(privilegeHierarchy))) {
                // privilege to add is not linked to any existing hierarchy
                perimeterToAdd.getPrivileges().add(privilege);
                userRightRepository.put(user, application, perimeterToAdd);
            } else {
                //get highest (else do nothing while the existing privilege is already the highest)
                privilegeToAddHierarchy.
                        stream().
                        filter(p -> p.isDefined(privilege)).
                        findAny().
                        ifPresent(priv -> {
                            perimeterToAdd.getPrivileges().removeIf(p1 -> p1.equalsIgnoreCase(priv.getCode()));
                            perimeterToAdd.getPrivileges().add(privilege);
                            userRightRepository.put(user, application, perimeterToAdd);
                        });
            }
        }
    }
}
