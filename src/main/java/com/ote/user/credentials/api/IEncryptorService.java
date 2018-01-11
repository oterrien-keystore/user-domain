package com.ote.user.credentials.api;

import com.ote.user.credentials.api.exception.EncryptingException;

public interface IEncryptorService {

    String encrypt(String value) throws EncryptingException;
}
