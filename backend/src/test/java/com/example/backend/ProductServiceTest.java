package com.example.backend;

import com.example.backend.model.Product;
import com.example.backend.repository.ProductRepository;
import com.example.backend.service.ProductService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService service;

    @Test
    void createProduct_shouldPersist() {
        Product p = new Product();
        p.setName("Test");
        p.setDescription("Test product");
        p.setPrice(new BigDecimal("10"));
        p.setCategory("Test");

        Product saved = service.create(p);
        Assertions.assertNotNull(saved.getId());
    }
}
