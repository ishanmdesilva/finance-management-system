package com.inova.pfms.exception;

public class UserNotAuthenticatedException extends RuntimeException {

    public UserNotAuthenticatedException(String message) {
        super((message));
    }

}
