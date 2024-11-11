package com.bolun.hotel.integration.controller;

import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.enums.Gender;
import com.bolun.hotel.integration.IntegrationTestBase;
import com.bolun.hotel.integration.util.TestObjectsUtils;
import com.bolun.hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserControllerIT extends IntegrationTestBase {

    private final UserRepository userRepository;
    private final MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        userRepository.saveAndFlush(TestObjectsUtils.getUser("test@gmail.com"));
        userRepository.saveAndFlush(TestObjectsUtils.getUser("test2@gmail.com"));
        userRepository.saveAndFlush(TestObjectsUtils.getUser("test3@gmail.com"));
        userRepository.saveAndFlush(TestObjectsUtils.getUser("test4@gmail.com"));

        mockMvc.perform(get("/users"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", hasSize(4)));
    }

    @Test
    void findById() throws Exception {
        User user = userRepository.saveAndFlush(TestObjectsUtils.getUser("test@gmail.com"));

        mockMvc.perform(get("/users/" + user.getId()))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        view().name("user/user"),
                        model().attributeExists("user")
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
    }

    @Test
    void delete() throws Exception {
        User user = userRepository.saveAndFlush(TestObjectsUtils.getUser("test@gmail.com"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + user.getId()))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/users")
                );

        assertFalse(userRepository.findById(user.getId()).isPresent());
    }
}
