package com.bolun.hotel.exception;

import lombok.Getter;

public class InvalidOldPasswordException extends RuntimeException {

    @Getter
    private final String message;

    public InvalidOldPasswordException(String message) {
        super(message);
        this.message = message;
    }
}
