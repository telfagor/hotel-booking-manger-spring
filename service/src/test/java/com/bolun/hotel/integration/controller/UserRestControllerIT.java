package com.bolun.hotel.integration.controller;

import com.bolun.hotel.dto.UserReadDto;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.enums.Gender;
import com.bolun.hotel.integration.IntegrationTestBase;
import com.bolun.hotel.integration.util.TestObjectsUtils;
import com.bolun.hotel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserRestControllerIT extends IntegrationTestBase {

    private final UserService userService;
    private final MockMvc mockMvc;

    @Test
    void findAllWithPagination() throws Exception {
        userService.create(TestObjectsUtils.getUserCreateEditDto("test@gmail.com"));
        userService.create(TestObjectsUtils.getUserCreateEditDto("test2@gmail.com"));
        userService.create(TestObjectsUtils.getUserCreateEditDto("test3@gmail.com"));
        userService.create(TestObjectsUtils.getUserCreateEditDto("test4@gmail.com"));
        int page = 1;
        int size = 2;

        mockMvc.perform(get("/users")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(view().name("user/users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", hasProperty("content", hasSize(size))))
                .andExpect(model().attribute("users",
                        hasProperty("metadata", hasProperty("page", is(page)))))
                .andExpect(model().attribute("users",
                        hasProperty("metadata", hasProperty("size", is(size)))));
    }

    @Test
    void findById() throws Exception {
        UserReadDto userReadDto = userService.create(TestObjectsUtils.getUserCreateEditDto("test@gmail.com"));

        mockMvc.perform(get("/users/" + userReadDto.id()))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        view().name("user/user"),
                        model().attributeExists("user"),
                        model().attribute("user", equalTo(userReadDto))
                );
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/users")
                        .param("firstName", "Ion")
                        .param("lastName", "Covalenco")
                        .param("email", "covalenco@gmail.com")
                        .param("password", "123")
                        .param("confirmPassword", "123")
                        .param("gender", Gender.MALE.name())
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/users")
                );

        Optional<User> actualResult = session.createQuery("select u from User u where email = :email", User.class)
                .setParameter("email", "covalenco@gmail.com")
                .uniqueResultOptional();
        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getEmail()).isEqualTo("covalenco@gmail.com");
    }

    @Test
    void delete() throws Exception {
        UserReadDto userReadDto = userService.create(TestObjectsUtils.getUserCreateEditDto("test@gmail.com"));

        mockMvc.perform(post("/users/" + userReadDto.id() + "/delete"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/users")
                );

        Optional<UserReadDto> actualResult = userService.findById(userReadDto.id());
        assertThat(actualResult).isEmpty();
    }
}
