package com.bolun.hotel.integration.util;

import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.entity.Order;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.UserDetail;
import com.bolun.hotel.entity.enums.ApartmentType;
import com.bolun.hotel.entity.enums.Gender;
import com.bolun.hotel.entity.enums.OrderStatus;
import com.bolun.hotel.entity.enums.Role;
import jakarta.persistence.EntityManager;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;

import java.time.LocalDate;

@UtilityClass
public class TestDataImporter {

    public void importData(Session session) {
        User andrei = saveUser(session, "Andrei", "Chirtoaca", "andrei@gmail",
                "123", Role.USER, Gender.MALE);
        User christina = saveUser(session, "Christina", "Aguilera", "christina@gmail.com",
                "123", Role.USER, Gender.FEMALE);
        User ivan = saveUser(session, "Ivan", "King", "ivan@gmail.com",
                "345", Role.ADMIN, Gender.MALE);
        User pavel = saveUser(session, "Pavel", "Gavrila", "pavel@gmail.com",
                "345", Role.ADMIN, Gender.MALE);
        User tudor = saveUser(session, "Tudor", "Anisimov", "tudor@gmail.com",
                "456", Role.USER, Gender.MALE);
        User tudorAgache = saveUser(session, "Tudor", "Agache", "tudor_agache@gmail.com",
                "456", Role.USER, Gender.MALE);

        UserDetail andreiUserDetail = saveUserDetail(session, andrei, "1",
                LocalDate.now().minusYears(20), 1000);
        UserDetail christinaUserDetail = saveUserDetail(session, christina, "2",
                LocalDate.now().minusYears(20), 1000);
        UserDetail ivanUserDetail = saveUserDetail(session, ivan, "3",
                LocalDate.now().minusYears(22), 1000);
        UserDetail pavelUserDetail = saveUserDetail(session, pavel, "4",
                LocalDate.now().minusYears(23), 1000);
        UserDetail tudorUserDetail = saveUserDetail(session, tudor, "5",
                LocalDate.now().minusYears(24), 1000);
        UserDetail tudorAgacheUserDetail = saveUserDetail(session, tudorAgache, "6",
                LocalDate.now().minusYears(24), 1500);

        Apartment apartment1 = saveApartment(session, 2, 4, 25, ApartmentType.STANDARD);
        Apartment apartment2 = saveApartment(session, 3, 6, 30, ApartmentType.LUX);
        Apartment apartment3 = saveApartment(session, 4, 8, 35, ApartmentType.STANDARD);
        Apartment apartment4 = saveApartment(session, 3, 7, 40, ApartmentType.LUX);
        Apartment apartment5 = saveApartment(session, 2, 5, 35, ApartmentType.STANDARD);

        Order andreiOrder = saveOrder(session, andrei, apartment1, LocalDate.now(),
                LocalDate.now().plusDays(2), 1200, OrderStatus.APPROVED);

        Order christinaOrder = saveOrder(session, christina, apartment2, LocalDate.now(),
                LocalDate.now().plusDays(3), 1300, OrderStatus.IN_PROGRESS);

        Order ivanOrder = saveOrder(session, ivan, apartment1,
                LocalDate.of(2024, 4, 20),
                LocalDate.of(2024, 4, 21), 1400, OrderStatus.REJECTED);
        Order pavelOrder = saveOrder(session, pavel, apartment1,
                LocalDate.of(2024, 4, 22),
                LocalDate.of(2024, 4, 23), 1500, OrderStatus.APPROVED);
        Order tudorOrder = saveOrder(session, tudor, apartment1,
                LocalDate.of(2024, 4, 24),
                LocalDate.of(2024, 4, 25), 2000, OrderStatus.APPROVED);
        Order tudorOrder2 = saveOrder(session, tudor, apartment5,
                LocalDate.of(2024, 4, 20),
                LocalDate.of(2024, 4, 26), 2000, OrderStatus.REJECTED);
        Order tudorOrder3 = saveOrder(session, tudor, apartment3,
                LocalDate.of(2024, 4, 21),
                LocalDate.of(2024, 4, 22), 2000, OrderStatus.APPROVED);
        Order tudorOrder4 = saveOrder(session, tudor, apartment5,
                LocalDate.of(2024, 5, 7),
                LocalDate.of(2024, 5, 9), 2000, OrderStatus.APPROVED);
        Order tudorAgacheOrder1 = saveOrder(session, tudorAgache, apartment1,
                LocalDate.of(2024, 5, 1),
                LocalDate.of(2024, 5, 4), 2000, OrderStatus.APPROVED);
        Order tudorAgacheOrder2 = saveOrder(session, tudorAgache, apartment2,
                LocalDate.of(2024, 5, 2),
                LocalDate.of(2024, 5, 3), 2000, OrderStatus.APPROVED);
        Order tudorAgacheOrder3 = saveOrder(session, tudorAgache, apartment2,
                LocalDate.of(2024, 5, 7),
                LocalDate.of(2024, 5, 10), 2000, OrderStatus.APPROVED);
    }

    public User saveUser(EntityManager session,
                         String firstName,
                         String lastName,
                         String email,
                         String password,
                         Role role,
                         Gender gender) {

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .role(role)
                .gender(gender)
                .build();

        session.persist(user);
        return user;
    }

    public UserDetail saveUserDetail(Session session,
                                     User user,
                                     String phoneNumber,
                                     LocalDate birthdate,
                                     Integer money) {

        UserDetail userDetail = UserDetail.builder()
                .phoneNumber(phoneNumber)
                .birthdate(birthdate)
                .money(money)
                .build();

        user.add(userDetail);
        session.persist(user);
        session.persist(userDetail);
        return userDetail;
    }

    public Apartment saveApartment(Session session,
                                   Integer roomNumber,
                                   Integer seatNumber,
                                   Integer dailyCost,
                                   ApartmentType type) {

        Apartment apartment = Apartment.builder()
                .roomNumber(roomNumber)
                .seatNumber(seatNumber)
                .dailyCost(dailyCost)
                .apartmentType(type)
                .photo("path/to/photo.png")
                .build();

        session.persist(apartment);
        return apartment;
    }

    private static Order saveOrder(Session session, User user, Apartment apartment, LocalDate checkIn,
                                   LocalDate checkOut, Integer totalCost, OrderStatus orderStatus) {

        Order order = Order.builder()
                .checkIn(checkIn)
                .checkOut(checkOut)
                .totalCost(totalCost)
                .status(orderStatus)
                .build();

        order.add(user);
        order.add(apartment);
        session.persist(order);
        return order;
    }
}
