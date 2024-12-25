package com.bolun.hotel.exception;

import lombok.Getter;

public class InvalidCurrentPasswordException extends RuntimeException {

    @Getter
    private final String message;

    public InvalidCurrentPasswordException(String message) {
        super(message);
        this.message = message;
    }
}
