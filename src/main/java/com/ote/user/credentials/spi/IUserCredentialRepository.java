package com.ote.user.credentials.spi;

public interface IUserCredentialRepository {

    boolean isUserDefined(String user);

    String getPassword(String user);
}
