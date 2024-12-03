package com.bolun.hotel.repository;

import com.bolun.hotel.dto.filters.OrderFilter;
import com.bolun.hotel.entity.Order;
import com.bolun.hotel.util.QuerydslPredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bolun.hotel.entity.QOrder.order;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements FilterOrderRepository {

    private final EntityManager entityManager;

    @Override
    public Page<Order> findAll(OrderFilter filter, Pageable pageable) {
        Predicate predicate = QuerydslPredicate.builder()
                .add(filter.checkIn(), order.checkIn::goe)
                .add(filter.checkOut(), order.checkOut::loe)
                .add(filter.totalCost(), order.totalCost::eq)
                .add(filter.apartmentNumber(), order.apartment.apartmentNumber::eq)
                .add(filter.email(), order.user.email::eq)
                .buildAnd();

        List<Order> orders = new JPAQuery<>(entityManager)
                .select(order)
                .from(order)
                .join(order.apartment)
                .join(order.user)
                .where(predicate)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        long totalElements = getTotalElements(predicate);
        return new PageImpl<>(orders, pageable, totalElements);
    }

    private Long getTotalElements(Predicate predicate) {
        return new JPAQuery<>(entityManager)
                .select(order.count())
                .from(order)
                .join(order.apartment)
                .join(order.user)
                .where(predicate)
                .fetchOne();
    }
}
