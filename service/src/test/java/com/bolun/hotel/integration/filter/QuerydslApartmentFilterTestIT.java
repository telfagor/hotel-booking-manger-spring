package com.bolun.hotel.integration.filter;

import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.entity.enums.ApartmentType;
import com.bolun.hotel.integration.IntegrationTestBase;
import com.bolun.hotel.integration.util.TestDataImporter;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.bolun.hotel.entity.QApartment.apartment;
import static org.assertj.core.api.Assertions.assertThat;

class QuerydslApartmentFilterTestIT extends IntegrationTestBase {

    @ParameterizedTest
    @MethodSource("getMethodArguments")
    void checkApartmentFilter(ApartmentFilter filter, Integer expectedSize) {
        TestDataImporter.importData(session);
        Predicate predicate = QuerydslPredicate.builder()
                .add(filter.getRooms(), apartment.roomNumber::eq)
                .add(filter.getSeats(), apartment.seatNumber::eq)
                .add(filter.getDailyCost(), apartment.dailyCost::eq)
                .add(filter.getType(), apartment.apartmentType::eq)
                .build();

        List<Apartment> actualApartments = new JPAQuery<Apartment>(session)
                .select(apartment)
                .from(apartment)
                .where(predicate)
                .fetch();

        assertThat(actualApartments).hasSize(expectedSize);
    }

    static Stream<Arguments> getMethodArguments() {
        return Stream.of(
                Arguments.of(
                        ApartmentFilter.builder()
                                .rooms(null)
                                .seats(null)
                                .dailyCost(null)
                                .type(ApartmentType.LUX)
                                .build(), 2),
                Arguments.of(
                        ApartmentFilter.builder()
                                .rooms(2)
                                .seats(4)
                                .dailyCost(null)
                                .type(ApartmentType.STANDARD)
                                .build(), 1),
                Arguments.of(
                        ApartmentFilter.builder()
                                .rooms(3)
                                .seats(null)
                                .dailyCost(null)
                                .type(null)
                                .build(), 2)
        );
    }
}
