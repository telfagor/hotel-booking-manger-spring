package com.bolun.hotel.repository;

import com.bolun.hotel.entity.Order;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>,
        FilterOrderRepository {

    @Query("SELECT o.user.id FROM Order o WHERE o.id = :orderId")
    Optional<UUID> findUserIdByOrderId(@Param("orderId") UUID orderId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Order o WHERE o.id = :id AND o.deleted = false")
    Optional<Order> findActiveByIdWithLock(UUID id);

    @Query("SELECT o FROM Order o WHERE o.id = :id AND o.deleted = false")
    Optional<Order> findActiveById(UUID id);
}

