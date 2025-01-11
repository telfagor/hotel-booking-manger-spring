package com.bolun.hotel.integration;

import com.bolun.hotel.integration.annotation.IT;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@WithMockUser(username = "bolun.andrei.ivan@gmail.com", password = "123", authorities = {"ADMIN", "USER"})
@IT
public abstract class IntegrationTestBase {

    protected static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:17")
            .withInitScript("init.sql");

    @BeforeAll
    static void runContainer() {
        container.start();
    }

    @PersistenceContext
    protected Session session;

    @DynamicPropertySource
    static void configureTestcontainers(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }
}

