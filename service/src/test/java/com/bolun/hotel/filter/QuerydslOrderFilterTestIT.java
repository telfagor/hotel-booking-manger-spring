package com.bolun.hotel.filter;

import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.entity.Order;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.enums.OrderStatus;
import com.bolun.hotel.filter.paramresolver.OrderFilterParamResolver;
import com.bolun.hotel.util.HibernateTestUtil;
import com.bolun.hotel.util.OrderFilter;
import com.bolun.hotel.util.TestDataImporter;
import com.bolun.hotel.util.TestObjectsUtils;
import com.bolun.integration.IntegrationTestBase;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityGraph;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static com.bolun.hotel.entity.QOrder.order;
import static com.bolun.hotel.entity.QUser.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(OrderFilterParamResolver.class)
class QuerydslOrderFilterTestIT extends IntegrationTestBase {

    @BeforeAll
    static void createSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        TestDataImporter.importData(sessionFactory);
    }

    @Test
    void findAllByStatus(OrderFilter orderFilter) {
        BooleanExpression predicate = QPredicate.builder()
                .addQuerydslPredicate(orderFilter.getStatus(), order.status::eq)
                .buildQuerydsl();

        List<Order> actualOrders = new JPAQuery<Order>(session)
                .select(order)
                .from(order)
                .where(predicate)
                .fetch();

        assertThat(actualOrders).hasSize(2);
        List<OrderStatus> orderStatuses = actualOrders.stream()
                .map(Order::getStatus)
                .toList();
        assertThat(orderStatuses).allMatch(status -> status.equals(OrderStatus.REJECTED));
    }

    @Test
    void findAllByEmail(OrderFilter orderFilter) {
        BooleanExpression predicate = QPredicate.builder()
                .addQuerydslPredicate(orderFilter.getUser().getEmail(), user.email::eq)
                .buildQuerydsl();

        List<Order> actualOrders = new JPAQuery<Order>(session)
                .select(order)
                .from(order)
                .where(predicate)
                .fetch();

        assertThat(actualOrders).hasSize(4);
        List<String> emails = actualOrders.stream()
                .map(Order::getUser)
                .map(User::getEmail)
                .toList();
        assertThat(emails).allMatch(email -> email.equals(orderFilter.getUser().getEmail()));
    }

    @Test
    void findOrderApartmentUserByOrderId() {
        Order givenOrder = TestObjectsUtils.getOrder();
        User givenUser = TestObjectsUtils.getUser("my@mail.ru");
        Apartment givenApartment = TestObjectsUtils.getApartment();
        givenOrder.add(givenUser);
        givenOrder.add(givenApartment);
        session.persist(givenUser);
        session.persist(givenApartment);
        session.persist(givenOrder);
        EntityGraph<Order> entityGraph = session.createEntityGraph(Order.class);
        entityGraph.addAttributeNodes("user");
        entityGraph.addAttributeNodes("apartment");
        BooleanExpression predicate = QPredicate.builder()
                .addQuerydslPredicate(givenOrder.getId(), order.id::eq)
                .buildQuerydsl();

        Order actualOrder = new JPAQuery<Order>(session)
                .select(order)
                .from(order)
                .where(predicate)
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .fetchFirst();

        assertNotNull(actualOrder);
        assertNotNull(actualOrder.getApartment());
        assertNotNull(actualOrder.getUser());
    }

    @Test
    void findAllApartmentsFromOrdersByUserEmail(OrderFilter orderFilter) {
        EntityGraph<Apartment> entityGraph = session.createEntityGraph(Apartment.class);
        BooleanExpression predicate = QPredicate.builder()
                .addQuerydslPredicate(orderFilter.getUser().getEmail(), order.user.email::eq)
                .buildQuerydsl();

        List<Apartment> actualApartments = new JPAQuery<Order>(session)
                .select(order.apartment)
                .from(order)
                .where(predicate)
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .fetch();

        assertThat(actualApartments).hasSize(2);
    }

    @Test
    void findAllApartmentsFromOrdersByUserFirstName(OrderFilter orderFilter) {
        EntityGraph<Apartment> entityGraph = session.createEntityGraph(Apartment.class);
        BooleanExpression predicate = QPredicate.builder()
                .addQuerydslPredicate(orderFilter.getUser().getFirstName(), order.user.firstName::eq)
                .buildQuerydsl();

        List<Apartment> actualApartments = new JPAQuery<Order>(session)
                .select(order.apartment)
                .from(order)
                .where(predicate)
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .fetch();

        assertThat(actualApartments).hasSize(4);
    }
}