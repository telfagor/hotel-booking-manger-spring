package com.bolun.hotel.config;

import com.HotelRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static java.util.Optional.of;

@Configuration
@EnableEnversRepositories(basePackageClasses = HotelRunner.class)
@EnableJpaAuditing
public class AuditConfiguration {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> of("andrei");
    }
}
