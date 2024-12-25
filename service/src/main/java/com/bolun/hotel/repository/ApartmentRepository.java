package com.bolun.hotel.repository;

import com.bolun.hotel.entity.Apartment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, UUID>,
        FilterApartmentRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Apartment a WHERE a.id = :id AND a.deleted = false")
    Optional<Apartment> findActiveByIdWithLock(UUID id);

    @Query("SELECT a FROM Apartment a WHERE a.id = :id AND a.deleted = false")
    Optional<Apartment> findActiveById(UUID id);
}
