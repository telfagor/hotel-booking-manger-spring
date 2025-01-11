package com.bolun.hotel.integration.controller;

import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.entity.Order;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.UserDetail;
import com.bolun.hotel.integration.IntegrationTestBase;
import com.bolun.hotel.integration.util.TestObjectsUtils;
import com.bolun.hotel.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static rocks.cleancode.hamcrest.record.HasFieldMatcher.field;
import static rocks.cleancode.hamcrest.record.HasFieldMatcher.hasField;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class OrderControllerTestIT extends IntegrationTestBase {

    private final MockMvc mockMvc;
    private final OrderRepository orderRepository;

    @Test
    void findAll() throws Exception {
        session.persist(getOrder("test1@gmail.com", "+373 60644566"));
        session.persist(getOrder("test2@gmail.com", "+373 60644565"));
        int page = 0;
        int size = 2;

        mockMvc.perform(get("/orders")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpectAll(
                        status().isOk(),
                        view().name("order/orders"),
                        model().attributeExists("data", "filter", "sortOptions", "selectedSort", "baseUrl"),
                        model().attribute("data", hasProperty("content", hasSize(size))),
                        model().attribute("data", hasProperty("metadata", hasProperty("page", is(page)))),
                        model().attribute("data", hasProperty("metadata", hasProperty("size", is(size))))
                );
    }

    @Test
    void findById() throws Exception {
        Order order = orderRepository.save(getOrder("test@gmail.com", "+373 60644566"));

        mockMvc.perform(get("/orders/{id}", order.getId()))
                .andExpectAll(
                        status().isOk(),
                        view().name("order/order"),
                        model().attributeExists("order", "referer"),
                        model().attribute("order", hasField("id")),
                        model().attribute("order", field("id", is(order.getId())))
                );
    }

    @Test
    void shouldThrowsResponseStatusExceptionWhenOrderNotFound() throws Exception {
        UUID orderId = UUID.randomUUID();

        mockMvc.perform(get("/orders/{id}", orderId))
                .andExpectAll(
                        status().isNotFound(),
                        result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException),
                        result -> assertEquals(HttpStatus.NOT_FOUND,
                                ((ResponseStatusException) Objects.requireNonNull(result.getResolvedException())).getStatusCode())
                );
    }

    @Test
    void delete() throws Exception {
        Order order = orderRepository.save(getOrder("test@gmail.com", "+373 60644566"));

        mockMvc.perform(post("/orders/{id}/delete", order.getId())
                        .with(csrf()))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/orders")
                );
    }

    @Test
    void shouldThrowResponseStatusExceptionWhenDelete() throws Exception {
        UUID orderId = UUID.randomUUID();

        mockMvc.perform(post("/orders/{id}/delete", orderId)
                        .with(csrf()))
                .andExpectAll(
                        status().isNotFound(),
                        result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException),
                        result -> assertEquals(HttpStatus.NOT_FOUND,
                                ((ResponseStatusException) Objects.requireNonNull(result.getResolvedException())).getStatusCode())
                );
    }

    private Order getOrder(String email, String phoneNumber) {
        User user = TestObjectsUtils.getUser(email);
        UserDetail userDetail = TestObjectsUtils.getUserDetail(phoneNumber);
        Apartment apartment = TestObjectsUtils.getApartment();
        Order order = TestObjectsUtils.getOrder();
        user.add(userDetail);
        session.persist(user);
        session.persist(apartment);
        order.add(user);
        order.add(apartment);
        session.flush();
        return order;
    }
}