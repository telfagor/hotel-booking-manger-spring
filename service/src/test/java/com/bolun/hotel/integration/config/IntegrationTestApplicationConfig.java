package com.bolun.hotel.integration.config;

import com.bolun.hotel.config.SpringConfig;
import com.bolun.hotel.integration.util.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(SpringConfig.class)
public class IntegrationTestApplicationConfig {

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateTestUtil.buildSessionFactory();
    }
}
