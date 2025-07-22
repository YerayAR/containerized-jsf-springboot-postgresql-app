package com.example.backend.repository;

import com.example.backend.model.Product;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Native Hibernate DAO implementation
 * This demonstrates using pure Hibernate Session instead of EntityManager
 * Completely decoupled from JPA generic APIs
 */
@Repository
@Transactional
public class ProductHibernateDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Save or update product using native Hibernate Session
     */
    public Product saveOrUpdate(Product product) {
        Session session = getCurrentSession();
        session.saveOrUpdate(product);
        return product;
    }

    /**
     * Find by ID using native Hibernate Session
     */
    public Product findById(Long id) {
        Session session = getCurrentSession();
        return session.get(Product.class, id);
    }

    /**
     * Find all active products using native Hibernate Criteria API
     */
    @SuppressWarnings("unchecked")
    public List<Product> findAllActive() {
        Session session = getCurrentSession();
        return session.createCriteria(Product.class)
                .add(Restrictions.eq("active", true))
                .list();
    }

    /**
     * Find by name using native Hibernate Criteria API with like
     */
    @SuppressWarnings("unchecked")
    public List<Product> findByNameLike(String name) {
        Session session = getCurrentSession();
        return session.createCriteria(Product.class)
                .add(Restrictions.ilike("name", "%" + name + "%"))
                .add(Restrictions.eq("active", true))
                .list();
    }

    /**
     * Find by category using native Hibernate Criteria API
     */
    @SuppressWarnings("unchecked")
    public List<Product> findByCategory(String category) {
        Session session = getCurrentSession();
        return session.createCriteria(Product.class)
                .add(Restrictions.eq("category", category))
                .add(Restrictions.eq("active", true))
                .list();
    }

    /**
     * Delete (soft delete) using native Hibernate Session
     */
    public void softDelete(Long id) {
        Session session = getCurrentSession();
        Product product = session.get(Product.class, id);
        if (product != null) {
            product.setActive(false);
            session.update(product);
        }
    }

    /**
     * Count active products using native Hibernate HQL
     */
    public Long countActive() {
        Session session = getCurrentSession();
        return (Long) session.createQuery("SELECT COUNT(p) FROM Product p WHERE p.active = true")
                .uniqueResult();
    }

    /**
     * Native Hibernate HQL query example
     */
    @SuppressWarnings("unchecked")
    public List<Product> findExpensiveProducts(java.math.BigDecimal minPrice) {
        Session session = getCurrentSession();
        return session.createQuery("FROM Product p WHERE p.price >= :minPrice AND p.active = true")
                .setParameter("minPrice", minPrice)
                .list();
    }
}
