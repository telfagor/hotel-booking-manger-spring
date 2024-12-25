package com.bolun.hotel.validation.impl;

import com.bolun.hotel.validation.PasswordMatcherValidator;
import com.bolun.hotel.validation.PasswordsMatcher;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PasswordMatcherValidatorImpl implements ConstraintValidator<PasswordsMatcher, PasswordMatcherValidator> {

    private String passwordField;
    private String confirmPasswordField;

    @Override
    public void initialize(PasswordsMatcher constraintAnnotation) {
        this.passwordField = constraintAnnotation.passwordField();
        this.confirmPasswordField = constraintAnnotation.confirmPasswordField();
    }

    @Override
    public boolean isValid(PasswordMatcherValidator user, ConstraintValidatorContext context) {
        if (user == null) {
            return true;
        }

        boolean passwordsMatch = user.getPassword().equals(user.getConfirmPassword());
        if (!passwordsMatch) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode(confirmPasswordField)
                    .addConstraintViolation();
        }
        return passwordsMatch;
    }
}

