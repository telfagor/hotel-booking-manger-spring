package com.bolun.hotel.integration.rest;

import com.bolun.hotel.dto.UserReadDto;
import com.bolun.hotel.integration.IntegrationTestBase;
import com.bolun.hotel.integration.util.TestObjectsUtils;
import com.bolun.hotel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserRestControllerIT extends IntegrationTestBase {

    private final UserService userService;
    private final MockMvc mockMvc;

    @Test
    void findAllWithPagination() throws Exception {
        userService.create(TestObjectsUtils.getUserCreateEditDto("test@gmail.com", "+373 60655422"));
        userService.create(TestObjectsUtils.getUserCreateEditDto("test2@gmail.com", "+373 60655423"));
        int page = 0;
        int size = 2;

        mockMvc.perform(get("/api/v1/users")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(size)))
                .andExpect(jsonPath("$.metadata.page", is(page)))
                .andExpect(jsonPath("$.metadata.size", is(size)));
    }

    @Test
    void findById() throws Exception {
        UserReadDto userReadDto = userService.create(TestObjectsUtils.getUserCreateEditDto());

        mockMvc.perform(get("/api/v1/users/{id}", userReadDto.id()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userReadDto.id().toString()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void shouldThrowsResponseStatusExceptionWhenOrderNotFound() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpectAll(
                        status().isNotFound(),
                        result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException),
                        result -> assertEquals(HttpStatus.NOT_FOUND,
                                ((ResponseStatusException) Objects.requireNonNull(result.getResolvedException())).getStatusCode())
                );
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "firstName": "John",
                                    "lastName": "Doe",
                                    "email": "john.doe@example.com",
                                    "gender": "MALE",
                                    "role": "USER",
                                    "phoneNumber": "+123456789",
                                    "birthdate": "1990-01-01",
                                    "photo": null,
                                    "money": 1000
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void delete() throws Exception {
        UserReadDto userReadDto = userService.create(TestObjectsUtils.getUserCreateEditDto());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{id}", userReadDto.id())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        Optional<UserReadDto> actualResult = userService.findById(userReadDto.id());
        assertThat(actualResult).isEmpty();
    }

    @Test
    void findAvatar() throws Exception {
        byte[] avatar = "mockAvatarData" .getBytes();
        MockMultipartFile photo = new MockMultipartFile("photo", "image.png", IMAGE_PNG_VALUE, avatar);
        UserReadDto userReadDto = userService.create(TestObjectsUtils.getUserCreateEditDto(photo));
        Optional<byte[]> expectedAvatar = userService.findAvatar(userReadDto.id());
        assertThat(expectedAvatar).isPresent();

        mockMvc.perform(get("/api/v1/users/{id}/avatar", userReadDto.id()))
                .andExpectAll(
                        status().isOk(),
                        content().bytes(expectedAvatar.get()),
                        content().contentType(APPLICATION_OCTET_STREAM)
                );
    }

    @Test
    void shouldThrowsNotFoundExceptionWhenAvatarNotFound() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(get("/api/v1/users/{id}/avatar", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldThrowResponseStatusExceptionWhenDelete() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{id}", userId)
                        .with(csrf()))
                .andExpectAll(
                        status().isNotFound(),
                        result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException),
                        result -> assertEquals(HttpStatus.NOT_FOUND,
                                ((ResponseStatusException) Objects.requireNonNull(result.getResolvedException())).getStatusCode())
                );
    }
}
