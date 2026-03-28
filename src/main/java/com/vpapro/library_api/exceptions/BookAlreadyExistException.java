package com.vpapro.library_api.exceptions;

public class BookAlreadyExistException extends RuntimeException {
    public BookAlreadyExistException() {
        super("The book with this ISBN already exists");
    }
}
