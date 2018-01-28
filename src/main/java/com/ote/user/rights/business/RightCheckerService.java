package com.ote.user.rights.business;

import com.ote.user.rights.api.IRightCheckerService;
import com.ote.user.rights.api.Path;
import com.ote.user.rights.api.Perimeter;
import com.ote.user.rights.api.Privilege;
import com.ote.user.rights.api.exception.*;
import com.ote.user.rights.spi.IRightCheckerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class RightCheckerService implements IRightCheckerService {

    private final IRightCheckerRepository userRightRepository;

    @Override
    public boolean doesUserOwnPrivilegeForApplicationOnPerimeter(String user, String application, String perimeter, String privilege)
            throws UserRightServiceException {

        assertUserFound(user);
        assertApplicationFound(application);
        assertPerimeterFound(perimeter);
        assertPrivilegeFound(privilege);
        assertRoleFound(user, application);

        List<Perimeter> userRights = userRightRepository.getPerimeters(user, application);

        List<String> perimeterPaths = Path.extractAll(perimeter);

        Privilege privilegeHierarchy = userRightRepository.getPrivilegeHierarchy(privilege);

        return userRights.
                stream().
                filter(p -> perimeterPaths.stream().anyMatch(r -> r.equalsIgnoreCase(p.getCode()))). // get only Perimeter which belongs to perimeterPaths
                flatMap(p -> p.getPrivileges().stream()). // get their privileges
                anyMatch(p -> privilegeHierarchy.isDefined(p)); // return true only if given privilege is defined in privilege hierarchy
    }

    private void assertUserFound(String user) throws UserNotFoundException {
        if (!userRightRepository.isUserDefined(user)) {
            throw new UserNotFoundException(user);
        }
    }

    private void assertApplicationFound(String application) throws ApplicationNotFoundException {
        if (!userRightRepository.isApplicationDefined(application)) {
            throw new ApplicationNotFoundException(application);
        }
    }

    private void assertPerimeterFound(String perimeter) throws PerimeterNotFoundException {
        if (!userRightRepository.isPerimeterDefined(perimeter)) {
            throw new PerimeterNotFoundException(perimeter);
        }
    }

    private void assertPrivilegeFound(String privilege) throws PrivilegeNotFoundException {
        if (!userRightRepository.isPrivilegeDefined(privilege)) {
            throw new PrivilegeNotFoundException(privilege);
        }
    }

    private void assertRoleFound(String user, String application) throws RightNotFoundException {
        if (!userRightRepository.isRoleDefined(user, application)) {
            throw new RightNotFoundException(user, application);
        }
    }

    /*@Override
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
    }*/
}
