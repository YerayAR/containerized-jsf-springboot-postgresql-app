package com.example.backend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Page<Product> findAll(String name, String category, Pageable pageable) {
        // Simple filtering
        return repository.findAll((root, query, cb) -> {
            java.util.List<javax.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
            if (name != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (category != null) {
                predicates.add(cb.equal(root.get("category"), category));
            }
            predicates.add(cb.isTrue(root.get("active")));
            return cb.and(predicates.toArray(new javax.persistence.criteria.Predicate[0]));
        }, pageable);
    }

    public Product get(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
    }

    public Product create(Product p) { return repository.save(p); }

    public Product update(Long id, Product p) {
        Product existing = get(id);
        p.setId(existing.getId());
        return repository.save(p);
    }

    public Product partialUpdate(Long id, java.util.Map<String, Object> updates) {
        Product existing = get(id);
        updates.forEach((k,v) -> {
            switch(k) {
                case "name": existing.setName((String)v); break;
                case "description": existing.setDescription((String)v); break;
                case "price": existing.setPrice(new java.math.BigDecimal(v.toString())); break;
                case "category": existing.setCategory((String)v); break;
            }
        });
        return repository.save(existing);
    }

    public void delete(Long id) {
        Product existing = get(id);
        existing.setActive(false);
        repository.save(existing);
    }
}
