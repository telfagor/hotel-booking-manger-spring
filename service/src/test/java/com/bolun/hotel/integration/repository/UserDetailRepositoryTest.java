package com.bolun.hotel.integration.repository;

import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.UserDetail;
import com.bolun.hotel.integration.IntegrationTestBase;
import com.bolun.hotel.integration.util.TestObjectsUtils;
import com.bolun.hotel.repository.UserDetailRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserDetailRepositoryTest extends IntegrationTestBase {

    private UserDetailRepository userDetailRepository;

    @BeforeEach
    void init() {
        userDetailRepository = new UserDetailRepository(session);
    }

    @Test
    void insert() {
        UserDetail userDetail = getUserDetail("test@gmail.com","1");

        userDetailRepository.save(userDetail);

        assertNotNull(userDetail.getId());
    }

    @Test
    void update() {
        UserDetail userDetail = userDetailRepository.save(getUserDetail("test@gmail.com","1"));
        userDetail.setMoney(1500);
        userDetail.setBirthdate(LocalDate.now().minusYears(10));

        userDetailRepository.update(userDetail);
        session.clear();
        Optional<UserDetail> actualUserDetail = userDetailRepository.findById(userDetail.getId());

        assertThat(actualUserDetail)
                .isPresent()
                .contains(userDetail);
    }

    @Test
    void shouldFindById() {
        UserDetail userDetail = userDetailRepository.save(getUserDetail("test@gmail.com","1"));
        session.clear();

        Optional<UserDetail> actualUserDetail = userDetailRepository.findById(userDetail.getId());

        assertThat(actualUserDetail)
                .isPresent()
                .contains(userDetail);
    }

    @Test
    void shouldNotFindById() {
        UUID fakeId = UUID.randomUUID();

        Optional<UserDetail> actualUserDetail = userDetailRepository.findById(fakeId);

        assertThat(actualUserDetail).isEmpty();
    }

    @Test
    void findAll() {
        UserDetail userDetail1 = userDetailRepository.save(getUserDetail("test@gmail1.com","1"));
        UserDetail userDetail2 = userDetailRepository.save(getUserDetail("test@gmail2.com","2"));
        UserDetail userDetail3 = userDetailRepository.save(getUserDetail("test@gmail3.com","3"));

        List<UserDetail> actualUserDetails = userDetailRepository.findAll();

        List<String> phoneNumbers = actualUserDetails.stream()
                .map(UserDetail::getPhoneNumber)
                .toList();
        assertThat(phoneNumbers).containsExactlyInAnyOrder(userDetail1.getPhoneNumber(),
                userDetail2.getPhoneNumber(), userDetail3.getPhoneNumber());
    }

    @Test
    void delete() {
        UserDetail userDetail = userDetailRepository.save(getUserDetail("test@gmail.com","1"));

        userDetailRepository.delete(userDetail);
        Optional<UserDetail> actualUserDetail = userDetailRepository.findById(userDetail.getId());

        assertThat(actualUserDetail).isEmpty();
    }

    @NotNull
    private UserDetail getUserDetail(String email, String phoneNumber) {
        User user = TestObjectsUtils.getUser(email);
        UserDetail userDetail = TestObjectsUtils.getUserDetail(phoneNumber);
        user.add(userDetail);
        session.persist(user);
        session.flush();
        return userDetail;
    }
}
