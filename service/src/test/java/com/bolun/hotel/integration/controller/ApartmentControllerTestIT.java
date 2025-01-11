package com.bolun.hotel.integration.controller;

import com.bolun.hotel.dto.ApartmentCreateEditDto;
import com.bolun.hotel.dto.ApartmentReadDto;
import com.bolun.hotel.entity.enums.ApartmentType;
import com.bolun.hotel.integration.IntegrationTestBase;
import com.bolun.hotel.integration.util.TestObjectsUtils;
import com.bolun.hotel.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static rocks.cleancode.hamcrest.record.HasFieldMatcher.field;
import static rocks.cleancode.hamcrest.record.HasFieldMatcher.hasField;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class ApartmentControllerTestIT extends IntegrationTestBase {

    private final MockMvc mockMvc;
    private final ApartmentService apartmentService;

    @Test
    void findAll() throws Exception {
        MultipartFile photo = new MockMultipartFile("photo", "image.png", "image/png", new byte[0]);
        apartmentService.create(TestObjectsUtils.getApartmentCreateEditDto(photo));
        apartmentService.create(TestObjectsUtils.getApartmentCreateEditDto(photo));
        session.flush();
        int page = 0;
        int size = 2;

        mockMvc.perform(get("/apartments")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpectAll(
                        status().isOk(),
                        view().name("apartment/apartments"),
                        model().attributeExists("data", "filter", "sortOptions", "selectedSort", "baseUrl"),
                        model().attribute("data", hasProperty("content", hasSize(size))),
                        model().attribute("data", hasProperty("metadata", hasProperty("page", is(page)))),
                        model().attribute("data", hasProperty("metadata", hasProperty("size", is(size))))
                );
    }

    @Test
    void findById() throws Exception {
        MultipartFile photo = new MockMultipartFile("photo", "image.png", "image/png", new byte[0]);
        ApartmentReadDto apartmentReadDto = apartmentService.create(TestObjectsUtils.getApartmentCreateEditDto(photo));
        session.flush();

        mockMvc.perform(get("/apartments/{id}", apartmentReadDto.id()))
                .andExpectAll(
                        status().isOk(),
                        view().name("apartment/apartment"),
                        model().attributeExists("apartment"),
                        model().attribute("apartment", hasField("id")),
                        model().attribute("apartment", field("id", is(apartmentReadDto.id())))
                );
    }

    @Test
    void shouldThrowsResponseStatusExceptionWhenApartmentNotFound() throws Exception {
        UUID apartmentId = UUID.randomUUID();

        mockMvc.perform(get("/apartments/{id}", apartmentId))
                .andExpectAll(
                        status().isNotFound(),
                        result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException),
                        result -> assertEquals(HttpStatus.NOT_FOUND,
                                ((ResponseStatusException) Objects.requireNonNull(result.getResolvedException())).getStatusCode())
                );
    }

    @Test
    void create() throws Exception {
        MockMultipartFile mockPhoto = new MockMultipartFile("photo", "image.png", IMAGE_PNG_VALUE, new byte[0]);

        mockMvc.perform(multipart(POST, "/apartments")
                        .file(mockPhoto)
                        .param("rooms", "2")
                        .param("seats", "4")
                        .param("dailyCost", "40")
                        .param("apartmentType", ApartmentType.LUX.name())
                        .with(csrf()))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/apartments")
                );
    }

    @Test
    void update() throws Exception {
        MockMultipartFile mockPhoto = new MockMultipartFile("photo", "image.png", IMAGE_PNG_VALUE, new byte[0]);
        ApartmentCreateEditDto apartmentCreateEditDto = TestObjectsUtils.getApartmentCreateEditDto(mockPhoto);
        ApartmentReadDto apartmentReadDto = apartmentService.create(apartmentCreateEditDto);

        mockMvc.perform(multipart(POST, "/apartments/{id}/update", apartmentReadDto.id())
                        .file(mockPhoto)
                        .param("rooms", "2")
                        .param("seats", "4")
                        .param("dailyCost", "40")
                        .param("apartmentType", ApartmentType.LUX.name())
                        .with(csrf()))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/apartments")
                );
    }

    @Test
    void delete() throws Exception {
        MockMultipartFile mockPhoto = new MockMultipartFile("photo", "image.png", IMAGE_PNG_VALUE, new byte[0]);
        ApartmentCreateEditDto apartmentCreateEditDto = TestObjectsUtils.getApartmentCreateEditDto(mockPhoto);
        ApartmentReadDto apartmentReadDto = apartmentService.create(apartmentCreateEditDto);

        mockMvc.perform(post("/apartments/{id}/delete", apartmentReadDto.id())
                        .with(csrf()))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/apartments")
                );
    }

    @Test
    void shouldThrowResponseStatusExceptionWhenDelete() throws Exception {
        UUID apartmentId = UUID.randomUUID();

        mockMvc.perform(post("/apartments/{id}/delete",apartmentId)
                        .with(csrf()))
                .andExpectAll(
                        status().isNotFound(),
                        result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException),
                        result -> assertEquals(HttpStatus.NOT_FOUND,
                                ((ResponseStatusException) Objects.requireNonNull(result.getResolvedException())).getStatusCode())
                );
    }
}