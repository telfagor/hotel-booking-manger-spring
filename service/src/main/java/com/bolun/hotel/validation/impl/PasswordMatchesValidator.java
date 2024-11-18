package com.bolun.hotel.validation.impl;

import com.bolun.hotel.dto.UserCreateEditDto;
import com.bolun.hotel.validation.PasswordsMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PasswordMatchesValidator implements ConstraintValidator<PasswordsMatch, UserCreateEditDto> {

    @Override
    public boolean isValid(UserCreateEditDto user, ConstraintValidatorContext context) {
        if (user == null) {
            return true;
        }

        boolean passwordsMatch = user.password().equals(user.confirmPassword());
        if (!passwordsMatch) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }
        return passwordsMatch;
    }
}
