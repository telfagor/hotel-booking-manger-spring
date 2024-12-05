package com.bolun.hotel.repository;

import com.bolun.hotel.dto.filters.OrderFilter;
import com.bolun.hotel.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilterOrderRepository {

    Page<Order> findAll(OrderFilter filter, Pageable pageable);
}
