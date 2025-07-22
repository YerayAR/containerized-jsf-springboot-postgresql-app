package com.example.backend.model;

// Importaciones de JPA y validación
import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

// Importaciones de Hibernate para optimizaciones específicas
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

// Importaciones para la documentación de Swagger/OpenAPI
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Entidad que representa un producto en la base de datos.
 * Esta clase está mapeada a la tabla "products".
 *
 * Se utilizan optimizaciones de Hibernate:
 * - @DynamicUpdate: Genera sentencias SQL de actualización solo con las columnas que han cambiado.
 * - @SelectBeforeUpdate: Comprueba si la entidad ha cambiado antes de ejecutar una actualización, evitando escrituras innecesarias.
 */
@Entity
@Table(name = "products")
@DynamicUpdate
@SelectBeforeUpdate
@Schema(description = "Entidad que representa un producto, con optimizaciones de Hibernate.")
public class Product {

    /**
     * Identificador único del producto.
     * Se genera automáticamente por la base de datos (estrategia de identidad).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del producto.", example = "1")
    private Long id;

    /**
     * Nombre del producto.
     * No puede ser nulo o vacío, y debe tener entre 3 y 100 caracteres.
     */
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
    private String name;

    /**
     * Descripción del producto.
     * Puede tener hasta 500 caracteres.
     */
    @Size(max = 500, message = "La descripción no puede tener más de 500 caracteres.")
    private String description;

    /**
     * Precio del producto.
     * No puede ser nulo y debe ser un valor decimal no negativo.
     */
    @NotNull(message = "El precio no puede ser nulo.")
    @DecimalMin(value = "0.00", message = "El precio debe ser un valor positivo.")
    private BigDecimal price;

    /**
     * Categoría del producto.
     * No puede ser nula o vacía.
     */
    @NotBlank(message = "La categoría no puede estar vacía.")
    private String category;

    /**
     * Estado del producto (activo o inactivo).
     * Por defecto, un producto se crea como activo.
     */
    private boolean active = true;

    // --- Constructores ---

    /**
     * Constructor por defecto.
     * Requerido por JPA.
     */
    public Product() {}
    
    /**
     * Constructor con todos los campos.
     * Útil para crear instancias de productos con todos sus datos.
     *
     * @param id          Identificador del producto.
     * @param name        Nombre del producto.
     * @param description Descripción del producto.
     * @param price       Precio del producto.
     * @param category    Categoría del producto.
     * @param active      Estado del producto.
     */
    public Product(Long id, String name, String description, BigDecimal price, String category, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.active = active;
    }

    // --- Getters y Setters ---
    // Métodos para acceder y modificar los atributos de la entidad.

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
