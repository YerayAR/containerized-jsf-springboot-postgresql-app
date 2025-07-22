package com.example.backend.model;

// JPA standard annotations with Hibernate enhancements
import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

// Hibernate-specific optimizations
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

// Swagger documentation imports
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "products")
@DynamicUpdate
@SelectBeforeUpdate
@Schema(description = "Product entity with Hibernate optimizations")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the product", example = "1")
    private Long id;

    @NotBlank
    @Size(min = 3, max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal price;

    @NotBlank
    private String category;

    private boolean active = true;

    // Constructors
    public Product() {}
    
    public Product(Long id, String name, String description, BigDecimal price, String category, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.active = active;
    }

    // getters and setters
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
