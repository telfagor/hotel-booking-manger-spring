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
        session.getTransaction().commit();
        session.close();
    }

    @Test
    void checkUserDetail() {
        User user = getUser("telfagor@gmail.com");
        UserDetail userDetail = getUserDetail("1");
        session.persist(user);
        userDetail.setUserId(user.getId());
        session.persist(userDetail);
        session.flush();

        UserDetail userDetailActualResult = session.find(UserDetail.class, userDetail.getId());

        assertEquals(userDetailActualResult, userDetail);
    }

    @Test
    void checkUser() {
        User user = getUser("telfagor2@gmail.com");
        UserDetail userDetail = getUserDetail("2");
        session.persist(user);
        userDetail.setUserId(user.getId());
        session.persist(userDetail);
        session.flush();

        User userActualResult = session.find(User.class, user.getId());

        assertEquals(user, userActualResult);
    }

    @Test
    void checkOrder() {
        User user = getUser("tefagor3@mail.ru");
        UserDetail userDetail = getUserDetail("3");
        Apartment apartment = getApartment("path/to/photo.png");
        session.persist(user);
        userDetail.setUserId(user.getId());
        session.persist(userDetail);
        session.persist(apartment);
        session.flush();

        Order order = Order.builder()
                .checkIn(LocalDate.now())
                .checkOut(LocalDate.now().plusDays(5))
                .totalCost(250)
                .status(OrderStatus.IN_PROGRESS)
                .userId(user.getId())
                .apartmentId(apartment.getId())
                .build();

        session.persist(order);
        session.flush();

        Order orderActualResult = session.find(Order.class, order.getId());

        assertEquals(order, orderActualResult);
    }

    private UserDetail getUserDetail(String phoneNumber) {
        return UserDetail.builder()
                .phoneNumber(phoneNumber)
                .birthdate(LocalDate.of(1989, 9, 19))
                .money(2500)
                .build();
    }

    private User getUser(String email) {
        return User.builder()
                .firstName("Igor")
                .lastName("Vdovicenko")
                .email(email)
                .password("123")
                .role(Role.ADMIN)
                .gender(Gender.MALE)
                .build();
    }

    private Apartment getApartment(String photo) {
        return Apartment.builder()
                .roomNumber(4)
                .seatNumber(5)
                .dailyCost(250)
                .apartmentType(ApartmentType.STANDARD)
                .photo(photo)
                .build();
    }
}

