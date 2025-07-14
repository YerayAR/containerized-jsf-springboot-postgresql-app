# Project Overview

This repository contains a containerized sample application composed of:

- **backend** – Spring Boot REST API that manages `Product` entities stored in PostgreSQL.
- **frontend** – JSF application served by Tomcat that consumes the backend API.
- **docker-compose.yml** – Orchestrates PostgreSQL, backend and frontend containers.
- **volumes/** – Persistent storage for the database.

## Responsibilities

- **Backend**: expose REST endpoints, handle persistence, logging and data seeding.
- **Frontend**: fetch product data from the backend and render it using JSF.
- **Database**: PostgreSQL 13 used by the backend via Spring Data JPA.

## Development Flow

1. `docker-compose up --build` – builds images and starts all containers.
2. Backend starts, loads sample data via `DataLoader` and exposes API on port 8080.
3. Frontend fetches JSON from the backend using Jackson and displays products.

Unit and integration tests reside under `backend/src/test` and can be executed with Maven.
