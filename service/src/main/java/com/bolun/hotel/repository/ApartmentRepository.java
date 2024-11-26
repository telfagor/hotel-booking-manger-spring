package com.bolun.hotel.repository;

import com.bolun.hotel.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, UUID>,
        FilterApartmentRepository {

}
