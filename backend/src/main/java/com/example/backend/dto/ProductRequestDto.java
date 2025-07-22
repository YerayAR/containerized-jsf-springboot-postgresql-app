package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "DTO for creating or updating products")
public class ProductRequestDto {
    
    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
    @Schema(description = "Product name", example = "Gaming Laptop", required = true)
    private String name;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Schema(description = "Product description", example = "High-performance gaming laptop with RTX graphics")
    private String description;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.00", message = "Price must be positive")
    @Digits(integer = 8, fraction = 2, message = "Price format is invalid")
    @Schema(description = "Product price", example = "1299.99", required = true)
    private BigDecimal price;
    
    @NotBlank(message = "Category is required")
    @Pattern(regexp = "^(Electronics|Books|Furniture|Food|Clothing|Sports|Home|Beauty|Automotive|Toys)$", 
             message = "Category must be one of: Electronics, Books, Furniture, Food, Clothing, Sports, Home, Beauty, Automotive, Toys")
    @Schema(description = "Product category", example = "Electronics", required = true,
            allowableValues = {"Electronics", "Books", "Furniture", "Food", "Clothing", "Sports", "Home", "Beauty", "Automotive", "Toys"})
    private String category;
    
    @Schema(description = "Product status (active/inactive)", example = "true")
    private boolean active = true;
    
    // Constructors
    public ProductRequestDto() {}
    
    public ProductRequestDto(String name, String description, BigDecimal price, String category, boolean active) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.active = active;
    }
    
    // Getters and Setters
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
