package com.github.montaser.library.exception;

public class RecordAlreadyExistedException extends RuntimeException {
    public RecordAlreadyExistedException(String errorMessage) {
        super(errorMessage);
    }
}
