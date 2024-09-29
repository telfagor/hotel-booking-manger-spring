package com.bolun.hotel.entity;

import com.bolun.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.bolun.hotel.util.TestObjectsUtils.getApartment;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ApartmentIT extends IntegrationTestBase {

    @Test
    void insert() {
        Apartment apartment = getApartment();

        session.persist(apartment);
        session.flush();

        assertNotNull(apartment.getId());
    }

    @Test
    void update() {
        Apartment apartment = getApartment();
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
        Apartment apartment = getApartment();
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
    void findAll() {
        Apartment apartment1 = getApartment();
        Apartment apartment2 = getApartment();
        Apartment apartment3 = getApartment();
        session.persist(apartment1);
        session.persist(apartment2);
        session.persist(apartment3);
        session.flush();
        session.clear();

        Apartment actualApartment1 = session.find(Apartment.class, apartment1.getId());
        Apartment actualApartment2 = session.find(Apartment.class, apartment2.getId());
        Apartment actualApartment3 = session.find(Apartment.class, apartment3.getId());

        List<UUID> actualIds = Stream.of(actualApartment1, actualApartment2, actualApartment3)
                .map(Apartment::getId)
                .toList();
        assertThat(actualIds).hasSize(3);
        assertThat(actualIds).containsExactlyInAnyOrder(apartment1.getId(), apartment2.getId(), apartment3.getId());
    }

    @Test
    void delete() {
        Apartment apartment = getApartment();
        session.persist(apartment);
        session.flush();

        session.remove(apartment);
        session.flush();
        Apartment actualApartment = session.find(Apartment.class, apartment.getId());

        assertNull(actualApartment);
    }
}


