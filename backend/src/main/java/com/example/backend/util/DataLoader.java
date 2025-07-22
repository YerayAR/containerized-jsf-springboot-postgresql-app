package com.example.backend.util;

import com.example.backend.model.Product;
import com.example.backend.model.User;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    private final ProductRepository repository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(ProductRepository repository,
                      UserRepository userRepository,
                      PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            repository.saveAll(List.of(
                new Product(null, "Laptop", "High-end laptop",
                        new BigDecimal("1500.00"), "Electronics", true),
                new Product(null, "Spring Boot Guide", "Book",
                        new BigDecimal("39.99"), "Books", true)
            ));
        }

        if (userRepository.count() == 0) {
            User admin = new User("admin",
                    passwordEncoder.encode("password"),
                    "ADMIN");
            userRepository.save(admin);
        }
    }
}
