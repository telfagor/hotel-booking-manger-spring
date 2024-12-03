package com.bolun.hotel.integration.util;

import com.bolun.hotel.dto.ApartmentReadDto;
import com.bolun.hotel.dto.UserCreateEditDto;
import com.bolun.hotel.dto.UserReadDto;
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
import java.util.Random;
import java.util.UUID;

@UtilityClass
public class TestObjectsUtils {

    Random random = new Random();

    public User getUser(String email) {
        return User.builder()
                .firstName("Igor")
                .lastName("Vdovicenko")
                .email(email)
                .password(String.valueOf(random.nextInt(10)))
                .role(getRandomEnumValue(Role.class))
                .gender(getRandomEnumValue(Gender.class))
                .build();
    }

    private <T extends Enum<T>> T getRandomEnumValue(Class<T> enumClas) {
        T[] enumValues = enumClas.getEnumConstants();
        int randomIndex = random.nextInt(enumValues.length);
        return enumValues[randomIndex];
    }

    public UserDetail getUserDetail(String phoneNumber) {
        return UserDetail.builder()
                .phoneNumber(phoneNumber)
                .birthdate(LocalDate.now().minusYears(20))
                .photo("path/to/photo.png")
                .money(random.nextInt(10_000))
                .build();
    }

    public Apartment getApartment() {
        return Apartment.builder()
                .roomNumber(random.nextInt(5) + 1)
                .seatNumber(random.nextInt(8) + 2)
                .dailyCost(random.nextInt(1000) + 100)
                .apartmentType(getRandomEnumValue(ApartmentType.class))
                .photo("path/to/photo.png")
                .build();
    }

    public Order getOrder() {
        return Order.builder()
                .checkIn(LocalDate.now())
                .checkOut(LocalDate.now().plusDays(2))
                .totalCost(random.nextInt(5000) + 500)
                .status(getRandomEnumValue(OrderStatus.class))
                .build();
    }

    public UserReadDto getGetUserReadDto1() {
        return new UserReadDto(
                null,
                "John",
                "Doe",
                "john@example.com",
                Gender.MALE,
                Role.USER,
                null);
    }

   public UserReadDto getGetUserReadDto2() {
        return new UserReadDto(
                null,
                "Jane",
                "Doe",
                "jane@example.com",
                Gender.FEMALE,
                Role.USER,
                null);
   }

   public UserCreateEditDto getUserCreateEditDto(String email) {
        return new UserCreateEditDto(
                "test",
                "test",
                email,
                "test",
                "test",
                Gender.MALE
        );
    }

    public ApartmentReadDto getApartmentReadDto(UUID id) {
        return new ApartmentReadDto(
                id,
                2,
                4,
                35,
                ApartmentType.STANDARD,
                "path/to/photo.png"
        );
    }
}

