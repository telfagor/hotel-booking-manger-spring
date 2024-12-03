package com.bolun.hotel.repository;

import com.bolun.hotel.dto.filters.ApartmentFilter;
import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.util.QuerydslPredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bolun.hotel.entity.QApartment.apartment;
import static com.bolun.hotel.entity.QOrder.order;
import static com.bolun.hotel.entity.enums.OrderStatus.APPROVED;

@RequiredArgsConstructor
@Repository
public class ApartmentRepositoryImpl implements FilterApartmentRepository {

    private final EntityManager entityManager;

    @Override
    public Page<Apartment> findAll(ApartmentFilter filter, Pageable pageable) {
        Predicate predicate = getPredicate(filter);

        BooleanExpression availabilityCondition = order.isNull()
                .or(order.status.ne(APPROVED));

        BooleanExpression checkInCondition = getByCheckInDate(filter);
        BooleanExpression checkOutCondition = getByCheckOutDate(filter);

        BooleanExpression dateCondition = checkInCondition.or(checkOutCondition);

        List<Apartment> apartments = new JPAQuery<>(entityManager)
                .select(apartment)
                .from(apartment)
                .leftJoin(apartment.orders, order)
                .on(dateCondition)
                .where(predicate, availabilityCondition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalElements = getTotalElements(predicate, availabilityCondition, dateCondition);

        return new PageImpl<>(apartments, pageable, totalElements);
    }

    private Predicate getPredicate(ApartmentFilter filter) {
        return QuerydslPredicate.builder()
                .add(filter.getRooms(), apartment.roomNumber::eq)
                .add(filter.getSeats(), apartment.seatNumber::eq)
                .add(filter.getDailyCost(), apartment.dailyCost::eq)
                .add(filter.getApartmentType(), apartment.apartmentType::eq)
                .buildAnd();
    }

    private static BooleanExpression getByCheckInDate(ApartmentFilter filter) {
        BooleanExpression checkInAfter = order.checkIn.gt(filter.getCheckIn());
        BooleanExpression checkInBefore = order.checkIn.lt(filter.getCheckOut());
        return checkInAfter.and(checkInBefore);
    }

    private static BooleanExpression getByCheckOutDate(ApartmentFilter filter) {
        BooleanExpression checkOutAfter = order.checkOut.gt(filter.getCheckIn());
        BooleanExpression checkOutBefore = order.checkOut.lt(filter.getCheckOut());
        return checkOutAfter.or(checkOutBefore);
    }

    private Long getTotalElements(Predicate predicate,
                                  BooleanExpression availabilityCondition,
                                  BooleanExpression dateCondition) {
        return new JPAQuery<>(entityManager)
                .select(apartment.count())
                .from(apartment)
                .leftJoin(apartment.orders, order)
                .on(dateCondition)
                .where(predicate, availabilityCondition)
                .fetchOne();
    }
}
