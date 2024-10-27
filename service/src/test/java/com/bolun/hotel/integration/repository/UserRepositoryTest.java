package com.bolun.hotel.integration.repository;

import com.bolun.hotel.entity.User;
import com.bolun.hotel.integration.IntegrationTestBase;
import com.bolun.hotel.integration.annotation.IT;
import com.bolun.hotel.integration.util.TestObjectsUtils;
import com.bolun.hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@IT
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

        userRepository.update(user);
        session.clear();
        Optional<User> actualUser = userRepository.findById(user.getId());

        assertThat(actualUser)
                .isPresent()
                .contains(user);
    }

    @Test
    void shouldFindById() {
        User user = userRepository.save(TestObjectsUtils.getUser("test@gmail.com"));
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

    @Test
    void findAll() {
        User user1 = userRepository.save(TestObjectsUtils.getUser("test@gmail.com"));
        User user2 = userRepository.save(TestObjectsUtils.getUser("test2@gmail.com"));
        User user3 = userRepository.save(TestObjectsUtils.getUser("test3@gmail.com"));

        List<User> users = userRepository.findAll();

        List<String> emails = users.stream()
                .map(User::getEmail)
                .toList();
        assertThat(emails).containsExactlyInAnyOrder(user1.getEmail(), user2.getEmail(), user3.getEmail());
    }

    @Test
    void delete() {
        User user = userRepository.save(TestObjectsUtils.getUser("test@gmail.com"));

        userRepository.delete(user);
        Optional<User> actualUser = userRepository.findById(user.getId());

        assertThat(actualUser).isEmpty();
    }
}
