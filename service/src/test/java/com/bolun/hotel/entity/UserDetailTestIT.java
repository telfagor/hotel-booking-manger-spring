package com.bolun.hotel.entity;

import com.bolun.hotel.util.TestObjectsUtils;
import com.bolun.integration.IntegrationTestBase;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserDetailTestIT extends IntegrationTestBase {

    @Test
    void insert() {
        User user = TestObjectsUtils.getUser("test@gmail.com");
        UserDetail userDetail = getUserDetail("1");
        userDetail.add(user);

        session.persist(user);
        session.persist(userDetail);
        session.flush();

        assertNotNull(userDetail.getId());
    }

    @Test
    void update() {
        UserDetail userDetail = saveUserDetail("test@Gmail.com", "1");
        userDetail.setMoney(1000);

        session.merge(userDetail);
        session.flush();
        session.clear();
        UserDetail actualUserDetail = session.find(UserDetail.class, userDetail.getId());

        assertEquals(userDetail, actualUserDetail);
    }

    @Test
    void shouldFindByIdIfUserDetailExist() {
        UserDetail userDetail = saveUserDetail("test@gmail.com", "1");

        UserDetail actualUserDetail = session.find(UserDetail.class, userDetail.getId());

        assertEquals(userDetail, actualUserDetail);
    }

    @Test
    void shouldReturnNullIfIdDoesNotExist() {
        UUID fakeId = UUID.randomUUID();

        UserDetail actualUserDetail = session.find(UserDetail.class, fakeId);

        assertNull(actualUserDetail);
    }

    @Test
    void delete() {
        UserDetail userDetail =saveUserDetail("test@gmail.com", "1");

        session.remove(userDetail);
        session.flush();
        UserDetail actualUserDetail = session.find(UserDetail.class, userDetail.getId());

        assertNull(actualUserDetail);
    }

    @NotNull
    private UserDetail saveUserDetail(String email, String phoneNumber) {
        User user = TestObjectsUtils.getUser(email);
        UserDetail userDetail = getUserDetail(phoneNumber);
        userDetail.add(user);
        session.persist(user);
        session.persist(userDetail);
        session.flush();
        session.clear();
        return userDetail;
    }

    private UserDetail getUserDetail(String phoneNumber) {
        return UserDetail.builder()
                .phoneNumber(phoneNumber)
                .birthdate(LocalDate.now().minusYears(20))
                .photo("path/to/photo.png")
                .money(0)
                .build();
    }
}
