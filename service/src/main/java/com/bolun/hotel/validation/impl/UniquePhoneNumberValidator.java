package com.bolun.hotel.validation.impl;

import com.bolun.hotel.service.UserDetailService;
import com.bolun.hotel.validation.UniquePhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniquePhoneNumberValidator implements ConstraintValidator<UniquePhoneNumber, String> {

    private final UserDetailService userDetailService;

    public UniquePhoneNumberValidator(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        return userDetailService.findByPhoneNumber(phoneNumber).isEmpty();
    }
}
