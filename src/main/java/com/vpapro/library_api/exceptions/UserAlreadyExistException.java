package com.vpapro.library_api.exceptions;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException() {
        super("The user with this email already exists");
    }
}
