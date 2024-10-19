package com.bolun.hotel.integration;

import com.bolun.hotel.integration.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Proxy;

public abstract class IntegrationTestBase {

    private static SessionFactory sessionFactory;
    protected static Session session;

    @BeforeAll
    static void createSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
    }

    @AfterAll
    static void closeSessionFactory() {
        sessionFactory.close();
    }

    @BeforeEach
    void openSession() {
        session.getTransaction().begin();
    }

    @AfterEach
    void closeSession() {
        session.getTransaction().rollback();
    }
}

