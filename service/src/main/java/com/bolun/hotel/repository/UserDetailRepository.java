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
    @Query("SELECT ud FROM UserDetail ud JOIN User u ON ud.user.id = :userId AND u.deleted = false")
    Optional<UserDetail> findActiveByUserIdWithLock(UUID userId);

    Optional<UserDetail> findByPhoneNumber(String phoneNumber);
}

