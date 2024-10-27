package com.bolun.hotel.integration;

import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;

public abstract class IntegrationTestBase {
    @PersistenceContext
    protected Session session;
}

