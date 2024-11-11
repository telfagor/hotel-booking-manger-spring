package com.bolun.hotel.dto;

import com.bolun.hotel.entity.enums.Gender;

public record UserCreateEditDto(String firstName,
                                String lastName,
                                String email,
                                String password,
                                String confirmPassword,
                                Gender gender) {

}
