package com.csq.fweb.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
               .csrf().disable()
               .authorizeRequests()
               .antMatchers("/auth/register", "/auth/login", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
               .anyRequest().authenticated()
               .and()
               .httpBasic();

        return http.build();
    }
}    