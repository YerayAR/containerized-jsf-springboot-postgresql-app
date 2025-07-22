package com.example.backend.config;

import com.example.backend.security.JwtAuthenticationFilter;
import com.example.backend.service.UserService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                    // Public endpoints
                    .antMatchers("/api/auth/login").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/products", "/api/products/**").permitAll()
                    // Swagger UI and API documentation
                    .antMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                    // Admin only endpoints
                    .antMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN")
                    .antMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.PATCH, "/api/products/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
                    // All other requests need authentication
                    .anyRequest().authenticated();

        // Enable JWT filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
