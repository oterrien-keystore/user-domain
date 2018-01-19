package com.ote.user.rights.business.cucumber;

import com.ote.user.rights.api.IUserRightService;
import com.ote.user.rights.api.Perimeter;
import com.ote.user.rights.api.Privilege;
import com.ote.user.rights.api.UserRightServiceProvider;
import com.ote.user.rights.business.common.PrivilegeTest;
import com.ote.user.rights.business.common.UserRightRepositoryMock;
import com.ote.user.rights.business.common.UserRightTest;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;

import java.util.List;

@Slf4j
public class StepDefinition {

    private UserRightRepositoryMock userRightRepository = new UserRightRepositoryMock();
    private IUserRightService userRightService;
    private boolean result;
    private Exception exception;

    @Before
    public void init() {
        this.userRightService = UserRightServiceProvider.getInstance().getFactory().createService(userRightRepository);
    }

    @After
    public void clean() {
        userRightRepository.clean();
    }

    @Given("the following privileges:")
    public void givenTheFollowingPrivileges(List<PrivilegeTest> privileges) {
        privileges.forEach(p -> {
            Privilege parent = null;
            if (p.getParent() != null && !p.getParent().isEmpty()) {
                parent = userRightRepository.getPrivileges().stream().
                        filter(p1 -> p1.getCode().equalsIgnoreCase(p.getParent())).
                        findAny().
                        orElseThrow(() -> new RuntimeException("Unable to find privilege " + p.getParent()));
            }
            Privilege privilege = new Privilege(p.getCode(), parent);
            userRightRepository.addPrivileges(privilege);
        });
    }

    @Given("the user '(.*)' owns following rights on application '(.*)':")
    public void givenUserOwnsFollowingRightsOnApplication(String login, String application, List<UserRoleTest> userRights) {
        UserRightTest userRight = new UserRightTest(login, application);
        userRights.forEach(r -> {
            Perimeter perimeter = new Perimeter(r.getPerimeter());
            userRight.getPerimeters().add(perimeter);
            perimeter.getPrivileges().add(r.getPrivilege());
        });
        userRightRepository.addUserRights(userRight);
    }

    @When("I want to check user '(.*)' and application '(.*)' owns privilege '(.*)' on perimeter '(.*)'")
    public void whenIWantTocHeckFollwingPrivileges(String login, String application, String privilege, String perimeter) {
        try {
            this.result = this.userRightService.doesUserOwnPrivilegeForApplicationOnPerimeter(login, application, perimeter, privilege);
        } catch (Exception e) {
            this.exception = e;
            this.result = false;
        }
    }

    @Then("an exception of type '(.*)' should be thrown")
    public void exceptionShouldBeThrown(String exceptionType) {
        Assertions.assertThat(this.exception).isNotNull();
        Assertions.assertThat(this.exception.getClass().getSimpleName()).startsWith(exceptionType);
    }


    @Then("I should get '(.*)'")
    public void iShouldGetBooleanResult(boolean result) {
        Assertions.assertThat(this.result).isEqualTo(result);
    }
}
