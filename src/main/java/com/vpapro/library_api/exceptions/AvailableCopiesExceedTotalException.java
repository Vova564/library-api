package com.vpapro.library_api.exceptions;

public class AvailableCopiesExceedTotalException extends RuntimeException {
    public AvailableCopiesExceedTotalException() {
        super("Available number of books exceed total");
    }
}
