package com.bolun.hotel.util;

import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.entity.Order;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.UserDetail;
import com.bolun.hotel.entity.enums.ApartmentType;
import com.bolun.hotel.entity.enums.Gender;
import com.bolun.hotel.entity.enums.OrderStatus;
import com.bolun.hotel.entity.enums.Role;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class TestObjectsUtils {

    public User getUser(String email) {
        return User.builder()
                .firstName("Igor")
                .lastName("Vdovicenko")
                .email(email)
                .password("123")
                .role(Role.ADMIN)
                .gender(Gender.MALE)
                .build();
    }

    public UserDetail getUserDetail(String phoneNumber) {
        return UserDetail.builder()
                .phoneNumber(phoneNumber)
                .birthdate(LocalDate.now().minusYears(20))
                .photo("path/to/photo.png")
                .money(0)
                .build();
    }

    public Apartment getApartment() {
        return Apartment.builder()
                .roomNumber(4)
                .seatNumber(7)
                .dailyCost(220)
                .apartmentType(ApartmentType.STANDARD)
                .photo("path/to/photo.png")
                .build();
    }

    public Order getOrder() {
        return Order.builder()
                .checkIn(LocalDate.now())
                .checkOut(LocalDate.now().plusDays(2))
                .totalCost(2000)
                .status(OrderStatus.IN_PROGRESS)
                .build();
    }
}

