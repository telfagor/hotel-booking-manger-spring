package com.bolun.hotel.integration.util;

import com.bolun.hotel.dto.ApartmentCreateEditDto;
import com.bolun.hotel.dto.ApartmentReadDto;
import com.bolun.hotel.dto.OrderCreateEditDto;
import com.bolun.hotel.dto.OrderReadDto;
import com.bolun.hotel.dto.UserCreateEditDto;
import com.bolun.hotel.dto.UserDetailCreateEditDto;
import com.bolun.hotel.dto.UserDetailReadDto;
import com.bolun.hotel.dto.UserReadDto;
import com.bolun.hotel.dto.filters.ApartmentFilter;
import com.bolun.hotel.dto.filters.OrderFilter;
import com.bolun.hotel.dto.filters.UserFilter;
import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.entity.Order;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.UserDetail;
import com.bolun.hotel.entity.enums.ApartmentType;
import com.bolun.hotel.entity.enums.Gender;
import com.bolun.hotel.entity.enums.OrderStatus;
import com.bolun.hotel.entity.enums.Role;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

import static com.bolun.hotel.entity.enums.Gender.MALE;
import static com.bolun.hotel.entity.enums.Role.ADMIN;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@UtilityClass
public class TestObjectsUtils {

    @Getter
    private final UserFilter userFilter = new UserFilter(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    );

    @Getter
    private final ApartmentFilter apartmentFilter = new ApartmentFilter(
            null,
            null,
            null,
            null,
            null,
            null,
            null
    );

    @Getter
    private final OrderFilter orderFilter = new OrderFilter(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    );

    public Apartment getApartment() {
        return Apartment.builder()
                .rooms(1)
                .apartmentNumber(2)
                .seats(4)
                .dailyCost(50)
                .apartmentType(ApartmentType.STANDARD)
                .photo("path/to/photo.png")
                .build();
    }

    public Apartment getApartment2() {
        return Apartment.builder()
                .rooms(4)
                .apartmentNumber(4)
                .seats(8)
                .dailyCost(1000)
                .apartmentType(ApartmentType.LUX)
                .photo("path/to/photo.png")
                .build();
    }

    public ApartmentReadDto getApartmentReadDto() {
        return new ApartmentReadDto(
                null,
                2,
                1,
                4,
                50,
                ApartmentType.STANDARD,
                "path/to/photo.png"
        );
    }


    public ApartmentReadDto getApartmentReadDto2() {
        return new ApartmentReadDto(
                null,
                4,
                4,
                8,
                1000,
                ApartmentType.LUX,
                "path/to/photo.png"
        );
    }

    public User getUser(String email) {
        return User.builder()
                .firstName("Igor")
                .lastName("Vdovicenko")
                .email(email)
                .password("123")
                .role(ADMIN)
                .gender(MALE)
                .userDetail(null)
                .build();
    }

    public UserDetail getUserDetail(String phoneNumber) {
        return UserDetail.builder()
                .phoneNumber(phoneNumber)
                .birthdate(LocalDate.now().minusYears(20))
                .photo("path/to/photo.png")
                .money(2000)
                .build();
    }

    public UserReadDto getUserReadDto1() {
        return new UserReadDto(
                null,
                "John",
                "Doe",
                "john@example.com",
                MALE,
                Role.USER,
                null
        );
    }

    public UserReadDto getUserReadDto2() {
        return new UserReadDto(
                null,
                "Jane",
                "Doe",
                "jane@example.com",
                Gender.FEMALE,
                Role.USER,
                null
        );
    }

    public UserCreateEditDto getUserCreateEditDto(String email) {
        return new UserCreateEditDto(
                "John",
                "Doe",
                email,
                "123",
                "123",
                Role.USER,
                MALE,
                "+123456789",
                3000,
                LocalDate.now().minusYears(20),
                new MockMultipartFile("photo", "image.png", IMAGE_PNG_VALUE, new byte[0])
        );
    }

    public UserReadDto getUserReadDto(String email) {
        return new UserReadDto(
                UUID.randomUUID(),
                "John",
                "Doe",
                email,
                MALE,
                Role.USER,
                null
        );
    }


    public UserCreateEditDto getUserCreateEditDto(String email, String phoneNumber) {
        return new UserCreateEditDto(
                "John",
                "Doe",
                email,
                "123",
                "123",
                Role.USER,
                MALE,
                phoneNumber,
                3000,
                LocalDate.now().minusYears(20),
                new MockMultipartFile("image.png", new byte[0])
        );
    }

    public UserCreateEditDto getUserCreateEditDto() {
        return new UserCreateEditDto(
                "John",
                "Doe",
                "john.doe@example.com",
                "123",
                "123",
                Role.USER,
                MALE,
                "+373 6813245",
                3000,
                LocalDate.now().minusYears(20),
                null
        );
    }

    public UserCreateEditDto getUserCreateEditDto(MockMultipartFile mockPhoto) {
        return new UserCreateEditDto(
                "John",
                "Doe",
                "john.doe@example.com",
                "123",
                "123",
                Role.USER,
                MALE,
                "+373 6813245",
                3000,
                LocalDate.now().minusYears(20),
                mockPhoto
        );
    }

    public ApartmentCreateEditDto getApartmentCreateEditDto(MultipartFile photo) {
        return new ApartmentCreateEditDto(
                2,
                4,
                40,
                ApartmentType.LUX,
                photo
        );
    }

    public UserDetailCreateEditDto getUserDetailCreateEditDto(MultipartFile mockPhoto) {
        return new UserDetailCreateEditDto(
                "+373 60445667",
                3000,
                LocalDate.now().minusYears(20),
                mockPhoto,
                getUser("test@gmail.com")
        );
    }

    public UserDetailReadDto getUserDetailReadDto() {
        return new UserDetailReadDto(null, null, null, null, null, null);
    }

    public OrderCreateEditDto getOrderCreateEditDto() {
        return new OrderCreateEditDto(
                null, null, null, null, null
        );
    }

    public Order getOrder() {
        return Order.builder()
                .checkIn(LocalDate.now())
                .checkOut(LocalDate.now().plusDays(2))
                .totalCost(100)
                .status(OrderStatus.IN_PROGRESS)
                .build();
    }

    public Order getOrder2() {
        return Order.builder()
                .checkIn(LocalDate.now().plusDays(1))
                .checkOut(LocalDate.now().plusDays(3))
                .totalCost(70)
                .status(OrderStatus.IN_PROGRESS)
                .build();
    }


    public OrderReadDto getOrderReadDto() {
        return new OrderReadDto(
                null,
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                100,
                OrderStatus.IN_PROGRESS,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public OrderReadDto getOrderReadDto2() {
        return new OrderReadDto(
                null,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                70,
                OrderStatus.IN_PROGRESS,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}

