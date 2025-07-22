package com.example.backend;

import com.example.backend.model.Product;
import com.example.backend.repository.ProductRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    void saveAndFind() {
        Product p = new Product();
        p.setName("Repo");
        p.setDescription("Repo product");
        p.setPrice(new BigDecimal("5"));
        p.setCategory("Repo");

        Product saved = repository.save(p);
        Assertions.assertTrue(repository.findById(saved.getId()).isPresent());
    }
}
