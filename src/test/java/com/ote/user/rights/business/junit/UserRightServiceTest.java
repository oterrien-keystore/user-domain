package com.ote.user.rights.business.junit;

import com.ote.user.rights.api.IUserRightService;
import com.ote.user.rights.api.Perimeter;
import com.ote.user.rights.api.Privilege;
import com.ote.user.rights.api.UserRightServiceProvider;
import com.ote.user.rights.api.exception.*;
import com.ote.user.rights.business.common.UserRightRepositoryMock;
import com.ote.user.rights.business.common.UserRightTest;
import mockito.MockitoExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

@ExtendWith(MockitoExtension.class)
public class UserRightServiceTest {

    @Spy
    private UserRightRepositoryMock userRightRepository = new UserRightRepositoryMock();
    private IUserRightService userRightService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.userRightService = UserRightServiceProvider.getInstance().getFactory().createService(userRightRepository);

        Privilege admin = new Privilege("ADMIN");
        Privilege write = new Privilege("WRITE", admin);
        Privilege read = new Privilege("READ", write);
        this.userRightRepository.addPrivileges(admin, write, read);
    }

    @AfterEach
    public void clean() {
        userRightRepository.clean();
    }

    @Test
    @DisplayName("Searching unexisting user should raise UserNotFoundException")
    public void testUserNotFoundException() {

        UserRightTest userRight = new UserRightTest("steve.jobs", "APPLE");
        Perimeter perimeter = new Perimeter("ACCOUNTING");
        perimeter.getPrivileges().add("READ");
        userRight.getPerimeters().add(perimeter);
        userRightRepository.addUserRights(userRight);

        Assertions.assertThatThrownBy(() -> userRightService.doesUserOwnPrivilegeForApplicationOnPerimeter("###########", "APPLE", "ACCOUNTING", "READ")).
                isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("Searching unexisting application should raise ApplicationNotFoundException")
    public void testApplicationNotFoundException() {

        UserRightTest userRight = new UserRightTest("steve.jobs", "APPLE");
        Perimeter perimeter = new Perimeter("ACCOUNTING");
        perimeter.getPrivileges().add("READ");
        userRight.getPerimeters().add(perimeter);
        userRightRepository.addUserRights(userRight);

        Assertions.assertThatThrownBy(() -> userRightService.doesUserOwnPrivilegeForApplicationOnPerimeter("steve.jobs", "###########", "ACCOUNTING", "READ")).
                isInstanceOf(ApplicationNotFoundException.class);
    }

    @Test
    @DisplayName("Searching unexisting perimeter should raise PerimeterNotFoundException")
    public void testPerimeterNotFoundException() {

        UserRightTest userRight = new UserRightTest("steve.jobs", "APPLE");
        Perimeter perimeter = new Perimeter("ACCOUNTING");
        perimeter.getPrivileges().add("READ");
        userRight.getPerimeters().add(perimeter);
        userRightRepository.addUserRights(userRight);

        Assertions.assertThatThrownBy(() -> userRightService.doesUserOwnPrivilegeForApplicationOnPerimeter("steve.jobs", "APPLE", "###########", "READ")).
                isInstanceOf(PerimeterNotFoundException.class);
    }

    @Test
    @DisplayName("Searching unexisting perimeter should raise PrivilegeNotFoundException")
    public void testPrivilegeNotFoundException() {

        UserRightTest userRight = new UserRightTest("steve.jobs", "APPLE");
        Perimeter perimeter = new Perimeter("ACCOUNTING");
        perimeter.getPrivileges().add("READ");
        userRight.getPerimeters().add(perimeter);
        userRightRepository.addUserRights(userRight);

        Assertions.assertThatThrownBy(() -> userRightService.doesUserOwnPrivilegeForApplicationOnPerimeter("steve.jobs", "APPLE", "ACCOUNTING", "###########")).
                isInstanceOf(PrivilegeNotFoundException.class);
    }

    @Test
    @DisplayName("Seaching user and application which are not bound together should raise RoleNotFoundException")
    public void testRoleNotFoundException() {

        UserRightTest userRight = new UserRightTest("steve.jobs", "APPLE");
        Perimeter perimeter = new Perimeter("ACCOUNTING");
        perimeter.getPrivileges().add("READ");
        userRight.getPerimeters().add(perimeter);
        userRightRepository.addUserRights(userRight);

        userRight = new UserRightTest("bill.gates", "MICROSOFT");
        perimeter = new Perimeter("ACCOUNTING");
        perimeter.getPrivileges().add("READ");
        userRight.getPerimeters().add(perimeter);
        userRightRepository.addUserRights(userRight);

        Assertions.assertThatThrownBy(() -> userRightService.doesUserOwnPrivilegeForApplicationOnPerimeter("steve.jobs", "MICROSOFT", "ACCOUNTING", "READ")).
                isInstanceOf(RoleNotFoundException.class);
    }

    @ParameterizedTest
    @CsvSource({"DEAL, READ", "DEAL, WRITE", "ACCOUNTING, WRITE"})
    @DisplayName("When user does not own given privilege for given application and given perimeter should return false")
    public void testUserDoesNotOwnRole(String perimeter, String privilege) throws Exception {

        UserRightTest userRight = new UserRightTest("steve.jobs", "APPLE");
        Perimeter perimeterObj = new Perimeter("ACCOUNTING");
        perimeterObj.getPrivileges().add("READ");
        userRight.getPerimeters().add(perimeterObj);
        userRightRepository.addUserRights(userRight);

        userRight = new UserRightTest("bill.gates", "MICROSOFT");
        perimeterObj = new Perimeter("DEAL");
        perimeterObj.getPrivileges().add("WRITE");
        userRight.getPerimeters().add(perimeterObj);
        userRightRepository.addUserRights(userRight);

        boolean result = userRightService.doesUserOwnPrivilegeForApplicationOnPerimeter("steve.jobs", "APPLE", perimeter, privilege);
        Assertions.assertThat(result).isFalse();
    }

    @ParameterizedTest
    @CsvSource({"ACCOUNTING, READ"})
    @DisplayName("When user owns given privilege for given application and given perimeter should return true")
    public void testUserOwnsRole(String perimeter, String privilege) throws Exception {

        UserRightTest userRight = new UserRightTest("steve.jobs", "APPLE");
        Perimeter perimeterObj = new Perimeter("ACCOUNTING");
        perimeterObj.getPrivileges().add("READ");
        userRight.getPerimeters().add(perimeterObj);
        userRightRepository.addUserRights(userRight);

        userRight = new UserRightTest("bill.gates", "MICROSOFT");
        perimeterObj = new Perimeter("DEAL");
        perimeterObj.getPrivileges().add("WRITE");
        userRight.getPerimeters().add(perimeterObj);
        userRightRepository.addUserRights(userRight);

        boolean result = userRightService.doesUserOwnPrivilegeForApplicationOnPerimeter("steve.jobs", "APPLE", perimeter, privilege);
        Assertions.assertThat(result).isTrue();
    }

    @ParameterizedTest
    @CsvSource({"ACCOUNTING/DEAL, READ"})
    @DisplayName("When user owns given role in upper level should return true")
    public void testUserOwnsRoleInUpperLevel(String perimeter, String privilege) throws Exception {

        UserRightTest userRight = new UserRightTest("steve.jobs", "APPLE");
        Perimeter perimeterObj = new Perimeter("ACCOUNTING");
        perimeterObj.getPrivileges().add("READ");
        userRight.getPerimeters().add(perimeterObj);
        userRightRepository.addUserRights(userRight);

        Mockito.spy(userRightRepository);
        Mockito.doReturn(true).when(userRightRepository).isPerimeterDefined(perimeter);

        boolean result = userRightService.doesUserOwnPrivilegeForApplicationOnPerimeter("steve.jobs", "APPLE", perimeter, privilege);
        Assertions.assertThat(result).isTrue();
    }

}
