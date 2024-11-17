package com.github.montaser.library.exception;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}