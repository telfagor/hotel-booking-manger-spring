package com.bolun.hotel.entity;

import com.bolun.hotel.entity.enums.OrderStatus;
import com.bolun.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.bolun.hotel.util.TestObjectsUtils.getApartment;
import static com.bolun.hotel.util.TestObjectsUtils.getUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderTestIT extends IntegrationTestBase {

    @Test
    void insert() {
        Order order = getOrder("test@gmail.com");

        session.persist(order);
        session.flush();

        assertNotNull(order.getId());
    }

    @Test
    void update() {
        Order order = getOrder("test@gmail.com");
        session.persist(order);
        order.setCheckOut(LocalDate.now().plusDays(4));
        order.setStatus(OrderStatus.APPROVED);

        session.merge(order);
        session.flush();
        session.clear();
        Order actualOrder = session.find(Order.class, order.getId());

        assertEquals(order, actualOrder);
    }

    @Test
    void shouldFindByIdIfOrderExist() {
        Order order = getOrder("test@gmail.com");
        session.persist(order);
        session.flush();
        session.clear();

        Order actualOrder = session.find(Order.class, order.getId());

        assertEquals(order, actualOrder);
    }

    @Test
    void shouldReturnNullIfIdDoesNotExist() {
        UUID fakeId = UUID.randomUUID();

        Order actualOrder = session.find(Order.class, fakeId);

        assertNull(actualOrder);
    }

    @Test
    void findAll() {
        Order order1 = getOrder("test@gmail1.com");
        Order order2 = getOrder("test@gmail2.com");
        Order order3 = getOrder("test@gmail3.com");
        session.persist(order1);
        session.persist(order2);
        session.persist(order3);
        session.flush();
        session.clear();

        Order actualOrder1 = session.find(Order.class, order1.getId());
        Order actualOrder2 = session.find(Order.class, order2.getId());
        Order actualOrder3 = session.find(Order.class, order3.getId());

        List<UUID> actualIds = Stream.of(actualOrder1, actualOrder2, actualOrder3)
                .map(Order::getId)
                .toList();
        assertThat(actualIds).hasSize(3);
        assertThat(actualIds).containsExactlyInAnyOrder(order1.getId(), order2.getId(), order3.getId());
    }

    @Test
    void delete() {
        Order order = getOrder("test@gmail.com");
        session.persist(order);

        session.remove(order);
        session.flush();
        Order actualOrder = session.find(Order.class, order.getId());

        assertNull(actualOrder);
    }

    private Order getOrder(String email) {
        User user = getUser(email);
        Apartment apartment = getApartment();
        Order order = prepareOrder(user, apartment);
        order.add(user);
        order.add(apartment);
        return order;
    }

    private Order prepareOrder(User user, Apartment apartment) {
        return Order.builder()
                .checkIn(LocalDate.now())
                .checkOut(LocalDate.now().plusDays(2))
                .totalCost(2000)
                .status(OrderStatus.IN_PROGRESS)
                .user(user)
                .apartment(apartment)
                .build();
    }
}

