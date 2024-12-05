package com.bolun.hotel.config;

import com.HotelRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Configuration
@EnableEnversRepositories(basePackageClasses = HotelRunner.class)
@EnableJpaAuditing
public class AuditConfiguration {

    @Bean
    public AuditorAware<String> auditorAware() {
            return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                    .filter(Authentication::isAuthenticated)
                    .map(Authentication::getPrincipal)
                    .map(principal -> {
                        if (principal instanceof UserDetails) {
                            return ((UserDetails) principal).getUsername();
                        } else if (principal instanceof String) {
                            return (String) principal;
                        }
                        return null;
                    });
        }
}
