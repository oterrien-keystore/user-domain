package com.ote.user.rights.business;

import com.ote.user.rights.api.IUserRightService;
import com.ote.user.rights.api.Perimeter;
import com.ote.user.rights.api.PerimeterPath;
import com.ote.user.rights.api.exception.ApplicationNotFoundException;
import com.ote.user.rights.api.exception.RoleNotFoundException;
import com.ote.user.rights.api.exception.UserNotFoundException;
import com.ote.user.rights.spi.IUserRightRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class UserRightService implements IUserRightService {

    private final IUserRightRepository userRightRepository;

    @Override
    public boolean doesUserOwnPrivilegeForApplicationOnPerimeter(String user, String application, PerimeterPath perimeterPath, String privilege)
            throws UserNotFoundException, ApplicationNotFoundException, RoleNotFoundException {

        if (!userRightRepository.isUserDefined(user)) {
            throw new UserNotFoundException(user);
        }

        if (!userRightRepository.isApplicationDefined(application)) {
            throw new ApplicationNotFoundException(application);
        }

        if (!userRightRepository.isRoleDefined(user, application)) {
            throw new RoleNotFoundException(user, application);
        }

        List<Perimeter> perimeters = userRightRepository.getPerimeters(user, application);

        if (perimeters.stream().anyMatch(p -> p.getPrivileges().isEmpty())) {
            throw new RoleNotFoundException(user, application, perimeterPath);
        }

        return perimeters.stream().
                map(p -> getPrivilegesForPath(p, perimeterPath)).
                flatMap(Collection::stream).
                anyMatch(p -> Objects.equals(p, privilege));
    }

    private List<String> getPrivilegesForPath(Perimeter perimeter, PerimeterPath perimeterPath) {

        List<String> privileges = new ArrayList<>();

        final List<Perimeter> perimetersToSearch = new ArrayList<>();
        perimetersToSearch.add(perimeter);

        perimeterPath.forEach(pathElement ->
                searchPerimeterByCode(perimetersToSearch, pathElement).
                        ifPresent(p -> apply(p, privileges, perimetersToSearch)));

        return privileges;
    }

    private Optional<Perimeter> searchPerimeterByCode(List<Perimeter> perimetersToSearch, String code) {
        return searchPerimeterByPredicate(perimetersToSearch, perimeter -> Objects.equals(perimeter.getCode(), code));
    }

    private Optional<Perimeter> searchPerimeterByPredicate(List<Perimeter> perimetersToSearch, Predicate<Perimeter> predicate) {
        return perimetersToSearch.stream().filter(predicate).findAny();
    }

    private void apply(Perimeter perimeter, List<String> privileges, List<Perimeter> perimetersToSearch) {
        privileges.addAll(perimeter.getPrivileges());
        perimetersToSearch.clear();
        perimetersToSearch.addAll(perimeter.getPerimeters());
    }
}
