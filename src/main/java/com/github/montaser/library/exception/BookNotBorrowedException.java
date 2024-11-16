package com.github.montaser.library.exception;

public class BookNotBorrowedException extends RuntimeException{
    public BookNotBorrowedException(String message){
        super(message);
    }
}
