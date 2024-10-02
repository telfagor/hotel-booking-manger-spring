package com.bolun.hotel.util;

import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.enums.ApartmentType;
import com.bolun.hotel.entity.enums.Gender;
import com.bolun.hotel.entity.enums.Role;

public class TestObjectsUtils {

    public static User getUser(String email) {
        return User.builder()
                .firstName("Igor")
                .lastName("Vdovicenko")
                .email(email)
                .password("123")
                .role(Role.ADMIN)
                .gender(Gender.MALE)
                .build();
    }

    public static Apartment getApartment() {
        return Apartment.builder()
                .roomNumber(4)
                .seatNumber(7)
                .dailyCost(220)
                .apartmentType(ApartmentType.STANDARD)
                .photo("path/to/photo.png")
                .build();
    }
}
