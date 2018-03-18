package com.ote.user.rights.api.exception;

public class PerimeterNotFoundException extends Exception {

    private static final String NOT_FOUND_MESSAGE = "Perimeter '%s' not found";

    public PerimeterNotFoundException(String application) {
        super(String.format(NOT_FOUND_MESSAGE, application));
    }
}
