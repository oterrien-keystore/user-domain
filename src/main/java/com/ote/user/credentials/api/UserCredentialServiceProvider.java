package com.ote.user.credentials.api;

import com.ote.user.credentials.business.credential.UserCredentialServiceFactory;
import com.ote.user.credentials.business.encryptor.EncryptorServiceFactory;
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
