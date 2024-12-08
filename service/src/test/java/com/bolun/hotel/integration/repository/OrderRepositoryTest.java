/*
package com.bolun.hotel.integration.repository;

import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.entity.Order;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.enums.OrderStatus;
import com.bolun.hotel.integration.IntegrationTestBase;
import com.bolun.hotel.integration.util.TestObjectsUtils;
import com.bolun.hotel.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
class OrderRepositoryTest extends IntegrationTestBase {

    private final OrderRepository orderRepository;

    @Test
    void insert() {
        Order order = getOrder("test@gmail.com");

        Order actualOrder = orderRepository.save(order);

        assertNotNull(actualOrder.getId());
    }

    @Test
    void update() {
        Order order = orderRepository.save(getOrder("test@gmail.com"));
        order.setCheckOut(LocalDate.now().plusDays(1));
        order.setStatus(OrderStatus.APPROVED);

        orderRepository.saveAndFlush(order);
        session.clear();
        Optional<Order> actualOrder = orderRepository.findById(order.getId());

        assertThat(actualOrder)
                .isPresent()
                .contains(order);
    }

    @Test
    void shouldFindById() {
        Order order = orderRepository.saveAndFlush(getOrder("test@gmail.com"));
        session.clear();

        Optional<Order> actualOrder = orderRepository.findById(order.getId());

        assertThat(actualOrder)
                .isPresent()
                .contains(order);
    }

    @Test
    void shouldNotFindById() {
        UUID fakeId = UUID.randomUUID();

        Optional<Order> actualOrder = orderRepository.findById(fakeId);

        assertThat(actualOrder).isEmpty();
    }

    @Test
    void findAll() {
        Order order1 = orderRepository.save(getOrder("test1@gmail.com"));
        Order order2 = orderRepository.save(getOrder("test2@gmail.com"));
        Order order3 = orderRepository.save(getOrder("test3@gmail.com"));

        List<Order> actualOrders = orderRepository.findAll();

        List<UUID> ids = actualOrders.stream()
                .map(Order::getId)
                .toList();
        assertThat(ids).containsExactlyInAnyOrder(order1.getId(), order2.getId(), order3.getId());
    }

    @Test
    void delete() {
        Order order = orderRepository.save(getOrder("test@gmail.com"));

        orderRepository.delete(order);
        Optional<Order> actualOrder = orderRepository.findById(order.getId());

        assertThat(actualOrder).isEmpty();
    }

    private Order getOrder(String email) {
        User user = TestObjectsUtils.getUser(email);
        Apartment apartment = TestObjectsUtils.getApartment();
        Order order = TestObjectsUtils.getOrder();
        order.add(user);
        order.add(apartment);
        session.persist(user);
        session.persist(apartment);
        session.flush();
        return order;
    }
}
*/
