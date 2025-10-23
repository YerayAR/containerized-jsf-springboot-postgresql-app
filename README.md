# JSF Spring Boot PostgreSQL - Containerized Application

Aplicación web completa con JSF, Spring Boot y PostgreSQL, optimizada para Railway con logging extensivo.

## 🚀 Características

- **Frontend**: JavaServer Faces (JSF) con PrimeFaces
- **Backend**: Spring Boot 3.2.0 con Java 17
- **Base de datos**: PostgreSQL
- **Logging**: Extensivo en todas las capas (DEBUG/INFO/ERROR)
- **Health Checks**: Spring Boot Actuator
- **Containerización**: Docker multi-stage optimizado
- **Cloud-Ready**: Configurado para Railway

## 📋 Prerequisitos

- Java 17+
- Maven 3.8+
- Docker & Docker Compose (para testing local)
- Railway CLI (para deploy)

## 🏗️ Estructura del Proyecto

```
├── src/
│   ├── main/
│   │   ├── java/com/example/app/
│   │   │   ├── Application.java          # Clase principal con logs
│   │   │   ├── model/User.java           # Entidad JPA con lifecycle logs
│   │   │   ├── repository/UserRepository.java
│   │   │   ├── service/UserService.java  # Servicio con logs extensivos
│   │   │   └── controller/UserBean.java  # Managed Bean JSF
│   │   ├── resources/
│   │   │   └── application.properties    # Configuración con logging
│   │   └── webapp/
│   │       ├── index.xhtml              # Vista principal JSF
│   │       └── WEB-INF/web.xml
├── Dockerfile                            # Multi-stage con logs
├── docker-compose.yml                    # Para testing local
├── railway.toml                          # Configuración Railway
└── pom.xml
```

## 🔧 Testing Local

### Con Docker Compose

```bash
# Build y start
docker-compose up --build

# Acceder a la aplicación
http://localhost:8080

# Health check
http://localhost:8080/actuator/health

# Logs en tiempo real
docker-compose logs -f app

# Detener
docker-compose down
```

### Sin Docker (requiere PostgreSQL local)

```bash
# Configurar variables de entorno
export DATABASE_URL=jdbc:postgresql://localhost:5432/testdb
export PGUSER=postgres
export PGPASSWORD=postgres

# Build y run
mvn clean package
java -jar target/*.jar
```

## ☁️ Deploy en Railway

### Opción 1: Desde GitHub (Recomendado)

1. **Crear repositorio en GitHub**
   ```bash
   git init
   git add .
   git commit -m "Initial commit"
   git remote add origin <tu-repo-url>
   git push -u origin main
   ```

2. **Conectar con Railway**
   - Ir a [Railway](https://railway.app)
   - "New Project" → "Deploy from GitHub repo"
   - Seleccionar tu repositorio
   - Railway detectará automáticamente el Dockerfile

3. **Agregar PostgreSQL**
   - En tu proyecto Railway: "New" → "Database" → "Add PostgreSQL"
   - Railway automáticamente configurará las variables:
     - `DATABASE_URL`
     - `PGHOST`, `PGPORT`, `PGDATABASE`
     - `PGUSER`, `PGPASSWORD`

4. **Configurar variables de entorno adicionales** (si es necesario)
   - En el dashboard del servicio → "Variables"
   - Añadir: `PORT` (Railway lo gestiona automáticamente)

5. **Verificar deployment**
   - Railway te dará una URL pública
   - Acceder a: `https://tu-app.railway.app`
   - Health check: `https://tu-app.railway.app/actuator/health`

### Opción 2: Desde CLI

```bash
# Instalar Railway CLI
npm i -g @railway/cli

# Login
railway login

# Inicializar proyecto
railway init

# Añadir PostgreSQL
railway add --plugin postgresql

# Deploy
railway up

# Ver logs
railway logs
```

## 📊 Monitoreo y Logs

### Ver logs en Railway

1. **Desde Dashboard**
   - Ir a tu servicio
   - Click en "Deployments"
   - Ver logs en tiempo real

2. **Desde CLI**
   ```bash
   railway logs
   railway logs --follow  # Logs en tiempo real
   ```

### Logs implementados en la aplicación

- **Application.java**: Logs de inicio/shutdown
- **User.java**: Logs de lifecycle JPA (@PrePersist, @PostPersist, etc.)
- **UserService.java**: Logs de operaciones CRUD
- **UserBean.java**: Logs de interacciones JSF
- **Dockerfile**: Logs de build y runtime
- **Spring Boot**: Logs de SQL queries (con valores)

### Niveles de logging

```properties
# Configurados en application.properties
logging.level.root=INFO
logging.level.com.example=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

## 🔍 Troubleshooting

### La aplicación no inicia en Railway

1. **Verificar logs**:
   ```bash
   railway logs
   ```

2. **Buscar errores comunes**:
   - `DATABASE_URL` no configurado → Añadir PostgreSQL plugin
   - Puerto incorrecto → Railway usa `$PORT` automáticamente
   - Memoria insuficiente → Ajustar plan de Railway

3. **Verificar health check**:
   ```bash
   curl https://tu-app.railway.app/actuator/health
   ```

### Errores de conexión a PostgreSQL

1. Verificar variables de entorno en Railway dashboard
2. Asegurarse que el servicio PostgreSQL está activo
3. Revisar logs: `railway logs | grep -i postgres`

### Build falla

1. Verificar que el Dockerfile es correcto
2. Revisar logs de build en Railway
3. Test local: `docker build -t test .`

## 🎯 Funcionalidades de la Aplicación

- **Crear usuarios**: Formulario con validación
- **Listar usuarios**: Tabla paginada con PrimeFaces
- **Eliminar usuarios**: Con confirmación
- **Health checks**: `/actuator/health`
- **Logs detallados**: En todas las operaciones

## 📝 Notas Importantes

- La aplicación usa `spring.jpa.hibernate.ddl-auto=update` para crear tablas automáticamente
- Los logs SQL muestran queries y valores de parámetros (TRACE level)
- El Dockerfile usa multi-stage build para optimizar tamaño de imagen
- Railway automáticamente asigna el puerto via `$PORT`
- Health check configurado para 5 minutos de timeout inicial

## 🔐 Seguridad

- Usuario non-root en Docker
- Variables de entorno para credenciales
- Validaciones en capa de servicio
- Health checks para monitoreo

## 📚 Referencias

- [Spring Boot](https://spring.io/projects/spring-boot)
- [JoinFaces](https://joinfaces.org/)
- [PrimeFaces](https://www.primefaces.org/)
- [Railway Docs](https://docs.railway.app/)

## 🤝 Contribuir

1. Fork el proyecto
2. Crear feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push al branch (`git push origin feature/AmazingFeature`)
5. Abrir Pull Request

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la Licencia MIT.
