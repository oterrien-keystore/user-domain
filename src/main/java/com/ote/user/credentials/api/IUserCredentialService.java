package com.ote.user.credentials.api;

import com.ote.user.credentials.api.exception.UserNotFoundException;

public interface IUserCredentialService {

    boolean areCredentialsCorrect(String user, String password) throws UserNotFoundException;
}
