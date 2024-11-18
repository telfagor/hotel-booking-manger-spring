package com.bolun.hotel.repository;

import com.bolun.hotel.entity.Apartment;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, UUID>,
        QuerydslPredicateExecutor<Apartment> {
    @Override
    Page<Apartment> findAll(Predicate predicate, Pageable pageable);
}
