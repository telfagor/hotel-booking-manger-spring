package com.bolun.hotel.repository;

import com.bolun.hotel.dto.filters.ApartmentFilter;
import com.bolun.hotel.entity.Apartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilterApartmentRepository {

    Page<Apartment> findAll(ApartmentFilter filter, Pageable pageable);
}
