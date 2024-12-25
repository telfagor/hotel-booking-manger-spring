package com.bolun.hotel.repository;

import com.bolun.hotel.entity.UserDetail;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ud FROM UserDetail ud WHERE ud.user.id = :userId")
    Optional<UserDetail> findActiveByUserIdWithLock(UUID userId);

    @Query("SELECT ud FROM UserDetail ud WHERE ud.user.id = :userId")
    Optional<UserDetail> findActiveByUserId(UUID userId);

    Optional<UserDetail> findByPhoneNumber(String phoneNumber);
}

