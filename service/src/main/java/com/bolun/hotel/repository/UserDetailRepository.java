package com.bolun.hotel.repository;

import com.bolun.hotel.entity.UserDetail;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class UserDetailRepository extends AbstractRepository<UUID, UserDetail> {

    public UserDetailRepository(EntityManager entityManager) {
        super(entityManager, UserDetail.class);
    }
}

