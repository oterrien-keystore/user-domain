package com.ote.user.credentials.api;

import com.ote.user.credentials.business.UserCredentialServiceFactory;
import lombok.Getter;

public final class UserCredentialServiceProvider {

    @Getter
    public static final UserCredentialServiceProvider Instance = new UserCredentialServiceProvider();

    @Getter
    public final UserCredentialServiceFactory factory;

    private UserCredentialServiceProvider() {
        this.factory = new UserCredentialServiceFactory();
    }
}
