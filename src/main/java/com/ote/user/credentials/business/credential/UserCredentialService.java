package com.ote.user.credentials.business.credential;

import com.ote.user.credentials.api.EncryptorServiceProvider;
import com.ote.user.credentials.api.IEncryptorService;
import com.ote.user.credentials.api.IUserCredentialService;
import com.ote.user.credentials.api.exception.EncryptingException;
import com.ote.user.credentials.api.exception.UserNotFoundException;
import com.ote.user.credentials.spi.IUserCredentialRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class UserCredentialService implements IUserCredentialService {

    private final IUserCredentialRepository credentialRepository;

    private final IEncryptorService encryptorService = EncryptorServiceProvider.getInstance().getFactory().createService();

    @Override
    public boolean areCredentialsCorrect(String user, String password) throws UserNotFoundException, EncryptingException {

        if (!credentialRepository.isUserDefined(user)) {
            throw new UserNotFoundException(user);
        }

        return Objects.equals(credentialRepository.getPassword(user), encryptorService.encrypt(password));
    }
}
