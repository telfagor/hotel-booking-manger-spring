package com.bolun.hotel.validation;

import com.bolun.hotel.validation.impl.MultipartFileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MultipartFileValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoto {

    String message() default "Photo is required";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
