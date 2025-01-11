package com.bolun.hotel.integration.rest;

import com.bolun.hotel.dto.ApartmentCreateEditDto;
import com.bolun.hotel.dto.ApartmentReadDto;
import com.bolun.hotel.integration.IntegrationTestBase;
import com.bolun.hotel.integration.util.TestObjectsUtils;
import com.bolun.hotel.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class ApartmentRestControllerTestIT extends IntegrationTestBase {

    private final MockMvc mockMvc;
    private final ApartmentService apartmentService;

    @Test
    void findAvatar() throws Exception {
        byte[] avatar = "mockAvatarData".getBytes();
        MockMultipartFile mockPhoto = new MockMultipartFile("photo", "image.png", IMAGE_PNG_VALUE, avatar);
        ApartmentCreateEditDto apartmentCreateEditDto = TestObjectsUtils.getApartmentCreateEditDto(mockPhoto);
        ApartmentReadDto apartmentReadDto = apartmentService.create(apartmentCreateEditDto);
        Optional<byte[]> expectedPhoto = apartmentService.findPhoto(apartmentReadDto.id());

        assertThat(expectedPhoto).isPresent();
        mockMvc.perform(get("/api/v1/apartments/{id}/photo", apartmentReadDto.id()))
                .andExpectAll(
                        status().isOk(),
                        content().bytes(expectedPhoto.get()),
                        content().contentType(APPLICATION_OCTET_STREAM)
                );
        assertThat(expectedPhoto).contains(avatar);
    }

    @Test
    void shouldThrowsNotFoundExceptionWhenAvatarNotFound() throws Exception {
        UUID apartmentId = UUID.randomUUID();

        mockMvc.perform(get("/api/v1/apartments/{id}/photo", apartmentId))
                .andExpect(status().isNotFound());
        assertThat(apartmentService.findPhoto(apartmentId)).isEmpty();
    }
}