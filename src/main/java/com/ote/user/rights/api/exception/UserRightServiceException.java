package com.ote.user.rights.api.exception;

public abstract class UserRightServiceException extends Exception{

    protected UserRightServiceException(String message){
        super(message);
    }
}