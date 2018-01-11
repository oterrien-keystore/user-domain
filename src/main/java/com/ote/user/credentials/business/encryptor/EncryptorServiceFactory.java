package com.ote.user.credentials.business.encryptor;

import com.ote.user.credentials.api.IEncryptorService;

public final class EncryptorServiceFactory {

    public IEncryptorService createService() {
        return new EncryptorService();
    }
}
