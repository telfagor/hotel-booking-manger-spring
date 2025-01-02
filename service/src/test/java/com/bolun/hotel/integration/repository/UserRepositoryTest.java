package com.bolun.hotel.integration.repository;

import com.bolun.hotel.dto.filters.UserFilter;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.enums.Gender;
import com.bolun.hotel.entity.enums.Role;
import com.bolun.hotel.integration.IntegrationTestBase;
import com.bolun.hotel.integration.util.TestDataImporter;
import com.bolun.hotel.integration.util.TestObjectsUtils;
import com.bolun.hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
class UserRepositoryTest extends IntegrationTestBase {

    private final UserRepository userRepository;

    @Test
    void insert() {
        User user = TestObjectsUtils.getUser("test@gmail.com");

        User actualUser = userRepository.save(user);

        assertNotNull(actualUser.getId());
    }

    @Test
    void update() {
        User user = userRepository.save(TestObjectsUtils.getUser("test@gmail.com"));
        user.setFirstName("Andrei");
        user.setLastName("Bolun");

        userRepository.saveAndFlush(user);
        session.clear();
        Optional<User> actualUser = userRepository.findById(user.getId());

        assertThat(actualUser)
                .isPresent()
                .contains(user);
    }

    @Test
    void shouldFindById() {
        User user = userRepository.saveAndFlush(TestObjectsUtils.getUser("test@gmail.com"));
        session.clear();

        Optional<User> actualUser = userRepository.findById(user.getId());

        assertThat(actualUser)
                .isPresent()
                .contains(user);
    }

    @Test
    void shouldNotFindById() {
        UUID fakeId = UUID.randomUUID();

        Optional<User> actualUser = userRepository.findById(fakeId);

        assertThat(actualUser).isEmpty();
    }

    @MethodSource("getMethodArguments")
    @ParameterizedTest
    void findAll(UserFilter filter, long expectedUsersSize) {
        TestDataImporter.importData(session);
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);

        Page<User> actualResult = userRepository.findAll(filter, pageable);

        assertThat(actualResult.getTotalElements()).isEqualTo(expectedUsersSize);
    }

    static Stream<Arguments> getMethodArguments() {
        return Stream.of(
                Arguments.of(
                        new UserFilter(
                                "Andrei",
                                "Chirtoaca",
                                "andrei.chirtoaca@gmail.com",
                                Gender.MALE,
                                Role.ADMIN,
                                "1",
                                LocalDate.of(2005, 9, 19),
                                LocalDate.of(2005, 9, 19),
                                1500,
                                1500
                        ), 1L
                ),
                Arguments.of(
                        new UserFilter(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                1000,
                                1500
                        ), 6L
                ),
                Arguments.of(
                        new UserFilter(
                                null,
                                null,
                                "tudor",
                                null,
                                null,
                                null,
                                null,
                                null,
                                1000,
                                1500
                        ), 2L
                )
        );
    }

    @Test
    void delete() {
        User user = userRepository.save(TestObjectsUtils.getUser("test@gmail.com"));

        userRepository.delete(user);
        Optional<User> actualUser = userRepository.findById(user.getId());

        assertThat(actualUser).isEmpty();
    }
}
