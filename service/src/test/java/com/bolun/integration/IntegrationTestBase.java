package com.bolun.integration;

import com.bolun.hotel.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class IntegrationTestBase {

    private static SessionFactory sessionFactory;
    protected Session session;

    @BeforeAll
    static void createSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @AfterAll
    static void closeSessionFactory() {
        sessionFactory.close();
    }

    @BeforeEach
    void openSession() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @AfterEach
    void closeSession() {
        session.getTransaction().rollback();
        session.close();
    }
}

