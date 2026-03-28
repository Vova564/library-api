package com.vpapro.library_api.exceptions;

public class BorrowNotFoundException extends RuntimeException {
    public BorrowNotFoundException() {
        super("Book is not borrowed");
    }
}
