package com.bolun.hotel.repository;

import com.bolun.hotel.entity.UserDetail;
import org.hibernate.Session;

import java.util.UUID;

public class UserDetailRepository extends AbstractRepository<UUID, UserDetail> {

    public UserDetailRepository(Session session) {
        super(session, UserDetail.class);
    }
}

