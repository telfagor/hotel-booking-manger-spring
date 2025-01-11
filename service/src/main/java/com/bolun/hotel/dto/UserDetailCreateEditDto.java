package com.bolun.hotel.dto;

import com.bolun.hotel.entity.User;
import com.bolun.hotel.validation.ValidPhoto;
import com.bolun.hotel.validation.group.UpdateAction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record UserDetailCreateEditDto(

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^\\+\\d{3} \\d{8}$",
                message = "Phone number must match the format +373 67643434",
                groups = UpdateAction.class
        )
        String phoneNumber,

        Integer money,

        @NotNull(message = "Birthdate is required")
        @Past(message = "Birthdate must be in the past")
        @DateTimeFormat(pattern = "dd.MM.yyyy")
        LocalDate birthdate,

        @ValidPhoto(groups = UpdateAction.class)
        MultipartFile photo,
        User user) {
}
