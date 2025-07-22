package com.example.backend.repository;

import com.example.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Spring Data JPA para la entidad {@link Product}.
 *
 * <p>Esta interfaz proporciona métodos CRUD (Crear, Leer, Actualizar, Eliminar)
 * para la entidad {@code Product} sin necesidad de implementación explícita,
 * gracias a la herencia de {@link JpaRepository}.</p>
 *
 * <p>Además, al extender {@link JpaSpecificationExecutor}, se añade la capacidad
 * de realizar consultas dinámicas y complejas utilizando el API de Criteria de JPA.</p>
 *
 * @see JpaRepository
 * @see JpaSpecificationExecutor
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    // JpaRepository proporciona métodos como:
    // - save(Product entity): Guarda o actualiza un producto.
    // - findById(Long id): Busca un producto por su ID.
    // - findAll(): Devuelve todos los productos.
    // - deleteById(Long id): Elimina un producto por su ID.

    // JpaSpecificationExecutor proporciona métodos como:
    // - findOne(Specification<Product> spec): Busca una única entidad que cumple con la especificación.
    // - findAll(Specification<Product> spec): Busca todas las entidades que cumplen con la especificación.
    // - findAll(Specification<Product> spec, Pageable pageable): Búsqueda paginada con especificación.
    // - count(Specification<Product> spec): Cuenta las entidades que cumplen con la especificación.
}
