# Documentación Detallada del Proyecto SaaS

Este documento amplía de forma exhaustiva la planificación, arquitectura y ciclo de vida completo de una aplicación SaaS moderna basada en Spring Boot y JSF en el frontend, con base de datos PostgreSQL y orquestación mediante Docker Compose. Sirve como guía integral para el desarrollo del proyecto, cubriendo desde la fase de análisis hasta el despliegue y las futuras iteraciones.

## 1. Introducción General

La aplicación tiene como objetivo permitir la gestión de productos en una tienda en línea de manera eficiente y escalable. El backend se basa en Spring Boot, la interfaz de usuario utiliza JSF, y se busca desplegar todo el stack en contenedores Docker. Se integra una base de datos PostgreSQL, y se propone un enfoque modular que permita la futura ampliación del sistema.

## 2. Análisis Detallado del Proyecto

### 2.1 Requisitos Funcionales

- Registro y autenticación de usuarios con roles diferenciados (administrador, editor, visitante).
- Creación, edición y eliminación de productos.
- Visualización de un catálogo de productos con paginación y filtros de búsqueda.
- Gestión de categorías de productos.
- API RESTful documentada con Swagger para interoperabilidad.

### 2.2 Requisitos No Funcionales

- Seguridad de datos a través de HTTPS y cifrado de contraseñas.
- Disponibilidad alta, con tolerancia a fallos mediante contenedores redundantes.
- Escalabilidad horizontal para soportar picos de uso.
- Observabilidad mediante logging estructurado y métricas de rendimiento.

### 2.3 Stakeholders

- **Equipo de desarrollo**: encargado de implementar nuevas funcionalidades y mantener la plataforma.
- **Equipo de operaciones**: responsable de la infraestructura, despliegues y monitorización.
- **Usuarios finales**: quienes gestionarán el catálogo y consultarán la tienda en línea.

## 3. Planificación y Arquitectura

### 3.1 Justificación del Stack Tecnológico

- **Spring Boot** se elige por su robustez en entornos empresariales y su integración nativa con Hibernate para el acceso a datos.
- **JSF** (JavaServer Faces) permite un desarrollo web basado en componentes con buen soporte de plantillas.
- **PostgreSQL** proporciona características avanzadas de consultas y transacciones, ideales para datos estructurados.
- **Docker Compose** simplifica el despliegue local con múltiples servicios en contenedores.

### 3.2 Esquema de Arquitectura

```text
+-------------+     REST API     +--------------+     +-------------+
|  Frontend   | <--------------> |  Spring Boot | <-->| PostgreSQL  |
|   JSF       |                  |   Backend    |     |  Database   |
+-------------+                  +--------------+     +-------------+
      |                                                       ^
      |                        +------------------------------+
      |                        |
      v                        v
+-------------+         +--------------+
|  Usuario    |         | Administrador|
+-------------+         +--------------+
```

### 3.3 Estructura de Carpetas del Repositorio

```
/ (raíz)
|-- backend/              # Código fuente del backend Spring Boot
|   |-- src/
|   |   |-- main/java/
|   |   |   `-- com/example/backend
|   |   |       |-- controller
|   |   |       |-- service
|   |   |       |-- model
|   |   |       `-- repository
|   |   `-- test/java/
|   |       `-- com/example/backend
|   |           |-- ProductRepositoryTest.java
|   |           `-- ProductServiceTest.java
|   `-- pom.xml
|-- frontend/
|   |-- src/
|   |   `-- main/java/
|   |       `-- com/example/frontend
|   `-- pom.xml
|-- docker-compose.yml
|-- README.md
|-- docs/
|   `-- ANALISIS_DETALLADO.md
```

## 4. Modelado de Datos

### 4.1 Diagrama Entidad-Relación (E-R)

```
[Usuario] 1---* [Producto] *---1 [Categoría]
      \
       \ *---1 [Rol]
```

### 4.2 Entidades Principales

- **Usuario**: id, nombre, contraseña, rol.
- **Producto**: id, nombre, descripción, precio, categoría_id.
- **Categoría**: id, nombre, descripción.
- **Rol**: id, nombre.

### 4.3 Migraciones Iniciales

Las migraciones se realizan mediante `Flyway` para garantizar consistencia en todos los entornos. Un ejemplo de archivo de migración es:

```sql
-- V1__init.sql
CREATE TABLE categoria (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT
);

CREATE TABLE producto (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    categoria_id INTEGER REFERENCES categoria(id)
);
```

## 5. Desarrollo del Backend

### 5.1 Controladores

```java
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Producto> listar() {
        return service.obtenerTodos();
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto p) {
        Producto guardado = service.guardar(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }
}
```

### 5.2 Servicios

```java
@Service
public class ProductoService {

    private final ProductoRepository repo;

    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    public List<Producto> obtenerTodos() {
        return repo.findAll();
    }

    public Producto guardar(Producto p) {
        return repo.save(p);
    }
}
```

### 5.3 Seguridad JWT

```java
public class JwtUtil {
    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
```

### 5.4 Pruebas Unitarias y de Integración

Se utilizan `JUnit 5` y `Spring Boot Test` para cubrir la lógica de repositorios y servicios. Los tests se ejecutan mediante Maven con `mvn test`.

## 6. Desarrollo del Frontend

### 6.1 Páginas JSF Principales

- `index.xhtml`: muestra la lista de productos.
- `login.xhtml`: formulario de autenticación.
- `admin.xhtml`: panel de administración para crear y editar productos.

### 6.2 Integración con el Backend

La comunicación se realiza mediante llamadas `Ajax` a la API REST expuesta por Spring Boot. Se utilizan beans de sesión para manejar el estado del usuario.

```xml
<h:form>
    <h:inputText value="#{productoBean.nombre}" />
    <h:commandButton value="Guardar" action="#{productoBean.guardar}" />
</h:form>
```

### 6.3 Validaciones y Mensajes

JSF permite validar campos de forma declarativa:

```xml
<h:inputText id="precio" value="#{productoBean.precio}">
    <f:validateDoubleRange minimum="0" />
</h:inputText>
<h:message for="precio" />
```

## 7. Containerización y Orquestación

### 7.1 Dockerfile del Backend

```Dockerfile
FROM eclipse-temurin:11-jre
COPY target/backend.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 7.2 Dockerfile del Frontend

```Dockerfile
FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:11-jre
COPY --from=build /app/target/frontend.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 7.3 Docker Compose Simplificado

```yaml
version: '3.8'
services:
  db:
    image: postgres:13
    environment:
      POSTGRES_USER: appuser
      POSTGRES_PASSWORD: securepass
      POSTGRES_DB: productdb
    volumes:
      - pgdata:/var/lib/postgresql/data
  backend:
    build: ./backend
    environment:
      SPRING_PROFILES_ACTIVE: prod
    depends_on:
      - db
    ports:
      - "8080:8080"
  frontend:
    build: ./frontend
    depends_on:
      - backend
    ports:
      - "8081:8080"
volumes:
  pgdata:
```

## 8. CI/CD con GitHub Actions

Se define un flujo de trabajo para compilar, testear y construir las imágenes Docker automáticamente.

```yaml
name: CI
on:
  push:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK
        uses: actions/setup-java@v3
        with:
          java-version: '11'
      - name: Test backend
        run: mvn -f backend/pom.xml test
      - name: Build Docker images
        run: docker-compose build
```

## 9. Observabilidad

### 9.1 Logging

Se emplea `logback` para la configuración de logs en Spring Boot. Todos los logs se exportan a un archivo y se envían a la salida estándar para ser recogidos por Docker.

### 9.2 Métricas

Se incorpora `Micrometer` con exportación a Prometheus. El endpoint `/actuator/prometheus` se expone para recopilar métricas de la aplicación.

### 9.3 Alertas

Grafana se configura para consumir las métricas de Prometheus y generar alertas basadas en umbrales de latencia o errores.

## 10. Buenas Prácticas de Desarrollo

- Uso de ramas `feature/*` para nuevas funcionalidades.
- Convenciones de commit siguiendo el formato `tipo: descripcion` (ej. `feat: agregar autenticacion`).
- Revisión obligatoria mediante Pull Requests antes de fusionar a `main`.

## 11. Retos Técnicos y Consideraciones

- Mantener consistencia de datos al escalar la base de datos: se propone replicación en lectura si el tráfico aumenta.
- Actualizaciones de librerías y dependencias para prevenir vulnerabilidades.
- Optimización de tiempos de construcción de contenedores mediante caché de capas.

## 12. Futuras Iteraciones

- Implementación de un panel de métricas en tiempo real para administradores.
- Integración de colas de mensajería (RabbitMQ) para tareas asíncronas.
- Modo oscuro y mejoras de accesibilidad en la interfaz.

## 13. Conclusiones

Este documento amplía la visión del proyecto, proporcionando detalles de configuración, desarrollo y despliegue. Al seguir estas directrices se garantiza un proyecto mantenible y escalable.
