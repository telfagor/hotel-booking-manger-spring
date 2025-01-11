package com.bolun.hotel.exception;

public class InvalidCurrentPasswordException extends RuntimeException {

    public InvalidCurrentPasswordException(String message) {
        super(message);
    }
}
