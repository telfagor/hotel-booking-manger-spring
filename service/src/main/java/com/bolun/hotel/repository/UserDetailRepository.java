package com.bolun.hotel.repository;

import com.bolun.hotel.entity.UserDetail;
import jakarta.persistence.EntityManager;

import java.util.UUID;

public class UserDetailRepository extends AbstractRepository<UUID, UserDetail> {

    public UserDetailRepository(EntityManager entityManager) {
        super(entityManager, UserDetail.class);
    }
}

