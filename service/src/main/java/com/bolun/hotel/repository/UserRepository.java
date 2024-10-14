package com.bolun.hotel.repository;

import com.bolun.hotel.entity.User;
import org.hibernate.Session;

import java.util.UUID;

public class UserRepository extends AbstractRepository<UUID, User> {

    public UserRepository(Session session) {
        super(session, User.class);
    }
}

