package com.vpapro.library_api.exceptions;

public class BookIsNotAvailableException extends RuntimeException {
    public BookIsNotAvailableException() {
        super("No available amount of this book");
    }
}
