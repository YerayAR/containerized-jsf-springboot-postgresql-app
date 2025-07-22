# Dockerfile para la aplicación de backend
# Utiliza una construcción multi-etapa para crear una imagen final más ligera.

# --- Etapa de Construcción (Build Stage) ---
# Se utiliza una imagen de Maven con JDK 11 para compilar el proyecto.
# Se le asigna un alias "build" a esta etapa para poder referenciarla más tarde.
FROM maven:3.8-jdk-11 AS build

# Establece el directorio de trabajo dentro del contenedor.
WORKDIR /app

# Copia el archivo pom.xml para descargar las dependencias.
# Esto aprovecha el sistema de caché de Docker: si el pom.xml no cambia,
# no se volverán a descargar las dependencias en construcciones posteriores.
COPY pom.xml .
RUN mvn -q dependency:resolve

# Copia el resto del código fuente del proyecto.
COPY src ./src

# Ejecuta el comando de Maven para empaquetar la aplicación en un archivo JAR.
# -q (quiet): modo silencioso para reducir el log.
# -DskipTests: se saltan las pruebas unitarias durante la construcción de la imagen.
RUN mvn -q package -DskipTests

# --- Etapa de Ejecución (Runtime Stage) ---
# Se utiliza una imagen base de OpenJDK 11 más ligera (jre-slim),
# ya que solo se necesita el entorno de ejecución de Java (JRE), no el JDK completo.
FROM openjdk:11-jre-slim

# Establece el directorio de trabajo en la imagen final.
WORKDIR /app

# Copia el archivo JAR generado desde la etapa de construcción ("build") a la imagen final.
# Esto resulta en una imagen mucho más pequeña, ya que no contiene Maven ni el código fuente.
COPY --from=build /app/target/backend-1.0.0.jar backend.jar

# Expone el puerto 8080, que es el puerto en el que la aplicación Spring Boot se ejecuta.
EXPOSE 8080

# Comando que se ejecutará cuando se inicie un contenedor a partir de esta imagen.
# Inicia la aplicación Java desde el archivo JAR.
CMD ["java", "-jar", "/app/backend.jar"]
