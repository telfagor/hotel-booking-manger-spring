package com.bolun.hotel.integration.repository;

import com.bolun.hotel.dto.filters.OrderFilter;
import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.entity.Order;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.enums.OrderStatus;
import com.bolun.hotel.integration.IntegrationTestBase;
import com.bolun.hotel.integration.util.TestDataImporter;
import com.bolun.hotel.integration.util.TestObjectsUtils;
import com.bolun.hotel.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

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
        order.setDeleted(false);

        orderRepository.saveAndFlush(order);
        session.clear();
        Optional<Order> actualOrder = orderRepository.findById(order.getId());

        assertThat(actualOrder)
                .isPresent()
                .hasValueSatisfying(actual -> {
                    assertThat(actual.getId()).isEqualTo(order.getId());
                    assertThat(actual.getCheckIn()).isEqualTo(order.getCheckIn());
                    assertThat(actual.getCheckOut()).isEqualTo(order.getCheckOut());
                    assertThat(actual.getTotalCost()).isEqualTo(order.getTotalCost());
                    assertThat(actual.getDeleted()).isEqualTo(order.getDeleted());
                    assertThat(actual.getStatus()).isEqualTo(order.getStatus());
                });
    }

    @Test
    void shouldFindById() {
        Order order = orderRepository.save(getOrder("test@gmail.com"));
        order.setDeleted(false);
        orderRepository.saveAndFlush(order);
        session.clear();

        Optional<Order> actualOrder = orderRepository.findById(order.getId());

        assertThat(actualOrder)
                .isPresent()
                .hasValueSatisfying(actual -> {
                    assertThat(actual.getId()).isEqualTo(order.getId());
                    assertThat(actual.getCheckIn()).isEqualTo(order.getCheckIn());
                    assertThat(actual.getCheckOut()).isEqualTo(order.getCheckOut());
                    assertThat(actual.getTotalCost()).isEqualTo(order.getTotalCost());
                    assertThat(actual.getDeleted()).isEqualTo(order.getDeleted());
                    assertThat(actual.getStatus()).isEqualTo(order.getStatus());
                });
    }

    @Test
    void shouldNotFindById() {
        UUID fakeId = UUID.randomUUID();

        Optional<Order> actualOrder = orderRepository.findById(fakeId);

        assertThat(actualOrder).isEmpty();
    }

    @MethodSource("getMethodArguments")
    @ParameterizedTest
    void findAll(OrderFilter filter, int expectedSize) {
        TestDataImporter.importData(session);
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);

        Page<Order> actualResult = orderRepository.findAll(filter, pageable);

        assertThat(actualResult.getTotalElements()).isEqualTo(expectedSize);
    }

    static Stream<Arguments> getMethodArguments() {
        return Stream.of(
                Arguments.of(
                        new OrderFilter(
                                LocalDate.of(2024, 4, 20),
                                LocalDate.of(2024, 4, 26),
                                2000,
                                2000,
                                "tudor@gmail.com",
                                OrderStatus.REJECTED,
                                null
                        ), 1
                ),
                Arguments.of(
                        new OrderFilter(
                                null,
                                null,
                                null,
                                null,
                                "tudor@gmail.com",
                                null,
                                null
                        ), 4
                ),
                Arguments.of(
                        new OrderFilter(
                                null,
                                null,
                                1300,
                                1500,
                                null,
                                null,
                                null
                        ), 3
                )
        );
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
        session.persist(user);
        session.persist(apartment);
        session.flush();
        order.add(user);
        order.add(apartment);
        return order;
    }
}
