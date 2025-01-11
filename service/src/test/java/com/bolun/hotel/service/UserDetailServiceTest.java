package com.bolun.hotel.service;

import com.bolun.hotel.dto.UserDetailCreateEditDto;
import com.bolun.hotel.dto.UserDetailReadDto;
import com.bolun.hotel.entity.UserDetail;
import com.bolun.hotel.integration.util.TestObjectsUtils;
import com.bolun.hotel.mapper.UserDetailCreateEditMapper;
import com.bolun.hotel.mapper.UserDetailReadMapper;
import com.bolun.hotel.repository.UserDetailRepository;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceTest {

    @Mock
    private UserDetailRepository userDetailRepository;

    @Mock
    private UserDetailCreateEditMapper userDetailCreateEditMapper;

    @Mock
    private UserDetailReadMapper userDetailReadMapper;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private UserDetailService userDetailService;

    @Test
    void create() {
        UserDetailService spyUserDetailService = Mockito.spy(userDetailService);
        MultipartFile mockPhoto = new MockMultipartFile("image.png", new byte[0]);
        UserDetailCreateEditDto userDetailCreateEditDto = TestObjectsUtils.getUserDetailCreateEditDto(mockPhoto);
        UserDetail userDetail = new UserDetail();
        UserDetailReadDto userDetailReadDto = TestObjectsUtils.getUserDetailReadDto();
        doNothing().when(spyUserDetailService).uploadImage(mockPhoto);
        doReturn(userDetail).when(userDetailCreateEditMapper).mapFrom(userDetailCreateEditDto);
        doReturn(userDetail).when(userDetailRepository).save(userDetail);
        doReturn(userDetailReadDto).when(userDetailReadMapper).mapFrom(userDetail);

        UserDetailReadDto actualResult = spyUserDetailService.create(userDetailCreateEditDto);

        assertThat(actualResult).isEqualTo(userDetailReadDto);
        verify(spyUserDetailService).uploadImage(mockPhoto);
        verify(userDetailCreateEditMapper).mapFrom(userDetailCreateEditDto);
        verify(userDetailRepository).save(userDetail);
        verify(userDetailReadMapper).mapFrom(userDetail);
    }

    @Test
    void shouldThrowAnExceptionWhenCreate() {
        MultipartFile mockPhoto = new MockMultipartFile("image.png", new byte[0]);
        UserDetailCreateEditDto userDetailCreateEditDto = TestObjectsUtils.getUserDetailCreateEditDto(mockPhoto);
        doReturn(null).when(userDetailCreateEditMapper).mapFrom(userDetailCreateEditDto);

        assertThrows(NoSuchElementException.class, () -> userDetailService.create(userDetailCreateEditDto));

        verifyNoInteractions(userDetailRepository);
        verifyNoInteractions(userDetailReadMapper);
    }

    @Test
    void update() {
        UUID userId = UUID.randomUUID();
        UserDetailService spyUserDetailService = Mockito.spy(userDetailService);
        MultipartFile mockPhoto = new MockMultipartFile("image.png", new byte[0]);
        UserDetailCreateEditDto userDetailCreateEditDto = TestObjectsUtils.getUserDetailCreateEditDto(mockPhoto);
        UserDetailReadDto userDetailReadDto = TestObjectsUtils.getUserDetailReadDto();
        UserDetail userDetail = new UserDetail();
        doReturn(Optional.of(userDetail)).when(userDetailRepository).findActiveByUserIdWithLock(userId);
        doNothing().when(spyUserDetailService).uploadImage(mockPhoto);
        doReturn(userDetail).when(userDetailCreateEditMapper).mapFrom(userDetailCreateEditDto, userDetail);
        doReturn(userDetail).when(userDetailRepository).saveAndFlush(userDetail);
        doReturn(userDetailReadDto).when(userDetailReadMapper).mapFrom(userDetail);

        Optional<UserDetailReadDto> actualResult = spyUserDetailService.update(userId, userDetailCreateEditDto);

        assertThat(actualResult)
                .isPresent()
                .contains(userDetailReadDto);
        verify(userDetailRepository).findActiveByUserIdWithLock(userId);
        verify(spyUserDetailService).uploadImage(mockPhoto);
        verify(userDetailCreateEditMapper).mapFrom(userDetailCreateEditDto, userDetail);
        verify(userDetailRepository).saveAndFlush(userDetail);
        verify(userDetailReadMapper).mapFrom(userDetail);
    }

    @Test
    void shouldReturnAnEmptyOptionalWhenUpdateFails() {
        UUID userId = UUID.randomUUID();
        UserDetail userDetail = new UserDetail();
        MultipartFile mockPhoto = new MockMultipartFile("image.png", new byte[0]);
        UserDetailReadDto userDetailReadDto = TestObjectsUtils.getUserDetailReadDto();
        UserDetailCreateEditDto userDetailCreateEditDto = TestObjectsUtils.getUserDetailCreateEditDto(mockPhoto);
        doReturn(Optional.empty()).when(userDetailRepository).findActiveByUserIdWithLock(userId);

        Optional<UserDetailReadDto> actualResult = userDetailService.update(userId, userDetailCreateEditDto);

        assertThat(actualResult).isEmpty();
        verify(userDetailRepository).findActiveByUserIdWithLock(userId);
        verifyNoInteractions(userDetailCreateEditMapper);
        verify(userDetailRepository, never()).saveAndFlush(userDetail);
        verify(userDetailReadMapper, never()).mapFrom(userDetail);
    }

    @Test
    void uploadPhoto() throws Exception {
        MultipartFile mockPhoto = Mockito.mock(MultipartFile.class);
        @Cleanup InputStream mockInputStream = Mockito.mock(InputStream.class);
        doReturn(false).when(mockPhoto).isEmpty();
        doReturn("image.png").when(mockPhoto).getOriginalFilename();
        doReturn(mockInputStream).when(mockPhoto).getInputStream();

        userDetailService.uploadImage(mockPhoto);

        verify(imageService).upload("image.png", mockInputStream);
        verify(mockPhoto).isEmpty();
        verify(mockPhoto).getOriginalFilename();
        verify(mockPhoto).getInputStream();
    }

    @Test
    void shouldNotUploadImageWhenPhotoIsEmpty() throws Exception {
        MultipartFile mockPhoto = Mockito.mock(MultipartFile.class);
        doReturn(true).when(mockPhoto).isEmpty();

        userDetailService.uploadImage(mockPhoto);

        verify(mockPhoto).isEmpty();
        verify(mockPhoto, never()).getOriginalFilename();
        verifyNoInteractions(imageService);
    }

    @Test
    void shouldThrowExceptionWhenInputStreamFails() throws Exception {
        MultipartFile mockPhoto = Mockito.mock(MultipartFile.class);
        doReturn(false).when(mockPhoto).isEmpty();
        doReturn("image.png").when(mockPhoto).getOriginalFilename();
        doThrow(new IOException("Stream Error")).when(mockPhoto).getInputStream();

        assertThrows(IOException.class, () -> userDetailService.uploadImage(mockPhoto));
        verify(mockPhoto).isEmpty();
        verify(mockPhoto).getInputStream();
        verifyNoInteractions(imageService);
    }

    @Test
    void findByPhoneNumber() {
        String phoneNumber = "1";
        UserDetail userDetail = new UserDetail();
        UserDetailReadDto userReadDto = TestObjectsUtils.getUserDetailReadDto();
        doReturn(Optional.of(userDetail)).when(userDetailRepository).findByPhoneNumber(phoneNumber);
        doReturn(userReadDto).when(userDetailReadMapper).mapFrom(userDetail);

        Optional<UserDetailReadDto> actualResult = userDetailService.findByPhoneNumber(phoneNumber);

        assertThat(actualResult)
                .isPresent()
                .contains(userReadDto);
        verify(userDetailRepository).findByPhoneNumber(phoneNumber);
        verify(userDetailReadMapper).mapFrom(userDetail);
    }

    @Test
    void shouldNotFindByPhoneNumber() {
        String phoneNumber = "1";
        doReturn(Optional.empty()).when(userDetailRepository).findByPhoneNumber(phoneNumber);

        Optional<UserDetailReadDto> actualResult = userDetailService.findByPhoneNumber(phoneNumber);

        assertThat(actualResult).isEmpty();
        verify(userDetailRepository).findByPhoneNumber(phoneNumber);
        verifyNoInteractions(userDetailReadMapper);
    }
}