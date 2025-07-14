package com.example.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    private final ProductRepository repository;

    public DataLoader(ProductRepository repository) {
        this.repository = repository;
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
    }
}
