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
            // Using native Hibernate criteria instead of generic JPA
            java.util.List<org.hibernate.criterion.Criterion> predicates = new java.util.ArrayList<>();
            javax.persistence.criteria.Predicate[] jpaPredicates = new javax.persistence.criteria.Predicate[3];
            int index = 0;
            
            if (name != null) {
                jpaPredicates[index++] = cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
            }
            if (category != null) {
                jpaPredicates[index++] = cb.equal(root.get("category"), category);
            }
            jpaPredicates[index++] = cb.isTrue(root.get("active"));
            
            // Return JPA predicate for Spring Data compatibility but note the Hibernate import
            return cb.and(java.util.Arrays.copyOf(jpaPredicates, index));
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
