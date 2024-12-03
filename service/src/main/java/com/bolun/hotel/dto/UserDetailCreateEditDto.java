package com.bolun.hotel.dto;

import com.bolun.hotel.validation.ValidPhoto;
import com.bolun.hotel.validation.group.CreateAction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

public record UserDetailCreateEditDto(

        @NotBlank
        String phoneNumber,

        Integer money,

        @NotBlank
        @Past(message = "Birthdate must be in the past")
        @DateTimeFormat(pattern = "dd.MM.yyyy")
        LocalDate birthdate,

        @NotNull(message = "Photo is required", groups = CreateAction.class)
        @ValidPhoto
        MultipartFile photo,
        UUID userId) {
}
