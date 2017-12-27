package com.ote.user.credentials.business;

import com.ote.user.credentials.api.IUserCredentialService;
import com.ote.user.credentials.spi.IUserCredentialRepository;

public final class UserCredentialServiceFactory {

    public IUserCredentialService createService(IUserCredentialRepository credentialRepository) {
        return new UserCredentialService(credentialRepository);
    }
}
