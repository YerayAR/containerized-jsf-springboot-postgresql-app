package com.example.backend.controller;

import com.example.backend.dto.ProductRequestDto;
import com.example.backend.dto.ProductResponseDto;
import com.example.backend.model.Product;
import com.example.backend.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Products", description = "Product management APIs")
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @Operation(summary = "Get all products", description = "Retrieve a paginated list of products with optional filtering")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved products",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public Page<ProductResponseDto> list(
            @Parameter(description = "Filter by product name (case-insensitive)") @RequestParam(required = false) String name,
            @Parameter(description = "Filter by exact category") @RequestParam(required = false) String category,
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        
        Page<Product> products = service.findAll(name, category, pageable);
        return new PageImpl<>(
            products.getContent().stream()
                .map(ProductResponseDto::from)
                .collect(Collectors.toList()),
            pageable,
            products.getTotalElements()
        );
    }

    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> get(
            @Parameter(description = "Product ID", required = true) @PathVariable Long id) {
        Product product = service.get(id);
        return ResponseEntity.ok(ProductResponseDto.from(product));
    }

    @Operation(summary = "Create new product", description = "Create a new product (Admin only)", 
               security = @SecurityRequirement(name = "JWT"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Admin access required")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductRequestDto productDto) {
        Product product = convertToEntity(productDto);
        Product savedProduct = service.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponseDto.from(savedProduct));
    }

    @Operation(summary = "Update product", description = "Update an existing product completely (Admin only)",
               security = @SecurityRequirement(name = "JWT"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Admin access required"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDto> update(
            @Parameter(description = "Product ID", required = true) @PathVariable Long id, 
            @Valid @RequestBody ProductRequestDto productDto) {
        Product product = convertToEntity(productDto);
        Product updatedProduct = service.update(id, product);
        return ResponseEntity.ok(ProductResponseDto.from(updatedProduct));
    }

    @Operation(summary = "Partially update product", description = "Update specific fields of a product (Admin only)",
               security = @SecurityRequirement(name = "JWT"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Admin access required"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDto> partial(
            @Parameter(description = "Product ID", required = true) @PathVariable Long id, 
            @Parameter(description = "Fields to update") @RequestBody Map<String, Object> updates) {
        Product updatedProduct = service.partialUpdate(id, updates);
        return ResponseEntity.ok(ProductResponseDto.from(updatedProduct));
    }

    @Operation(summary = "Delete product", description = "Soft delete a product (Admin only)",
               security = @SecurityRequirement(name = "JWT"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Admin access required"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Product ID", required = true) @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Helper method to convert DTO to Entity
    private Product convertToEntity(ProductRequestDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
        product.setActive(dto.isActive());
        return product;
    }
}
