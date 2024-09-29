package com.bolun.integration;

import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.entity.Order;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.UserDetail;
import com.bolun.hotel.entity.enums.ApartmentType;
import com.bolun.hotel.entity.enums.Gender;
import com.bolun.hotel.entity.enums.OrderStatus;
import com.bolun.hotel.entity.enums.Role;
import com.bolun.hotel.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTestIT {

    private static SessionFactory sessionFactory;
    private Session session;

    @BeforeAll
    static void createSessionFactory() {
        sessionFactory = HibernateUtil.buildSessionFactory();
    }

    @AfterAll
    static void closeSessionFactory() {
        sessionFactory.close();
    }

    @BeforeEach
    void openSession() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @AfterEach
    void closeSession() {
        session.getTransaction().rollback();
        session.close();
    }

    @Test
    void checkUserDetail() {
        User user = getUser();
        UserDetail userDetail = getUserDetail();
        session.persist(user);
        userDetail.setUserId(user.getId());
        session.persist(userDetail);
        session.flush();

        session.clear();
        UserDetail userDetailActualResult = session.find(UserDetail.class, userDetail.getId());

        assertEquals(userDetailActualResult, userDetail);
    }

    @Test
    void checkUser() {
        User user = getUser();
        session.persist(user);
        session.flush();
        session.clear();

        User userActualResult = session.find(User.class, user.getId());

        assertEquals(user, userActualResult);
    }

    @Test
    void checkOrder() {
        User user = getUser();
        Apartment apartment = getApartment();
        session.persist(user);
        session.persist(apartment);
        Order order = getOrder(user, apartment);
        session.persist(order);
        session.flush();
        session.clear();

        Order orderActualResult = session.find(Order.class, order.getId());

        assertEquals(order, orderActualResult);
    }

    private Order getOrder(User user, Apartment apartment) {
        return Order.builder()
                .checkIn(LocalDate.now())
                .checkOut(LocalDate.now().plusDays(5))
                .totalCost(250)
                .status(OrderStatus.IN_PROGRESS)
                .userId(user.getId())
                .apartmentId(apartment.getId())
                .build();
    }

    private UserDetail getUserDetail() {
        return UserDetail.builder()
                .phoneNumber("+743456876")
                .birthdate(LocalDate.of(1989, 9, 19))
                .money(2500)
                .build();
    }

    private User getUser() {
        return User.builder()
                .firstName("Igor")
                .lastName("Vdovicenko")
                .email("test@gmail.com")
                .password("123")
                .role(Role.ADMIN)
                .gender(Gender.MALE)
                .build();
    }

    private Apartment getApartment() {
        return Apartment.builder()
                .roomNumber(4)
                .seatNumber(5)
                .dailyCost(250)
                .apartmentType(ApartmentType.STANDARD)
                .photo("path/to/photo.png")
                .build();
    }
}

