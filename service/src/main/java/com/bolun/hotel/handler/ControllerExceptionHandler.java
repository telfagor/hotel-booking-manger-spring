package com.bolun.hotel.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(basePackages = "com.bolun.hotel.controller")
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex) {
        log.error("Failed to return response", ex);
        return "error/error500";
    }
}
