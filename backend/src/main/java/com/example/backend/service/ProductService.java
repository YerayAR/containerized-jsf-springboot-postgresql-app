package com.example.backend.service;

import com.example.backend.model.Product;
import com.example.backend.repository.ProductRepository;
import com.example.backend.exception.ResourceNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Servicio para gestionar la lógica de negocio de los productos.
 * Proporciona métodos para operaciones CRUD y consultas filtradas.
 */
@Service
public class ProductService {

    private final ProductRepository repository;

    /**
     * Constructor para la inyección de dependencias del repositorio de productos.
     * @param repository El repositorio de productos.
     */
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    /**
     * Busca y devuelve una página de productos con filtros opcionales por nombre y categoría.
     * Solo se devuelven los productos activos.
     *
     * @param name     El nombre del producto a filtrar (búsqueda parcial, insensible a mayúsculas).
     * @param category La categoría del producto a filtrar.
     * @param pageable La información de paginación.
     * @return Una página de productos que coinciden con los criterios de búsqueda.
     */
    @Transactional(readOnly = true)
    public Page<Product> findAll(String name, String category, Pageable pageable) {
        // Se utiliza una especificación de JPA para construir una consulta dinámica.
        return repository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Filtro por nombre (si se proporciona)
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            // Filtro por categoría (si se proporciona)
            if (category != null && !category.isEmpty()) {
                predicates.add(cb.equal(root.get("category"), category));
            }
            // Siempre se filtran los productos activos
            predicates.add(cb.isTrue(root.get("active")));
            
            // Combina todos los predicados con un "AND"
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    /**
     * Obtiene un producto por su ID.
     *
     * @param id El ID del producto.
     * @return El producto encontrado.
     * @throws ResourceNotFoundException si no se encuentra el producto.
     */
    @Transactional(readOnly = true)
    public Product get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    /**
     * Crea un nuevo producto.
     *
     * @param product El producto a crear.
     * @return El producto guardado.
     */
    @Transactional
    public Product create(Product product) {
        return repository.save(product);
    }

    /**
     * Actualiza un producto existente.
     *
     * @param id      El ID del producto a actualizar.
     * @param product El producto con los datos actualizados.
     * @return El producto actualizado.
     * @throws ResourceNotFoundException si el producto no existe.
     */
    @Transactional
    public Product update(Long id, Product product) {
        // Verifica si el producto existe antes de actualizar
        Product existing = get(id);
        product.setId(existing.getId()); // Asegura que se actualice la entidad correcta
        return repository.save(product);
    }

    /**
     * Actualiza parcialmente un producto con los campos proporcionados.
     *
     * @param id      El ID del producto a actualizar.
     * @param updates Un mapa con los campos a actualizar y sus nuevos valores.
     * @return El producto actualizado.
     * @throws ResourceNotFoundException si el producto no existe.
     */
    @Transactional
    public Product partialUpdate(Long id, Map<String, Object> updates) {
        Product existing = get(id);

        // Itera sobre el mapa de actualizaciones y aplica los cambios
        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    existing.setName((String) value);
                    break;
                case "description":
                    existing.setDescription((String) value);
                    break;
                case "price":
                    existing.setPrice(new java.math.BigDecimal(value.toString()));
                    break;
                case "category":
                    existing.setCategory((String) value);
                    break;
                // Se pueden añadir más campos aquí si es necesario
            }
        });

        return repository.save(existing);
    }

    /**
     * Realiza un borrado lógico de un producto (lo marca como inactivo).
     *
     * @param id El ID del producto a "eliminar".
     * @throws ResourceNotFoundException si el producto no existe.
     */
    @Transactional
    public void delete(Long id) {
        Product existing = get(id);
        existing.setActive(false); // Borrado lógico
        repository.save(existing);
    }
}
