package com.bolun.hotel.entity;

import com.bolun.integration.IntegrationTestBase;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;

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

class UserTestIT extends IntegrationTestBase {

    @Test
    void insert() {
        User user = getUser("test@gmail.com");

        session.persist(user);
        session.flush();

        assertNotNull(user.getId());
    }

    @Test
    void update() {
        User user = getUser("test@gmail.com");
        session.persist(user);
        user.setFirstName("abc");
        user.setLastName("abc");

        session.merge(user);
        session.flush();
        session.clear();
        User actualUser = session.find(User.class, user.getId());

        assertEquals(user, actualUser);
    }

    @Test
    void shouldFindByIdIfUserExist() {
        User user = getUser("test@gmail.com");
        session.persist(user);
        session.flush();
        session.clear();

        User actualResult = session.find(User.class, user.getId());

        assertEquals(user, actualResult);
    }

    @Test
    void shouldReturnNullIfIdDoesNotExist() {
        UUID fakeId = UUID.randomUUID();

        User user = session.find(User.class, fakeId);

        assertNull(user);
    }

    @Test
    void findAll() {
        User user1 = getUser("test@gmail.com");
        User user2 = getUser("test@gmail2.com");
        User user3 = getUser("test@gmail3.com");
        session.persist(user1);
        session.persist(user2);
        session.persist(user3);
        session.flush();
        session.clear();

        User actualUser1 = session.find(User.class, user1.getId());
        User actualUser2 = session.find(User.class, user2.getId());
        User actualUser3 = session.find(User.class, user3.getId());

        List<String> actualEmails = Stream.of(actualUser1, actualUser2, actualUser3)
                .map(User::getEmail)
                .toList();
        assertThat(actualEmails).hasSize(3);
        assertThat(actualEmails).containsExactlyInAnyOrder(actualUser1.getEmail(), actualUser2.getEmail(), actualUser3.getEmail());
    }

    @Test
    void delete() {
        User user = getUser("test@gmail.com");
        session.persist(user);
        session.flush();

        session.remove(user);
        session.flush();
        User actualUser = session.find(User.class, user.getId());

        assertNull(actualUser);
    }

    @Test
    void shouldThrowExceptionIfEmailIsNotUnique() {
        User user = getUser("test@gmail.com");
        User user2 = getUser("test@gmail.com");
        session.persist(user);
        session.flush();
        session.clear();

        ConstraintViolationException ex = assertThrows(ConstraintViolationException.class, () -> {
                    session.persist(user2);
                    session.flush();
                }
        );

        String expectedMessage = "duplicate key value violates unique constraint \"user_email_key\"";
        String actualMessage = ex.getCause().getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}


