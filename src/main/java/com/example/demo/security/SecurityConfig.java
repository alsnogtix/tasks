package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig: Central configuration for Spring Security.
 * Sets up JWT filters, authentication providers, and authorization rules.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    /**
     * Defines the AuthenticationManager, which is used to process login credentials.
     * This relies on Spring's built-in AuthenticationConfiguration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Defines the DaoAuthenticationProvider, which handles user lookup and password checking.
     * Spring will automatically link this provider to the AuthenticationManager.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
        UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder) {
        
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // Uses our custom UserService (which implements UserDetailsService)
        authProvider.setUserDetailsService(userDetailsService); 
        authProvider.setPasswordEncoder(passwordEncoder);
        
        return authProvider;
    }

    /**
     * Defines the core security filter chain, setting up the protection rules.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Disable CSRF (Crucial for Stateless REST APIs)
            .csrf(AbstractHttpConfigurer::disable)
            
            // 2. Set session management as STATELESS (Crucial for JWT)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // 3. Define the authorization rules for HTTP requests
            .authorizeHttpRequests(authorize -> authorize
                // Allow public access to registration and login endpoints
                .requestMatchers("/auth/register", "/auth/login").permitAll()
                
                // Require authentication (a token) for all /tasks endpoints
                .requestMatchers("/tasks/**").authenticated()
                
                // Require authentication for any other request
                .anyRequest().authenticated()
            )
            
            // 4. Add JWT filter before Spring's default authentication filter
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }
}
