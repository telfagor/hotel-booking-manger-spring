package com.bolun.hotel.entity;

import com.bolun.hotel.entity.enums.OrderStatus;
import com.bolun.hotel.util.TestObjectsUtils;
import com.bolun.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderTestIT extends IntegrationTestBase {

    @Test
    void insert() {
        User user = TestObjectsUtils.getUser("test@gmail.com");
        Apartment apartment = TestObjectsUtils.getApartment();
        Order order = getOrder();
        order.add(user);
        order.add(apartment);

        session.persist(user);
        session.persist(apartment);
        session.persist(order);
        session.flush();

        assertNotNull(order.getId());
    }

    @Test
    void update() {
        Order order = saveOrder();
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
        Order order = saveOrder();

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
    void delete() {
        Order order = saveOrder();

        session.remove(order);
        session.flush();
        Order actualOrder = session.find(Order.class, order.getId());

        assertNull(actualOrder);
    }

    private Order saveOrder() {
        User user = TestObjectsUtils.getUser("test@gmail.com");
        Apartment apartment = TestObjectsUtils.getApartment();
        Order order = getOrder();
        order.add(user);
        order.add(apartment);
        session.persist(user);
        session.persist(apartment);
        session.persist(order);
        session.flush();
        session.clear();
        return order;
    }

    private Order getOrder() {
        return Order.builder()
                .checkIn(LocalDate.now())
                .checkOut(LocalDate.now().plusDays(2))
                .totalCost(2000)
                .status(OrderStatus.IN_PROGRESS)
                .build();
    }
}

