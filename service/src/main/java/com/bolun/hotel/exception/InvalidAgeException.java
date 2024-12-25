package com.bolun.hotel.exception;

import lombok.Getter;

public class InvalidAgeException extends RuntimeException {

    @Getter
    private final String message;

    public InvalidAgeException(String message) {
        super(message);
        this.message = message;
    }
}
