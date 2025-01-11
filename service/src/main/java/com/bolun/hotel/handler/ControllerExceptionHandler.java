package com.bolun.hotel.handler;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice(basePackages = "com.bolun.hotel.controller")
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        log.error("Unexpected exception: {}", ex.getMessage(), ex);
        model.addAttribute("message", "An unexpected error occurred. Please try again later.");
        return "error/error500";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatusException(ResponseStatusException ex, Model model, HttpServletResponse response) {
        log.warn("ResponseStatusException: {}", ex.getReason(), ex);

        String customMessage;
        HttpStatusCode statusCode = ex.getStatusCode();
        if (NOT_FOUND.equals(statusCode)) {
            customMessage = "The page you're looking for might have been removed or is temporarily unavailable.";
        } else if (FORBIDDEN.equals(statusCode)) {
            customMessage = "You do not have permission to access this resource.";
        } else if (BAD_REQUEST.equals(statusCode)) {
            customMessage = "The request is invalid. Please check your input.";
        } else {
            customMessage = "An unexpected error occurred. Please try again later.";
        }

        model.addAttribute("message", customMessage);
        model.addAttribute("status", ex.getStatusCode());
        response.setStatus(ex.getStatusCode().value());

        return "error/error";
    }
}
