package com.bolun.hotel.dto;

import com.bolun.hotel.validation.PasswordMatcherValidator;
import com.bolun.hotel.validation.PasswordsMatcher;
import jakarta.validation.constraints.NotBlank;

@PasswordsMatcher
public record ChangePasswordDto(@NotBlank(message = "Current password is required")
                                String currentPassword,
                                @NotBlank(message = "New password is required")
                                String newPassword,
                                @NotBlank(message = "Confirm password is required")
                                String confirmNewPassword) implements PasswordMatcherValidator {

    @Override
    public String getPassword() {
        return newPassword;
    }

    @Override
    public String getConfirmPassword() {
        return confirmNewPassword;
    }
}
