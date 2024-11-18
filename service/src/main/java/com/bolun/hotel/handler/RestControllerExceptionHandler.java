package com.bolun.hotel.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "com.bolun.hotel.rest")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

}
