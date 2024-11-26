package com.bolun.hotel.dto.filters;

import com.bolun.hotel.entity.enums.Gender;
import com.bolun.hotel.entity.enums.Role;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record UserFilter(String firstName,
                         String lastName,
                         String email,
                         Gender gender,
                         Role role,
                         String phoneNumber,
                         String photo,
                         @DateTimeFormat(pattern = "dd.MM.yyyy")
                         LocalDate birthdateFrom,
                         @DateTimeFormat(pattern = "dd.MM.yyyy")
                         LocalDate birthdateTo,
                         Integer money) {

}
