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

        BooleanExpression availabilityCondition = order.checkOut.goe(filter.getCheckOut())
                .and(order.checkIn.lt(filter.getCheckOut()).and(order.checkOut.gt(filter.getCheckIn())));


        List<Apartment> apartments = new JPAQuery<>(entityManager)
                .select(apartment)
                .from(apartment)
                .leftJoin(apartment.orders, order)
                .on(order.status.eq(APPROVED), availabilityCondition)
                .where(predicate, order.isNull(), apartment.deleted.eq(false))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalElements = getTotalElements(predicate, availabilityCondition);

        return new PageImpl<>(apartments, pageable, totalElements);
    }

    private Predicate getPredicate(ApartmentFilter filter) {
        return QuerydslPredicate.builder()
                .add(filter.getRooms(), apartment.roomNumber::eq)
                .add(filter.getSeats(), apartment.seatNumber::eq)
                .add(filter.getDailyCostFrom(), apartment.dailyCost::goe)
                .add(filter.getDailyCostTo(), apartment.dailyCost::loe)
                .add(filter.getApartmentType(), apartment.apartmentType::eq)
                .buildAnd();
    }

    private Long getTotalElements(Predicate predicate,
                                  BooleanExpression availabilityCondition) {
        return new JPAQuery<>(entityManager)
                .select(apartment.count())
                .from(apartment)
                .leftJoin(apartment.orders, order)
                .on(order.status.eq(APPROVED), availabilityCondition)
                .where(predicate, order.isNull(), apartment.deleted.eq(false))
                .fetchOne();
    }
}
