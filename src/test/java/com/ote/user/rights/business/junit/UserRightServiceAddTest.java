package com.ote.user.rights.business.junit;

import com.ote.user.rights.api.IUserRightService;
import com.ote.user.rights.api.Perimeter;
import com.ote.user.rights.api.Privilege;
import com.ote.user.rights.api.UserRightServiceProvider;
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

import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class UserRightServiceAddTest {

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
    @DisplayName("adding unexisting right should provide those rights")
    public void addUnexistingRight() throws Exception {

        UserRightTest userRight = new UserRightTest("steve.jobs", "APPLE");
        Perimeter perimeterObj = new Perimeter("ACCOUNTING");
        perimeterObj.getPrivileges().add("READ");
        userRight.getPerimeters().add(perimeterObj);
        userRightRepository.addUserRights(userRight);

        Mockito.doReturn(true).when(userRightRepository).isPerimeterDefined("DEAL");

        userRightService.addRights("steve.jobs", "APPLE", "DEAL", "READ");

        boolean result = userRightService.doesUserOwnPrivilegeForApplicationOnPerimeter("steve.jobs", "APPLE", "DEAL", "READ");
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("adding new right for existing perimeter should insert into")
    public void addUnexistingRightOnExistingPerimeter() throws Exception {

        UserRightTest userRight = new UserRightTest("steve.jobs", "APPLE");
        Perimeter perimeterObj = new Perimeter("ACCOUNTING");
        perimeterObj.getPrivileges().add("WRITE");
        userRight.getPerimeters().add(perimeterObj);
        userRightRepository.addUserRights(userRight);

        Mockito.doReturn(true).when(userRightRepository).isPrivilegeDefined("OTHER");
        Mockito.doReturn(new Privilege("OTHER")).when(userRightRepository).getPrivilegeHierarchy("OTHER");

        userRightService.addRights("steve.jobs", "APPLE", "ACCOUNTING", "OTHER");

        Optional<Perimeter> accountingPerimeter = userRightRepository.getPerimeters("steve.jobs", "APPLE").
                stream().
                filter(p -> p.getCode().equals("ACCOUNTING")).
                findAny();
        Assertions.assertThat(accountingPerimeter).isPresent();

        Set<String> privileges = accountingPerimeter.get().getPrivileges();
        Assertions.assertThat(privileges).contains("WRITE");
        Assertions.assertThat(privileges).contains("OTHER");
    }

    @ParameterizedTest
    @CsvSource({
            "READ, READ, READ",
            "READ, WRITE, WRITE",
            "WRITE, READ, WRITE",
            "ADMIN, READ, ADMIN",
            "READ, ADMIN, ADMIN"})
    @DisplayName("adding privilege which belong to an existing privilege's hierarchy for existing perimeter should let the highest privilege")
    public void addNewPrivilegeOnExistingPerimeterWhereExistingPrivilegeIsInTheSameHierachy(String existingPrivilege, String newPrivilege, String expectedRemainingPrivilege) throws Exception {

        UserRightTest userRight = new UserRightTest("steve.jobs", "APPLE");
        Perimeter perimeterObj = new Perimeter("ACCOUNTING");
        perimeterObj.getPrivileges().add(existingPrivilege);
        userRight.getPerimeters().add(perimeterObj);
        userRightRepository.addUserRights(userRight);

        userRightService.addRights("steve.jobs", "APPLE", "ACCOUNTING", newPrivilege);

        Optional<Perimeter> accountingPerimeter = userRightRepository.getPerimeters("steve.jobs", "APPLE").
                stream().
                filter(p -> p.getCode().equals("ACCOUNTING")).
                findAny();
        Assertions.assertThat(accountingPerimeter).isPresent();

        Set<String> privileges = accountingPerimeter.get().getPrivileges();
        Assertions.assertThat(privileges.size()).isEqualTo(1);
        Assertions.assertThat(privileges).contains(expectedRemainingPrivilege);
    }
}
