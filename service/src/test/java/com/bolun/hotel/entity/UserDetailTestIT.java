package com.bolun.hotel.entity;

import com.bolun.integration.IntegrationTestBase;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.bolun.hotel.util.TestObjectsUtils.getUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDetailTestIT extends IntegrationTestBase {

    @Test
    void insert() {
        UserDetail userDetail = getUserDetail("1", getUser("test@gmail.com"));

        session.persist(userDetail);
        session.flush();

        assertNotNull(userDetail.getId());
    }

    @Test
    void update() {
        UserDetail userDetail = getUserDetail("1", getUser("test@gmail.com"));
        session.persist(userDetail);
        userDetail.setMoney(1000);

        session.merge(userDetail);
        session.flush();
        session.clear();
        UserDetail actualUserDetail = session.find(UserDetail.class, userDetail.getId());

        assertEquals(userDetail, actualUserDetail);
    }

    @Test
    void shouldFindByIdIfUserDetailExist() {
        UserDetail userDetail = getUserDetail("1", getUser("test@gmail.com"));
        session.persist(userDetail);
        session.flush();
        session.clear();

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
    void findAll() {
        UserDetail userDetail1 = prepareUserDetail("1", getUser("test@gmail1.com"));
        UserDetail userDetail2 = prepareUserDetail("2", getUser("test@gmail2.com"));
        UserDetail userDetail3 = prepareUserDetail("3", getUser("test@gmail3.com"));
        session.persist(userDetail1);
        session.persist(userDetail2);
        session.persist(userDetail3);
        session.flush();
        session.clear();

        UserDetail actualUserDetail1 = session.find(UserDetail.class, userDetail1.getId());
        UserDetail actualUserDetail2 = session.find(UserDetail.class, userDetail2.getId());
        UserDetail actualUserDetail3 = session.find(UserDetail.class, userDetail3.getId());

        List<String> actualPhoneNumbers = Stream.of(actualUserDetail1, actualUserDetail2, actualUserDetail3)
                .map(UserDetail::getPhoneNumber)
                .toList();
        assertThat(actualPhoneNumbers).hasSize(3);
        assertThat(actualPhoneNumbers).containsExactlyInAnyOrder(actualUserDetail1.getPhoneNumber(),
                actualUserDetail2.getPhoneNumber(), actualUserDetail3.getPhoneNumber());
    }

    @Test
    void delete() {
        UserDetail userDetail = getUserDetail("1", getUser("test@gmail.com"));
        session.persist(userDetail);

        session.remove(userDetail);
        session.flush();
        UserDetail actualUserDetail = session.find(UserDetail.class, userDetail.getId());

        assertNull(actualUserDetail);
    }

    @Test
    void shouldThrowExceptionIfPhoneNumberIsNotUnique() {
        User user = getUser("test@gmail.com");
        UserDetail userDetail = prepareUserDetail("1", user);
        User user2 = getUser("test@gmail2.com");
        UserDetail userDetail2 = prepareUserDetail("1", user2);
        userDetail.add(user);
        session.persist(userDetail);
        session.flush();

        ConstraintViolationException ex = assertThrows(ConstraintViolationException.class, () -> {
            userDetail2.add(user2);
            session.persist(userDetail2);
            session.flush();
        });

        String expectedMessage = "duplicate key value violates unique constraint \"user_detail_phone_number_key\"";
        String actualMessage = ex.getCause().getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowExceptionIfUserIdIsNotUnique() {
        User user = getUser("test@gmail.com");
        UserDetail userDetail = prepareUserDetail("1", user);
        userDetail.add(user);
        session.persist(userDetail);
        session.flush();
        UserDetail userDetail2 = prepareUserDetail("2", user);
        userDetail2.add(user);

        ConstraintViolationException ex = assertThrows(ConstraintViolationException.class, () -> {
            session.persist(userDetail2);
            session.flush();
        });

        String expectedMessage = "duplicate key value violates unique constraint \"user_detail_user_id_key\"";
        String actualMessage = ex.getCause().getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    private UserDetail getUserDetail(String phoneNumber, User user) {
        return prepareUserDetail(phoneNumber, user);
    }


    private UserDetail prepareUserDetail(String phoneNumber, User user) {
        return UserDetail.builder()
                .phoneNumber(phoneNumber)
                .user(user)
                .birthdate(LocalDate.now().minusYears(20))
                .photo("path/to/photo")
                .money(0)
                .build();
    }
}
