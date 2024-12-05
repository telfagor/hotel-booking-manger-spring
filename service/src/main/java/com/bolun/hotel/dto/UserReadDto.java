package com.bolun.hotel.dto;

import com.bolun.hotel.entity.enums.Gender;
import com.bolun.hotel.entity.enums.Role;

import java.util.UUID;

public record UserReadDto(UUID id,
                          String firstName,
                          String lastName,
                          String email,
                          Gender gender,
                          Role role,
                          UserDetailReadDto userDetail) {

}
