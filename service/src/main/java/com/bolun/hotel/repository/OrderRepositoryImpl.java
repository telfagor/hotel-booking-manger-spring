package com.bolun.hotel.repository;

import com.bolun.hotel.dto.filters.OrderFilter;
import com.bolun.hotel.entity.Order;
import com.bolun.hotel.entity.QOrder;
import com.bolun.hotel.util.QuerydslPredicate;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static com.bolun.hotel.entity.QOrder.order;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements FilterOrderRepository {

    private final EntityManager entityManager;

    @Override
    public Page<Order> findAll(OrderFilter filter, Pageable pageable) {
        Predicate predicate = getPredicate(filter);
        OrderSpecifier<?>[] orderSpecifiers = getOrderSpecifier(pageable.getSort(), order);

        List<Order> orders = new JPAQuery<>(entityManager)
                .select(order)
                .from(order)
                .join(order.apartment)
                .join(order.user)
                .where(predicate, order.deleted.eq(false))
                .orderBy(orderSpecifiers)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        long totalOrders = getTotalOrders(predicate);
        return new PageImpl<>(orders, pageable, totalOrders);
    }

    private Predicate getPredicate(OrderFilter filter) {
        return QuerydslPredicate.builder()
                .add(filter.checkIn(), order.checkIn::goe)
                .add(filter.checkOut(), order.checkOut::loe)
                .add(filter.totalCostFrom(), order.totalCost::goe)
                .add(filter.totalCostTo(), order.totalCost::loe)
                .add(filter.apartmentNumber(), order.apartment.apartmentNumber::eq)
                .add(filter.status(), order.status::eq)
                .add(filter.email(), order.user.email::equalsIgnoreCase)
                .buildAnd();
    }

    private Long getTotalOrders(Predicate predicate) {
        return new JPAQuery<>(entityManager)
                .select(order.count())
                .from(order)
                .join(order.apartment)
                .join(order.user)
                .where(predicate, order.deleted.eq(false))
                .fetchOne();
    }

    @Override
    public Page<Order> findAllByUserId(UUID id, OrderFilter filter, Pageable pageable) {
        Predicate predicate = getPredicate(filter);
        OrderSpecifier<?>[] userOrdersSpecifier = getOrderSpecifier(pageable.getSort(), order);

        List<Order> orders = new JPAQuery<>(entityManager)
                .select(order)
                .from(order)
                .where(predicate, order.user.id.eq(id), order.deleted.eq(false))
                .orderBy(userOrdersSpecifier)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        long totalOrders = getTotalOrdersByUserId(id, filter);
        return new PageImpl<>(orders, pageable, totalOrders);
    }

    private Long getTotalOrdersByUserId(UUID id, OrderFilter filter) {
        Predicate predicate = getPredicate(filter);

        return new JPAQuery<>(entityManager)
                .select(order.count())
                .from(order)
                .where(predicate, order.user.id.eq(id), order.deleted.eq(false))
                .fetchOne();
    }

    public static OrderSpecifier[] getOrderSpecifier(Sort sort, QOrder userOrders) {
        return sort.stream()
                .map(order -> {
                    Expression path = switch (order.getProperty()) {
                        case "checkIn" -> userOrders.checkIn;
                        case "checkOut" -> userOrders.checkOut;
                        case "totalCost" -> userOrders.totalCost;
                        case "email" -> userOrders.user.email;
                        default -> throw new IllegalArgumentException("Invalid sort property: " + order.getProperty());
                    };

                    return new OrderSpecifier<>(
                            order.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC,
                            path
                    );
                })
                .toArray(OrderSpecifier[]::new);
    }
}
