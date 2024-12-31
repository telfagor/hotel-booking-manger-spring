package com.bolun.hotel.repository;

import com.bolun.hotel.dto.filters.ApartmentFilter;
import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.entity.QApartment;
import com.bolun.hotel.util.QuerydslPredicate;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        OrderSpecifier<?>[] orderSpecifiers = getApartmentOrderSpecifier(pageable.getSort(), QApartment.apartment);

        BooleanExpression availabilityCondition = order.checkOut.goe(filter.checkOut())
                .and(order.checkIn.lt(filter.checkOut()).and(order.checkOut.gt(filter.checkIn())));


        List<Apartment> apartments = new JPAQuery<>(entityManager)
                .select(apartment)
                .from(apartment)
                .leftJoin(apartment.orders, order)
                .on(order.status.eq(APPROVED), availabilityCondition)
                .where(predicate, order.isNull(), apartment.deleted.eq(false))
                .orderBy(orderSpecifiers)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalElements = getTotalElements(predicate, availabilityCondition);
        return new PageImpl<>(apartments, pageable, totalElements);
    }

    private Predicate getPredicate(ApartmentFilter filter) {
        return QuerydslPredicate.builder()
                .add(filter.rooms(), apartment.rooms::eq)
                .add(filter.seats(), apartment.seats::eq)
                .add(filter.dailyCostFrom(), apartment.dailyCost::goe)
                .add(filter.dailyCostTo(), apartment.dailyCost::loe)
                .add(filter.apartmentType(), apartment.apartmentType::eq)
                .buildAnd();
    }

    private Long getTotalElements(Predicate predicate, BooleanExpression availabilityCondition) {
        return new JPAQuery<>(entityManager)
                .select(apartment.count())
                .from(apartment)
                .leftJoin(apartment.orders, order)
                .on(order.status.eq(APPROVED), availabilityCondition)
                .where(predicate, order.isNull(), apartment.deleted.eq(false))
                .fetchOne();
    }

    public static OrderSpecifier[] getApartmentOrderSpecifier(Sort sort, QApartment apartment) {
        return sort.stream()
                .map(order -> {
                    Expression<Integer> path = switch (order.getProperty()) {
                        case "dailyCost" -> apartment.dailyCost;
                        case "rooms" -> apartment.rooms;
                        case "seats" -> apartment.seats;
                        default -> throw new IllegalArgumentException("Invalid sort property: " + order.getProperty());
                    };

                    return new OrderSpecifier<>(
                            order.isAscending() ? Order.ASC : Order.DESC,
                            path
                    );
                })
                .toArray(OrderSpecifier[]::new);
    }
}
