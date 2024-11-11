package com.bolun.hotel.service;

import com.bolun.hotel.dto.UserCreateEditDto;
import com.bolun.hotel.dto.UserReadDto;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.integration.util.TestObjectsUtils;
import com.bolun.hotel.mapper.UserCreateEditMapper;
import com.bolun.hotel.mapper.UserReadMapper;
import com.bolun.hotel.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserReadMapper userReadMapper;

    @Mock
    private UserCreateEditMapper userCreateEditMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void findAll() {
        User user1 = TestObjectsUtils.getUser("test@gmail.com");
        User user2 = TestObjectsUtils.getUser("test2@gmail.com");
        UserReadDto userReadDto1 = TestObjectsUtils.getGetUserReadDto1();
        UserReadDto userReadDto2 = TestObjectsUtils.getGetUserReadDto2();
        List<User> users = List.of(user1, user2);

        doReturn(users).when(userRepository).findAll();

        doReturn(userReadDto1).when(userReadMapper).mapFrom(user1);
        doReturn(userReadDto2).when(userReadMapper).mapFrom(user2);

        List<UserReadDto> actualResult = userService.findAll();

        assertEquals(2, actualResult.size());
        assertEquals(userReadDto1, actualResult.get(0));
        assertEquals(userReadDto2, actualResult.get(1));

        verify(userRepository).findAll();
        verify(userReadMapper).mapFrom(user1);
        verify(userReadMapper).mapFrom(user2);
    }

    @Test
    void shouldFindById() {
        User user = TestObjectsUtils.getUser("test@gmail.com");
        Optional<User> maybeUser = Optional.of(user);
        UserReadDto userReadDto = TestObjectsUtils.getGetUserReadDto1();

        doReturn(maybeUser).when(userRepository).findById(user.getId());
        doReturn(userReadDto).when(userReadMapper).mapFrom(user);

        Optional<UserReadDto> actualResult = userService.findById(user.getId());

        assertThat(actualResult)
                .isPresent()
                .contains(userReadDto);

        verify(userRepository).findById(user.getId());
        verify(userReadMapper).mapFrom(user);
    }

    @Test
    void shouldNotFindById() {
        UUID fakeId = UUID.randomUUID();

        doReturn(Optional.empty()).when(userRepository).findById(fakeId);

        Optional<UserReadDto> actualResult = userService.findById(fakeId);

        assertThat(actualResult).isEmpty();

        verify(userRepository).findById(fakeId);
        verifyNoInteractions(userCreateEditMapper);
    }

    @Test
    void create() {
        User user = TestObjectsUtils.getUser("test@gmail.com");
        UserCreateEditDto userCreateEditDto = TestObjectsUtils.getUserCreateEditDto();

        UserReadDto userReadDto = TestObjectsUtils.getGetUserReadDto2();

        doReturn(user).when(userCreateEditMapper).mapFrom(userCreateEditDto);
        doReturn(user).when(userRepository).save(user);
        doReturn(userReadDto).when(userReadMapper).mapFrom(user);

        UserReadDto actualResult = userService.create(userCreateEditDto);

        assertEquals(userReadDto, actualResult);

        verify(userCreateEditMapper).mapFrom(userCreateEditDto);
        verify(userRepository).save(user);
        verify(userReadMapper).mapFrom(user);
    }

    @Test
    void shouldThrowAnExceptionWhenCreate() {
        UserCreateEditDto userCreateEditDto = TestObjectsUtils.getUserCreateEditDto();

        doReturn(null).when(userCreateEditMapper).mapFrom(userCreateEditDto);

        assertThrows(NoSuchElementException.class, () -> userService.create(userCreateEditDto));

        verifyNoInteractions(userRepository);
        verifyNoInteractions(userReadMapper);
    }

    @Test
    void update() {
        User user = TestObjectsUtils.getUser("test@gmail.com");

        UserCreateEditDto userCreateEditDto = TestObjectsUtils.getUserCreateEditDto();

        UserReadDto userReadDto = TestObjectsUtils.getGetUserReadDto1();

        doReturn(Optional.of(user)).when(userRepository).findById(user.getId());
        doReturn(user).when(userCreateEditMapper).mapFrom(userCreateEditDto, user);
        doReturn(user).when(userRepository).saveAndFlush(user);
        doReturn(userReadDto).when(userReadMapper).mapFrom(user);

        Optional<UserReadDto> actualResult = userService.update(user.getId(), userCreateEditDto);

        assertThat(actualResult)
                .isPresent()
                .contains(userReadDto);

        verify(userRepository).findById(user.getId());
        verify(userRepository).saveAndFlush(user);
        verify(userReadMapper).mapFrom(user);
    }

    @Test
    void delete() {
        User user = TestObjectsUtils.getUser("test@gmail.com");

        doReturn(Optional.of(user)).when(userRepository).findById(user.getId());

        boolean actualResult = userService.delete(user.getId());

        assertTrue(actualResult);
        verify(userRepository).findById(user.getId());
        verify(userRepository).delete(user);
    }

    @Test
    void shouldNotDelete() {
        User user = TestObjectsUtils.getUser("test@gmail.com");

        doReturn(Optional.empty()).when(userRepository).findById(user.getId());

        boolean actualResult = userService.delete(user.getId());

        assertFalse(actualResult);

        verify(userRepository).findById(user.getId());
        verify(userRepository, never()).delete(user);
    }
}