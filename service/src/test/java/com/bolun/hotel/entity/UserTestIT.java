package com.bolun.hotel.entity;

import com.bolun.hotel.util.TestObjectsUtils;
import com.bolun.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserTestIT extends IntegrationTestBase {

    @Test
    void insert() {
        User user = TestObjectsUtils.getUser("test@gmail.com");

        session.persist(user);
        session.flush();

        assertNotNull(user.getId());
    }

    @Test
    void update() {
        User user = TestObjectsUtils.getUser("test@gmail.com");
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
        User user = TestObjectsUtils.getUser("test@gmail.com");
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
    void delete() {
        User user = TestObjectsUtils.getUser("test@gmail.com");
        session.persist(user);

        session.remove(user);
        session.flush();
        User actualUser = session.find(User.class, user.getId());

        assertNull(actualUser);
    }
}


