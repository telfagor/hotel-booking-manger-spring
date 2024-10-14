package com.bolun.hotel.repository;

import com.bolun.hotel.entity.Order;
import org.hibernate.Session;

import java.util.UUID;

public class OrderRepository extends AbstractRepository<UUID, Order> {

    public OrderRepository(Session session) {
        super(session, Order.class);
    }
}

