package com.bolun.hotel.integration;

import com.bolun.hotel.integration.config.IntegrationTestApplicationConfig;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public abstract class IntegrationTestBase {

    protected static AnnotationConfigApplicationContext applicationContext;
    protected Session session;

    @BeforeAll
    static void createApplicationContext() {
        applicationContext = new AnnotationConfigApplicationContext(IntegrationTestApplicationConfig.class);
    }

    @AfterAll
    static void closeApplicationContext() {
        applicationContext.close();
    }

    @BeforeEach
    void openSession() {
        session = applicationContext.getBean("session", Session.class);
        session.getTransaction().begin();
    }

    @AfterEach
    void closeSession() {
        session.getTransaction().rollback();
    }
}

