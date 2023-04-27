package com.example.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable()).authorizeRequests(auth -> {
            auth.requestMatchers(HttpMethod.GET, "/v1/bookstore/books").hasAnyRole("USER", "ADMIN");
            auth.requestMatchers(HttpMethod.POST, "/v1/bookstore/books").hasAnyRole("USER", "ADMIN");
            auth.requestMatchers(HttpMethod.PUT, "/v1/bookstore/books/**").hasAnyRole("USER", "ADMIN");
            auth.requestMatchers(HttpMethod.DELETE, "/v1/bookstore/books/**").hasRole("ADMIN");
        }).httpBasic(Customizer.withDefaults()).build();
    }

    @Bean
    public InMemoryUserDetailsManager users() {
        // hardcoded users for testing
        UserDetails user = User.withDefaultPasswordEncoder().username("user").password("password").roles("USER")
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder().username("admin").password("password").roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}
