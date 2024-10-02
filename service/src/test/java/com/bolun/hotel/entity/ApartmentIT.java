package com.bolun.hotel.entity;

import com.bolun.hotel.util.TestObjectsUtils;
import com.bolun.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ApartmentIT extends IntegrationTestBase {

    @Test
    void insert() {
        Apartment apartment = TestObjectsUtils.getApartment();

        session.persist(apartment);
        session.flush();

        assertNotNull(apartment.getId());
    }

    @Test
    void update() {
        Apartment apartment = TestObjectsUtils.getApartment();
        session.persist(apartment);
        apartment.setDailyCost(400);
        apartment.setRoomNumber(4);

        session.merge(apartment);
        session.flush();
        session.clear();
        Apartment actualApartment = session.find(Apartment.class, apartment.getId());

        assertEquals(apartment, actualApartment);
    }

    @Test
    void shouldFindByIdIfApartmentExist() {
        Apartment apartment = TestObjectsUtils.getApartment();
        session.persist(apartment);
        session.flush();
        session.clear();

        Apartment actualApartment = session.find(Apartment.class, apartment.getId());

        assertEquals(apartment, actualApartment);
    }

    @Test
    void shouldReturnNullIfIdDoesNotExist() {
        UUID fakeId = UUID.randomUUID();

        Apartment actualApartment = session.find(Apartment.class, fakeId);

        assertNull(actualApartment);
    }

    @Test
    void delete() {
        Apartment apartment = TestObjectsUtils.getApartment();
        session.persist(apartment);

        session.remove(apartment);
        session.flush();
        Apartment actualApartment = session.find(Apartment.class, apartment.getId());

        assertNull(actualApartment);
    }
}


