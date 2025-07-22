# 🚀 Aplicación Contenerizada con JSF, Spring Boot y PostgreSQL

Esta es una aplicación web full-stack que demuestra la integración de un frontend con **JavaServer Faces (JSF)**, un backend con **Spring Boot** y una base de datos **PostgreSQL**. Todo el entorno está orquestado y desplegado utilizando **Docker** y **Docker Compose**.

El código ha sido **completamente documentado** para facilitar su comprensión, mantenimiento y extensibilidad.

## 🌟 Arquitectura y Tecnologías

La aplicación se divide en tres servicios principales que se ejecutan en contenedores Docker independientes:

-   **`frontend`**: Una aplicación web Java que utiliza **JavaServer Faces (JSF)** para la interfaz de usuario. Se ejecuta sobre un servidor **Apache Tomcat**.
    -   **JSF**: Framework basado en componentes para construir UIs del lado del servidor.
    -   **CDI (Weld)**: Para la inyección de dependencias en los Managed Beans de JSF.
    -   **Cliente HTTP**: Se comunica con el backend a través de su API REST para obtener y mostrar los datos.

-   **`backend`**: Una aplicación **Spring Boot** que expone una **API RESTful** para gestionar los productos.
    -   **Spring Web**: Para crear los endpoints REST.
    -   **Spring Data JPA & Hibernate**: Para la persistencia de datos y la comunicación con la base de datos. Se utilizan optimizaciones de Hibernate como `@DynamicUpdate`.
    -   **Spring Security**: Para la autenticación y autorización basadas en **JWT (JSON Web Tokens)**.
    -   **PostgreSQL Driver**: Para la conexión a la base de datos.
    -   **Swagger/OpenAPI**: Para la documentación interactiva de la API.

-   **`db`**: Una base de datos **PostgreSQL** donde se almacenan los datos de la aplicación.

-   **`pgadmin`**: Una herramienta de administración web para la base de datos PostgreSQL.

## ✨ Características Clave

-   **Frontend Moderno**: Interfaz de usuario construida con JSF, que permite vistas tanto en formato de tarjetas (grid) como de tabla.
-   **Backend Robusto**: API REST segura y bien documentada con Spring Boot.
-   **Seguridad Integrada**: Autenticación basada en JWT para proteger los endpoints del backend.
-   **Documentación de API**: **Swagger UI** integrado para explorar y probar la API de forma interactiva.
-   **Base de Datos Relacional**: PostgreSQL para un almacenamiento de datos fiable.
-   **Contenerización Completa**: Todo el stack se despliega con un solo comando gracias a Docker Compose.
-   **Código Documentado**: Cada clase, método y configuración importante está comentado para una fácil comprensión.

## 🚀 Cómo Empezar

### Prerrequisitos

-   **Docker** instalado.
-   **Docker Compose** instalado.

### Ejecución

1.  Clona este repositorio en tu máquina local.
2.  Abre una terminal en el directorio raíz del proyecto.
3.  Ejecuta el siguiente comando para construir las imágenes y levantar los contenedores:

    ```bash
    docker-compose up --build
    ```

    El flag `--build` asegura que las imágenes de Docker se construyan desde cero, aplicando cualquier cambio en el código o en los `Dockerfile`.

## 🔗 Enlaces Útiles

Una vez que la aplicación esté en funcionamiento, puedes acceder a los siguientes servicios:

### 📱 **Frontend (JSF)**

-   **Página Principal**: [http://localhost:8081/app/index.xhtml](http://localhost:8081/app/index.xhtml)
-   **Catálogo de Productos**: [http://localhost:8081/app/products.xhtml](http://localhost:8081/app/products.xhtml)
-   **Página de Login**: [http://localhost:8081/app/login.xhtml](http://localhost:8081/app/login.xhtml)

### 🛠️ **Backend (API REST & Documentación)**

-   **Swagger UI (Documentación Interactiva)**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
-   **Endpoint Principal de Productos**: [http://localhost:8080/api/products](http://localhost:8080/api/products)

### 🗄️ **Base de Datos**

-   **pgAdmin (Administración de BD)**: [http://localhost:5050](http://localhost:5050)
    -   **Email**: `admin@admin.com`
    -   **Contraseña**: `admin`

## 🔐 Autenticación

Para acceder a los endpoints protegidos de la API (como crear o eliminar productos), necesitas un token JWT.

1.  Ve a la sección `Auth` en **Swagger UI**.
2.  Utiliza el endpoint `POST /api/auth/login`.
3.  Proporciona las siguientes credenciales en el cuerpo de la solicitud:
    ```json
    {
      "username": "admin",
      "password": "password"
    }
    ```
4.  Copia el `token` de la respuesta.
5.  Haz clic en el botón **"Authorize"** en la parte superior derecha de Swagger UI e introduce el token en el formato `Bearer <tu-token>`.

Ahora podrás ejecutar las operaciones que requieren autenticación.

## 🧪 Pruebas

Para ejecutar las pruebas unitarias y de integración del backend, puedes usar Maven:

```bash
# Navega al directorio del backend y ejecuta los tests
mvn -f backend/pom.xml test
```
