# 🚀 Project Enhancement Prompt for Generative AI

## 📋 Project Context
You are working with a **containerized JSF + Spring Boot + PostgreSQL application** that currently provides basic product management functionality. The application is fully operational with:

- **Backend**: Spring Boot REST API with CRUD operations for products
- **Frontend**: JSF web interface displaying products in a table
- **Database**: PostgreSQL with sample product data
- **Deployment**: Docker Compose orchestration

## 🎯 Enhancement Request

**Please generate code and configurations to implement the following missing features:**

### 1. **🔐 Security & Authentication**
- **Spring Security** configuration with JWT authentication
- **User management system** with roles (ADMIN, USER)
- **Login/logout functionality** in JSF frontend
- **Protected API endpoints** with role-based access control
- **Password encryption** and secure user registration

### 2. **🧪 Testing Infrastructure**
- **Unit tests** for all service classes and controllers
- **Integration tests** for API endpoints
- **Repository tests** with TestContainers for PostgreSQL
- **JSF frontend tests** with Selenium WebDriver
- **Test coverage** reporting with JaCoCo

### 3. **📚 API Documentation**
- **OpenAPI/Swagger** integration for REST API documentation
- **Interactive API explorer** accessible via web interface
- **API versioning** strategy and implementation
- **Request/response examples** and schema definitions

### 4. **⚡ Performance & Monitoring**
- **Caching layer** with Redis for frequently accessed data
- **Application metrics** with Micrometer and Prometheus
- **Health checks** and monitoring endpoints
- **Database connection pooling** optimization
- **Request/response logging** and performance monitoring

### 5. **🎨 Enhanced User Interface**
- **Modern responsive design** with Bootstrap or similar framework
- **Product creation/editing forms** with validation
- **Search and filtering** functionality
- **Pagination** for large product lists
- **File upload** for product images
- **Real-time updates** with WebSocket support

### 6. **🔧 DevOps & Production Readiness**
- **CI/CD pipeline** configuration (GitHub Actions)
- **Multi-stage Docker builds** for optimization
- **Environment-specific configurations** (dev, staging, prod)
- **Database migrations** with Flyway or Liquibase
- **Logging configuration** with structured logging (JSON format)
- **SSL/TLS configuration** for production deployment

### 7. **🌐 Advanced Features**
- **Product categories management** with hierarchical structure
- **Inventory tracking** with stock levels
- **Order management system** with basic shopping cart
- **Email notifications** for order confirmations
- **Export/import functionality** for products (CSV, Excel)
- **Multi-language support** (i18n) for the frontend

## 📝 Implementation Guidelines

### **Code Quality Requirements:**
- Use **Spring Boot best practices** and conventions
- Implement **proper error handling** with custom exceptions
- Add **comprehensive validation** for all inputs
- Follow **REST API design principles**
- Use **dependency injection** and **loose coupling**

### **Security Requirements:**
- Implement **OWASP security practices**
- Add **CSRF protection** for forms
- Use **secure headers** and **content security policy**
- Implement **rate limiting** for API endpoints

### **Testing Requirements:**
- Achieve **minimum 80% test coverage**
- Include **both positive and negative test cases**
- Use **test data builders** and **fixtures**
- Implement **contract testing** between frontend and backend

### **Documentation Requirements:**
- Update **README.md** with new features and setup instructions
- Add **architecture diagrams** and **API documentation**
- Include **troubleshooting guide** and **FAQ section**
- Document **deployment procedures** and **configuration options**

## 📂 Current Project Structure
```
containerized-jsf-springboot-postgresql-app/
├── backend/
│   ├── src/main/java/com/example/backend/
│   │   ├── Product.java (JPA entity)
│   │   ├── ProductController.java (REST controller)
│   │   ├── ProductService.java (business logic)
│   │   ├── ProductRepository.java (data access)
│   │   └── DataLoader.java (sample data)
│   └── pom.xml
├── frontend/
│   ├── src/main/webapp/
│   │   ├── index.xhtml (product list view)
│   │   └── WEB-INF/
│   └── pom.xml
├── docker-compose.yml
└── README.md
```

## 🎯 Expected Deliverables
1. **Complete source code** for all requested features
2. **Updated Docker configurations** and docker-compose.yml
3. **Database migration scripts** for new tables/columns
4. **Updated documentation** with setup and usage instructions
5. **Test files** with comprehensive coverage
6. **Configuration files** for monitoring and deployment
7. **Updated README** with new features and API documentation

## 🚨 Important Notes
- Maintain **backward compatibility** with existing functionality
- Ensure all new features are **properly containerized**
- Follow **microservices principles** where applicable
- Implement **graceful degradation** for optional features
- Consider **scalability** and **performance** implications

**Generate a comprehensive implementation plan with code examples, configuration files, and step-by-step instructions for integrating these enhancements into the existing project.**
