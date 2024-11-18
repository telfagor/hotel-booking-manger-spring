package com.bolun.hotel.integration.repository;

import com.bolun.hotel.dto.ApartmentFilter;
import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.entity.enums.ApartmentType;
import com.bolun.hotel.integration.IntegrationTestBase;
import com.bolun.hotel.integration.util.TestDataImporter;
import com.bolun.hotel.integration.util.TestObjectsUtils;
import com.bolun.hotel.repository.ApartmentRepository;
import com.bolun.hotel.util.QuerydslPredicate;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.bolun.hotel.entity.QApartment.apartment;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
class ApartmentRepositoryTest extends IntegrationTestBase {

    private final ApartmentRepository apartmentRepository;

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

        apartmentRepository.saveAndFlush(apartment);
        session.clear();
        Optional<Apartment> actualApartment = apartmentRepository.findById(apartment.getId());

        assertThat(actualApartment)
                .isPresent()
                .contains(apartment);
    }

    @Test
    void shouldFindById() {
        Apartment apartment = apartmentRepository.saveAndFlush(TestObjectsUtils.getApartment());
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

    @ParameterizedTest
    @MethodSource("getMethodArguments")
    void checkApartmentFilter(ApartmentFilter filter, Integer expectedApartmentSize) {
        TestDataImporter.importData(session);
        Predicate reservationPredicate = ExpressionUtils.anyOf(
                filter.getCheckIn() != null && filter.getCheckOut() != null
                        ? ExpressionUtils.anyOf(
                        apartment.orders.any().checkIn.lt(filter.getCheckOut()),
                        apartment.orders.any().checkOut.goe(filter.getCheckIn())
                )
                        : null
        );
        Predicate predicate = QuerydslPredicate.builder()
                .add(filter.getRooms(), apartment.roomNumber::eq)
                .add(filter.getSeats(), apartment.seatNumber::eq)
                .add(filter.getDailyCost(), apartment.dailyCost::eq)
                .add(filter.getType(), apartment.apartmentType::eq)
                .add(reservationPredicate, Function.identity())
                .buildAnd();
        Pageable pageable = PageRequest.of(0, 2);

        Page<Apartment> actualApartments = apartmentRepository.findAll(predicate, pageable);

        assertThat(actualApartments).hasSize(expectedApartmentSize);
    }

    static Stream<Arguments> getMethodArguments() {
        return Stream.of(
                Arguments.of(
                        ApartmentFilter.builder()
                                .rooms(null)
                                .seats(null)
                                .dailyCost(null)
                                .type(null)
                                .checkIn(LocalDate.of(2024, 4, 20))
                                .checkOut(LocalDate.of(2024, 4, 26))
                                .build(), 2),
                Arguments.of(
                        ApartmentFilter.builder()
                                .rooms(2)
                                .seats(null)
                                .dailyCost(null)
                                .type(null)
                                .checkIn(LocalDate.of(2024, 4, 20))
                                .checkOut(LocalDate.of(2024, 4, 26))
                                .build(), 2),
                Arguments.of(
                        ApartmentFilter.builder()
                                .rooms(3)
                                .seats(6)
                                .dailyCost(null)
                                .type(ApartmentType.LUX)
                                .checkIn(LocalDate.of(2024, 5, 4))
                                .checkOut(LocalDate.of(2024, 5, 5))
                                .build(), 1),
                Arguments.of(
                        ApartmentFilter.builder()
                                .rooms(300)
                                .seats(600)
                                .dailyCost(Integer.MAX_VALUE)
                                .type(ApartmentType.LUX)
                                .checkIn(LocalDate.of(2024, 5, 4))
                                .checkOut(LocalDate.of(2024, 5, 5))
                                .build(), 0)
        );
    }

    @Test
    void delete() {
        Apartment apartment = apartmentRepository.save(TestObjectsUtils.getApartment());

        apartmentRepository.delete(apartment);
        Optional<Apartment> actualApartment = apartmentRepository.findById(apartment.getId());

        assertThat(actualApartment).isEmpty();
    }
}
