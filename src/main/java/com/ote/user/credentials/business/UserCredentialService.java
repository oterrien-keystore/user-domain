package com.ote.user.credentials.business;

import com.ote.user.credentials.api.IUserCredentialService;
import com.ote.user.credentials.api.exception.UserNotFoundException;
import com.ote.user.credentials.spi.IUserCredentialRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class UserCredentialService implements IUserCredentialService {

    private final IUserCredentialRepository credentialRepository;

    @Override
    public boolean areCredentialsCorrect(String user, String password) throws UserNotFoundException {

        if (!credentialRepository.isUserDefined(user)) {
            throw new UserNotFoundException(user);
        }

        return Objects.equals(credentialRepository.getPassword(user), Encryptor.getInstance().encrypt(password));
    }
}
