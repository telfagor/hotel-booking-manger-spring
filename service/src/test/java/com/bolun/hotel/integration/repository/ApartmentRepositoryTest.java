package com.bolun.hotel.integration.repository;

import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.integration.IntegrationTestBase;
import com.bolun.hotel.integration.util.TestObjectsUtils;
import com.bolun.hotel.repository.ApartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApartmentRepositoryTest extends IntegrationTestBase {

    private ApartmentRepository apartmentRepository;

    @BeforeEach
    void init() {
        apartmentRepository = new ApartmentRepository(session);
    }

    @Test
    void insert() {
        Apartment apartment = TestObjectsUtils.getApartment();

        Apartment actualApartment = apartmentRepository.save(apartment);

        assertNotNull(actualApartment.getId());
    }

    @Test
    void update() {
        Apartment apartment = apartmentRepository.save(TestObjectsUtils.getApartment());
        apartment.setRoomNumber(1);
        apartment.setSeatNumber(4);

        apartmentRepository.update(apartment);
        session.clear();
        Optional<Apartment> actualApartment = apartmentRepository.findById(apartment.getId());

        assertThat(actualApartment)
                .isPresent()
                .contains(apartment);
    }

    @Test
    void shouldFindById() {
        Apartment apartment = apartmentRepository.save(TestObjectsUtils.getApartment());
        session.clear();

        Optional<Apartment> actualApartment = apartmentRepository.findById(apartment.getId());

        assertThat(actualApartment)
                .isPresent()
                .contains(apartment);
    }

    @Test
    void shouldNotFindById() {
        UUID fakeId = UUID.randomUUID();

        Optional<Apartment> actualApartment = apartmentRepository.findById(fakeId);

        assertThat(actualApartment).isEmpty();
    }

    @Test
    void findAll() {
        Apartment apartment1 = apartmentRepository.save(TestObjectsUtils.getApartment());
        Apartment apartment2 = apartmentRepository.save(TestObjectsUtils.getApartment());
        Apartment apartment3 = apartmentRepository.save(TestObjectsUtils.getApartment());

        List<Apartment> actualApartments = apartmentRepository.findAll();

        List<UUID> ids = actualApartments.stream()
                .map(Apartment::getId)
                .toList();
        assertThat(ids).containsExactlyInAnyOrder(apartment1.getId(), apartment2.getId(), apartment3.getId());
    }

    @Test
    void delete() {
        Apartment apartment = apartmentRepository.save(TestObjectsUtils.getApartment());

        apartmentRepository.delete(apartment);
        Optional<Apartment> actualApartment = apartmentRepository.findById(apartment.getId());

        assertThat(actualApartment).isEmpty();
    }
}
