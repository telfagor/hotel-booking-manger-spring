package com.bolun.hotel.repository;

import com.bolun.hotel.entity.Order;
import jakarta.persistence.EntityManager;

import java.util.UUID;

public class OrderRepository extends AbstractRepository<UUID, Order> {

    public OrderRepository(EntityManager entityManager) {
        super(entityManager, Order.class);
    }
}

