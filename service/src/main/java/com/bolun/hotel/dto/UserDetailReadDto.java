package com.bolun.hotel.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

public record UserDetailReadDto(UUID id,
                               String phoneNumber,
                               String photo,

                               @DateTimeFormat(pattern = "dd.MM.yyyy")
                               LocalDate birthdate,
                               Integer money,
                               UUID userId) {

}
