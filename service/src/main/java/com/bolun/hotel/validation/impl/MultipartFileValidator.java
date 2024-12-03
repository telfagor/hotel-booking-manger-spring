package com.bolun.hotel.validation.impl;

import com.bolun.hotel.validation.ValidPhoto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MultipartFileValidator implements ConstraintValidator<ValidPhoto, MultipartFile> {

    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024;

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File size must be less than 5MB")
                    .addConstraintViolation();
            return false;
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Only image files are allowed")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
