package com.github.montaser.library.exception;

public class DtoValidationException extends RuntimeException {

    public DtoValidationException(String errorMessage) {
        super(errorMessage);
    }
}
