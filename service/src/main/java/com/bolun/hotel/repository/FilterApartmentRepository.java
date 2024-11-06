package com.bolun.hotel.repository;

import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.util.ApartmentFilter;

import java.util.List;

public interface FilterApartmentRepository {

    List<Apartment> findAll(ApartmentFilter filter);
}
