package com.ote.user.rights.business.junit;


import com.ote.user.rights.api.Privilege;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class PrivilegeTest {

    @Test
    public void testPrivilegeHierarchy() {

        Privilege admin = new Privilege("ADMIN");
        Privilege write = new Privilege("WRITE", admin);
        Privilege read = new Privilege("READ", write);

        Assertions.assertThat(read.isDefined("READ")).isTrue();
        Assertions.assertThat(read.isDefined("WRITE")).isTrue();
        Assertions.assertThat(read.isDefined("ADMIN")).isTrue();
    }

}
