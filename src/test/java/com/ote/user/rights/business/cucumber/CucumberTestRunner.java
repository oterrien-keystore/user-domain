package com.ote.user.rights.business.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.CucumberExtension;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.stream.Stream;

@Tag("cucumber")
@CucumberOptions(
        plugin = {"pretty"},
        features = "src/test/resources/com/ote/user/rights/business/",
        tags = {"~@Ignore"},
        glue = "com.ote.user.rights.business.cucumber")
public class CucumberTestRunner {

    @ExtendWith(CucumberExtension.class)
    @TestFactory
    public Stream<DynamicTest> runCucumber(Stream<DynamicTest> scenarios) {
        return scenarios;
    }
}