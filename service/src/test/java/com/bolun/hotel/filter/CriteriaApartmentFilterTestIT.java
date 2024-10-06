package com.bolun.hotel.filter;

import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.entity.Apartment_;
import com.bolun.hotel.entity.enums.ApartmentType;
import com.bolun.integration.IntegrationTestBase;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CriteriaApartmentFilterTestIT extends IntegrationTestBase {

    @ParameterizedTest
    @MethodSource("getMethodArguments")
    void checkApartmentFilter(ApartmentFilter filter, Integer expectedSize) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Apartment> criteria = cb.createQuery(Apartment.class);
        Root<Apartment> apartmentRoot = criteria.from(Apartment.class);
        CriteriaPredicate predicateBuilder = CriteriaPredicate.builder()
                .add(filter.getRooms(), roomNumber -> cb.equal(apartmentRoot.get(Apartment_.roomNumber), roomNumber))
                .add(filter.getSeats(), seatNumber -> cb.equal(apartmentRoot.get(Apartment_.seatNumber), seatNumber))
                .add(filter.getDailyCost(), apDailyCost -> cb.equal(apartmentRoot.get(Apartment_.dailyCost), apDailyCost))
                .add(filter.getType(), apType -> cb.equal(apartmentRoot.get(Apartment_.apartmentType), apType));
        criteria.select(apartmentRoot).where(predicateBuilder.getPredicates().toArray(new Predicate[0]));

        List<Apartment> actualApartments = session.createQuery(criteria).list();

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
