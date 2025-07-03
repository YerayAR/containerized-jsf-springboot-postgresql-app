package com.example.backend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public Page<Product> list(@RequestParam(required = false) String name,
                              @RequestParam(required = false) String category,
                              Pageable pageable) {
        return service.findAll(name, category, pageable);
    }

    @GetMapping("/{id}")
    public Product get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public Product create(@Valid @RequestBody Product p) {
        return service.create(p);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody Product p) {
        return service.update(id, p);
    }

    @PatchMapping("/{id}")
    public Product partial(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return service.partialUpdate(id, updates);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
