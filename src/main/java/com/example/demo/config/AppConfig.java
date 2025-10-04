package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * AppConfig: General application configuration to host beans that are widely used
 * and should not be placed in security-specific configuration (like PasswordEncoder).
 */
@Configuration
public class AppConfig {

    /**
     * Defines the BCryptPasswordEncoder bean for password hashing.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
