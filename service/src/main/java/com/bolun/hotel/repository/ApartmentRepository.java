package com.bolun.hotel.repository;

import com.bolun.hotel.entity.Apartment;
import org.hibernate.Session;

import java.util.UUID;

public class ApartmentRepository extends AbstractRepository<UUID, Apartment> {

    public ApartmentRepository(Session session) {
        super(session, Apartment.class);
    }
}

