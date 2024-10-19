package com.bolun.hotel.repository;

import com.bolun.hotel.entity.User;
import jakarta.persistence.EntityManager;

import java.util.UUID;

public class UserRepository extends AbstractRepository<UUID, User> {

    public UserRepository(EntityManager entityManager) {
        super(entityManager, User.class);
    }
}

