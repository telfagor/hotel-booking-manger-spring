package com.bolun.hotel.config;

import com.bolun.hotel.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
@ComponentScan("com.bolun.hotel")
public class SpringConfig {

    @Bean(destroyMethod = "close")
    public SessionFactory sessionFactory() {
        return HibernateUtil.buildSessionFactory();
    }

    @Bean
    public Session session(SessionFactory sessionFactory) {
        return (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[] {Session.class},
                (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
    }
}
