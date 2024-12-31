package com.bolun.hotel.validation;

import com.bolun.hotel.validation.impl.DataRangeValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DataRangeValidatorImpl.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataRange {

    String message() default "Check in must be after check out";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
