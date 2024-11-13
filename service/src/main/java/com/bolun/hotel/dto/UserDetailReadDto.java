package com.bolun.hotel.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UserDetailReadDto(UUID id,
                               String phoneNumber,
                               String photo,
                               LocalDate birthdate,
                               Integer money) {

}
