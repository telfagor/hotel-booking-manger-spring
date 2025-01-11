package com.bolun.hotel.integration.controller;

import com.bolun.hotel.dto.UserDetailCreateEditDto;
import com.bolun.hotel.integration.IntegrationTestBase;
import com.bolun.hotel.integration.util.TestObjectsUtils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserDetailControllerTestIT extends IntegrationTestBase {

    private final MockMvc mockMvc;

    @Test
    void create() throws Exception {
        MockMultipartFile mockPhoto = new MockMultipartFile("photo", "image.png", IMAGE_PNG_VALUE, new byte[0]);
        UserDetailCreateEditDto userDetailDto = TestObjectsUtils.getUserDetailCreateEditDto(mockPhoto);

        mockMvc.perform(multipart(POST, "/userDetails")
                        .file(mockPhoto)
                        .flashAttr("userDetail", userDetailDto)
                        .with(csrf()))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/apartments")
                );
    }
}