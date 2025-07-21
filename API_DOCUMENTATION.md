# 📋 API REST Documentation

## 🔗 Enlaces de Swagger (Cuando esté configurado correctamente)

### URLs Swagger/OpenAPI
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **Swagger UI (alt)**: http://localhost:8080/swagger-ui.html  
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **OpenAPI YAML**: http://localhost:8080/v3/api-docs.yaml

---

## 📦 **Endpoints de Productos**

### `GET /api/products`
**Descripción**: Obtener lista paginada de productos activos con filtros opcionales

**Parámetros de consulta**:
- `name` (optional): Filtrar por nombre (búsqueda case-insensitive)
- `category` (optional): Filtrar por categoría exacta
- `page` (optional, default: 0): Número de página
- `size` (optional, default: 20): Tamaño de página
- `sort` (optional): Campo de ordenamiento

**Ejemplo**:
```bash
GET http://localhost:8080/api/products
GET http://localhost:8080/api/products?name=laptop
GET http://localhost:8080/api/products?category=Electronics
GET http://localhost:8080/api/products?page=0&size=10
```

**Respuesta**:
```json
{
  "content": [
    {
      "id": 1,
      "name": "Laptop",
      "description": "High-end laptop",
      "price": 1500.00,
      "category": "Electronics",
      "active": true
    }
  ],
  "pageable": { ... },
  "totalElements": 12,
  "totalPages": 1,
  "first": true,
  "last": true
}
```

---

### `GET /api/products/{id}` 🔒
**Descripción**: Obtener producto específico por ID

**Parámetros**:
- `id`: ID del producto

**Autenticación**: Requerida (JWT Bearer Token)

**Ejemplo**:
```bash
GET http://localhost:8080/api/products/1
Authorization: Bearer <jwt-token>
```

---

### `POST /api/products` 🔒
**Descripción**: Crear nuevo producto

**Autenticación**: Requerida (JWT Bearer Token)

**Cuerpo de la petición**:
```json
{
  "name": "Nuevo Producto",
  "description": "Descripción del producto",
  "price": 99.99,
  "category": "Electronics",
  "active": true
}
```

**Validaciones**:
- `name`: Requerido, 3-100 caracteres
- `description`: Máximo 500 caracteres
- `price`: Requerido, >= 0.00
- `category`: Requerido

---

### `PUT /api/products/{id}` 🔒
**Descripción**: Actualizar producto completo

**Autenticación**: Requerida (JWT Bearer Token)

---

### `PATCH /api/products/{id}` 🔒
**Descripción**: Actualización parcial de producto

**Autenticación**: Requerida (JWT Bearer Token)

**Cuerpo de la petición** (campos opcionales):
```json
{
  "name": "Nuevo nombre",
  "price": 129.99
}
```

---

### `DELETE /api/products/{id}` 🔒
**Descripción**: Eliminar producto (soft delete - marca como inactivo)

**Autenticación**: Requerida (JWT Bearer Token)

---

## 🔐 **Endpoints de Autenticación**

### `POST /api/auth/login`
**Descripción**: Autenticar usuario y obtener JWT token

**Cuerpo de la petición**:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Respuesta exitosa**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer"
}
```

**Credenciales por defecto**:
- Username: `admin`
- Password: `admin123`

---

## 📈 **Ejemplos de Uso con cURL**

### 1. Obtener productos (sin autenticación)
```bash
curl -X GET "http://localhost:8080/api/products" \
  -H "accept: application/json"
```

### 2. Filtrar productos por nombre
```bash
curl -X GET "http://localhost:8080/api/products?name=laptop" \
  -H "accept: application/json"
```

### 3. Autenticarse y obtener token
```bash
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

### 4. Crear producto (con autenticación)
```bash
curl -X POST "http://localhost:8080/api/products" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Nuevo Producto",
    "description": "Descripción del producto",
    "price": 99.99,
    "category": "Electronics"
  }'
```

---

## 🛡️ **Autenticación y Seguridad**

### JWT Bearer Authentication
La mayoría de endpoints requieren autenticación JWT. Para usar endpoints protegidos:

1. **Login**: Usa `POST /api/auth/login` para obtener un token
2. **Incluye el token**: En el header `Authorization: Bearer <token>`
3. **Token válido**: El token debe ser válido y no expirado

### Endpoints Públicos
- `GET /api/products` (lista de productos)
- `POST /api/auth/login` (autenticación)
- Swagger UI y documentación (cuando esté configurado)

---

## 🎯 **Características de la API**

### ✅ Funcionalidades Implementadas
- **CRUD completo** de productos
- **Filtros avanzados** (nombre, categoría)
- **Paginación** y ordenamiento
- **Soft delete** (productos se marcan como inactivos)
- **Validación de datos** con Bean Validation
- **Autenticación JWT** con Spring Security
- **Integración Hibernate** nativa con optimizaciones
- **Documentación Swagger/OpenAPI**

### 🔧 Tecnologías Utilizadas
- **Spring Boot 2.6.0**
- **Hibernate 5.6.1** (nativo, no JPA genérico)
- **PostgreSQL 13**
- **Spring Security** con JWT
- **SpringDoc OpenAPI** para documentación
- **Docker & Docker Compose**

---

## 🚀 **Estado del Proyecto**

✅ **Backend funcionando**: API REST operativa  
✅ **Base de datos**: 12 productos de prueba cargados  
✅ **Frontend JSF**: Interfaz web disponible  
✅ **Hibernate nativo**: Migración completada  
🔧 **Swagger UI**: En proceso de configuración  

---

## 📞 **URLs de Acceso**

- **API Backend**: http://localhost:8080/api/
- **Frontend JSF**: http://localhost:8081/app/
- **pgAdmin**: http://localhost:5050 (admin@admin.com / admin)
- **Swagger UI**: http://localhost:8080/swagger-ui.html (cuando esté listo)
