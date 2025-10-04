package com.example.demo.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    @Autowired
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }

    /**
     * Defines the AuthenticationManager, which is used to process login credentials.
     */
    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        // Use DaoAuthenticationProvider (standard for username/password login)
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        
        // Tells the provider to use our custom UserDetailsService (defined below)
        authProvider.setUserDetailsService(userDetailsService());
        
        // Tells the provider to use our BCrypt hash encoder
        authProvider.setPasswordEncoder(passwordEncoder);
        
        return new ProviderManager(authProvider);
    }

    /**
     * Defines the UserDetailsService, which finds user details from the database.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        // This lambda function tells Spring Security how to use your existing UserService
        return username -> userService.findByUsername(username).map(user ->
            new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                java.util.Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority(user.getRole()))
            )
        ).orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found: " + username));
    }

    /**
     * Defines the core security filter chain, setting up the protection rules.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Disable CSRF and CORS (CORS is handled by the @CrossOrigin annotation on controllers)
            .csrf(AbstractHttpConfigurer::disable)
            
            // 2. Define session management as STATELESS (Crucial for JWT)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 3. Define the authorization rules for HTTP requests
            .authorizeHttpRequests(authorize -> authorize
                // Allow public access to the registration endpoint
                .requestMatchers("/auth/register").permitAll()
                
                // Allow public access to the login endpoint (we will add this later)
                .requestMatchers("/auth/login").permitAll()
                
                // Require authentication (a token) for all /tasks endpoints
                .requestMatchers("/tasks/**").authenticated()
                
                // Require authentication for any other request
                .anyRequest().authenticated()
            );
            
        return http.build();
    }

}
