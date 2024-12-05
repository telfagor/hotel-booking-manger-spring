package com.bolun.hotel.exception;

import lombok.Getter;

public class InsufficientFundsException extends RuntimeException {

    @Getter
    private final String message;

    public InsufficientFundsException(String message) {
        super(message);
        this.message = message;
    }
}
