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

/**
 * Controlador REST para la gestión de productos.
 * Expone los endpoints de la API para realizar operaciones CRUD sobre los productos.
 */
@Tag(name = "Products", description = "APIs para la gestión de productos")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    /**
     * Constructor para la inyección de dependencias del servicio de productos.
     * @param service El servicio que gestiona la lógica de negocio de los productos.
     */
    public ProductController(ProductService service) {
        this.service = service;
    }

    /**
     * Endpoint para obtener una lista paginada de productos, con filtros opcionales.
     * @param name     Filtro por nombre de producto (búsqueda parcial, insensible a mayúsculas).
     * @param category Filtro por categoría exacta.
     * @param pageable Parámetros de paginación (página, tamaño, ordenación).
     * @return Una página de DTOs de respuesta de productos.
     */
    @Operation(summary = "Obtener todos los productos", description = "Recupera una lista paginada de productos con filtros opcionales.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos recuperados con éxito",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public Page<ProductResponseDto> list(
            @Parameter(description = "Filtrar por nombre de producto (insensible a mayúsculas)") @RequestParam(required = false) String name,
            @Parameter(description = "Filtrar por categoría exacta") @RequestParam(required = false) String category,
            @Parameter(description = "Parámetros de paginación") Pageable pageable) {
        
        // Llama al servicio para encontrar los productos
        Page<Product> products = service.findAll(name, category, pageable);
        // Convierte la página de entidades a una página de DTOs
        return new PageImpl<>(
            products.getContent().stream()
                .map(ProductResponseDto::from)
                .collect(Collectors.toList()),
            pageable,
            products.getTotalElements()
        );
    }

    /**
     * Endpoint para obtener un producto específico por su ID.
     * @param id El ID del producto a recuperar.
     * @return Una respuesta con el DTO del producto o un error 404 si no se encuentra.
     */
    @Operation(summary = "Obtener producto por ID", description = "Recupera un producto específico por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> get(
            @Parameter(description = "ID del producto", required = true) @PathVariable Long id) {
        Product product = service.get(id);
        return ResponseEntity.ok(ProductResponseDto.from(product));
    }

    /**
     * Endpoint para crear un nuevo producto.
     * Requiere rol de 'ADMIN'.
     * @param productDto DTO con los datos del producto a crear.
     * @return Una respuesta con el DTO del producto creado y el estado 201 (Created).
     */
    @Operation(summary = "Crear nuevo producto", description = "Crea un nuevo producto (solo para administradores).",
               security = @SecurityRequirement(name = "JWT"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado con éxito",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado (se requiere rol de ADMIN)")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductRequestDto productDto) {
        Product product = convertToEntity(productDto);
        Product savedProduct = service.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponseDto.from(savedProduct));
    }

    /**
     * Endpoint para actualizar completamente un producto existente.
     * Requiere rol de 'ADMIN'.
     * @param id El ID del producto a actualizar.
     * @param productDto DTO con los nuevos datos del producto.
     * @return Una respuesta con el DTO del producto actualizado.
     */
    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente por completo (solo para administradores).",
               security = @SecurityRequirement(name = "JWT"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado con éxito",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDto> update(
            @Parameter(description = "ID del producto", required = true) @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto productDto) {
        Product product = convertToEntity(productDto);
        Product updatedProduct = service.update(id, product);
        return ResponseEntity.ok(ProductResponseDto.from(updatedProduct));
    }

    /**
     * Endpoint para actualizar parcialmente un producto.
     * Requiere rol de 'ADMIN'.
     * @param id El ID del producto a actualizar.
     * @param updates Un mapa con los campos a actualizar.
     * @return Una respuesta con el DTO del producto actualizado.
     */
    @Operation(summary = "Actualizar parcialmente un producto", description = "Actualiza campos específicos de un producto (solo para administradores).",
               security = @SecurityRequirement(name = "JWT"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado con éxito",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDto> partial(
            @Parameter(description = "ID del producto", required = true) @PathVariable Long id,
            @Parameter(description = "Campos a actualizar") @RequestBody Map<String, Object> updates) {
        Product updatedProduct = service.partialUpdate(id, updates);
        return ResponseEntity.ok(ProductResponseDto.from(updatedProduct));
    }

    /**
     * Endpoint para realizar un borrado lógico de un producto.
     * Requiere rol de 'ADMIN'.
     * @param id El ID del producto a eliminar.
     * @return Una respuesta sin contenido (204 No Content).
     */
    @Operation(summary = "Eliminar producto", description = "Realiza un borrado lógico de un producto (solo para administradores).",
               security = @SecurityRequirement(name = "JWT"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado con éxito"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del producto", required = true) @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Método de ayuda para convertir un DTO de solicitud a una entidad de producto.
     * @param dto El DTO de solicitud.
     * @return La entidad de producto correspondiente.
     */
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
