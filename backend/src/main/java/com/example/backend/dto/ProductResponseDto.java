package com.example.backend.dto;

import com.example.backend.model.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO for product responses")
public class ProductResponseDto {
    
    @Schema(description = "Product ID", example = "1")
    private Long id;
    
    @Schema(description = "Product name", example = "Gaming Laptop")
    private String name;
    
    @Schema(description = "Product description", example = "High-performance gaming laptop with RTX graphics")
    private String description;
    
    @Schema(description = "Product price", example = "1299.99")
    private BigDecimal price;
    
    @Schema(description = "Product category", example = "Electronics")
    private String category;
    
    @Schema(description = "Product status", example = "true")
    private boolean active;
    
    @Schema(description = "Formatted price for display", example = "$1,299.99")
    private String formattedPrice;
    
    @Schema(description = "Category badge color for UI", example = "primary")
    private String categoryBadge;
    
    // Constructors
    public ProductResponseDto() {}
    
    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.category = product.getCategory();
        this.active = product.isActive();
        this.formattedPrice = formatPrice(product.getPrice());
        this.categoryBadge = getCategoryBadge(product.getCategory());
    }
    
    private String formatPrice(BigDecimal price) {
        if (price == null) return "$0.00";
        return String.format("$%,.2f", price);
    }
    
    private String getCategoryBadge(String category) {
        if (category == null) return "secondary";
        switch (category.toLowerCase()) {
            case "electronics": return "primary";
            case "books": return "success";
            case "furniture": return "warning";
            case "food": return "info";
            case "clothing": return "danger";
            case "sports": return "dark";
            default: return "secondary";
        }
    }
    
    // Factory method for creating from Product entity
    public static ProductResponseDto from(Product product) {
        return new ProductResponseDto(product);
    }
    
    // Getters and Setters
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
    
    public String getFormattedPrice() { return formattedPrice; }
    public void setFormattedPrice(String formattedPrice) { this.formattedPrice = formattedPrice; }
    
    public String getCategoryBadge() { return categoryBadge; }
    public void setCategoryBadge(String categoryBadge) { this.categoryBadge = categoryBadge; }
}
