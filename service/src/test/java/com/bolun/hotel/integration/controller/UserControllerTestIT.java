package com.bolun.hotel.integration.controller;

import com.bolun.hotel.dto.UserReadDto;
import com.bolun.hotel.entity.enums.Gender;
import com.bolun.hotel.entity.enums.Role;
import com.bolun.hotel.integration.IntegrationTestBase;
import com.bolun.hotel.integration.util.TestObjectsUtils;
import com.bolun.hotel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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
class UserControllerTestIT extends IntegrationTestBase {

    private final MockMvc mockMvc;
    private final UserService userService;

    @Test
    void findAll() throws Exception {
        userService.create(TestObjectsUtils.getUserCreateEditDto("test1@gmail.com", "+373 6711245"));
        userService.create(TestObjectsUtils.getUserCreateEditDto("test2@gmail.com", "+373 6711246"));
        session.flush();
        int page = 0;
        int size = 2;

        mockMvc.perform(get("/users")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpectAll(
                        status().isOk(),
                        view().name("user/users"),
                        model().attributeExists("data", "filter", "sortOptions", "selectedSort", "baseUrl"),
                        model().attribute("data", hasProperty("content", hasSize(size))),
                        model().attribute("data", hasProperty("metadata", hasProperty("page", is(page)))),
                        model().attribute("data", hasProperty("metadata", hasProperty("size", is(size))))
                );
    }

    @Test
    void findById() throws Exception {
        UserReadDto userReadDto = userService.create(TestObjectsUtils.getUserCreateEditDto("test2@gmail.com"));
        session.flush();

        mockMvc.perform(get("/users/" + userReadDto.id()))
                .andExpectAll(
                        status().isOk(),
                        view().name("user/user"),
                        model().attributeExists("user", "data", "orderStatuses", "sortOptions", "selectedSort",
                                "filter", "baseUrl"),
                        model().attribute("user", hasField("id")),
                        model().attribute("user", field("id", is(userReadDto.id())))
                );
    }

    @Test
    void shouldThrowsResponseStatusExceptionWhenApartmentNotFound() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(get("/users/" + userId))
                .andExpectAll(
                        status().isNotFound(),
                        result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException),
                        result -> assertEquals(HttpStatus.NOT_FOUND,
                                ((ResponseStatusException) Objects.requireNonNull(result.getResolvedException())).getStatusCode())
                );
    }

    @Test
    void registration() throws Exception {
        mockMvc.perform(post("/users")
                        .param("firstName", "Nikolay")
                        .param("lastName", "Negurita")
                        .param("email", "negurita@gmail.com")
                        .param("rawPassword", "123")
                        .param("confirmPassword", "123")
                        .param("role", Role.ADMIN.name())
                        .param("gender", Gender.MALE.name())
                        .with(csrf()))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/login")
                );
    }

    @Test
    void shouldThrowResponseStatusExceptionWhenDelete() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.post("/users/{id}/delete", userId)
                        .with(csrf()))
                .andExpectAll(
                        status().isNotFound(),
                        result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException),
                        result -> assertEquals(HttpStatus.NOT_FOUND,
                                ((ResponseStatusException) Objects.requireNonNull(result.getResolvedException())).getStatusCode())
                );
    }
}