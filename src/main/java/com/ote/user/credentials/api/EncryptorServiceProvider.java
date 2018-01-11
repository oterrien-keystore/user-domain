package com.ote.user.credentials.api;

import com.ote.user.credentials.business.encryptor.EncryptorServiceFactory;
import lombok.Getter;

public final class EncryptorServiceProvider {

    @Getter
    public static final EncryptorServiceProvider Instance = new EncryptorServiceProvider();

    @Getter
    public final EncryptorServiceFactory factory;

    private EncryptorServiceProvider() {
        this.factory = new EncryptorServiceFactory();
    }
}
