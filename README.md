# Inventory Management System

> **DevOps Case Study**: End-to-End CI/CD Pipeline using Jenkins, Maven, Docker, and Kubernetes

[![Java](https://img.shields.io/badge/Java-21-orange)](https://adoptium.net)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9.9-blue)](https://maven.apache.org)

---

## Quick Start (No MySQL / Docker Required)

```powershell
# Windows PowerShell - uses H2 in-memory database
.\mvnw.cmd spring-boot:run

# Open browser at:
# http://localhost:8080
# Login: admin / admin123
```

---

## Technology Stack

| Layer | Technology |
|-------|------------|
| Language | Java 21 |
| Framework | Spring Boot 3.3.5 |
| Frontend | Thymeleaf, HTML5, CSS3, JavaScript |
| Database (Dev) | H2 In-Memory |
| Database (Prod) | MySQL 8.0 |
| ORM | Spring Data JPA + Hibernate |
| Security | Spring Security 6 + BCrypt |
| Migrations | Flyway |
| Build | Apache Maven 3.9.9 (via wrapper) |
| CI/CD | Jenkins (Declarative Pipeline) |
| Containers | Docker + Docker Compose |
| Orchestration | Kubernetes (Minikube for local) |
| Testing | JUnit 5 + Mockito + Spring Boot Test |

---

## Project Structure

```
inventory-management/
├── src/
│   ├── main/
│   │   ├── java/com/inventory/app/
│   │   │   ├── config/          # Security, Login controller, Data seeder
│   │   │   ├── controller/      # HTTP layer (Dashboard, Products, Stock)
│   │   │   ├── dto/             # Data Transfer Objects with validation
│   │   │   ├── entity/          # JPA entities (Product, StockTransaction)
│   │   │   ├── exception/       # Custom exceptions + global handler
│   │   │   ├── repository/      # Spring Data JPA repositories
│   │   │   └── service/         # Business logic layer
│   │   └── resources/
│   │       ├── db/migration/    # Flyway SQL migrations (V1, V2)
│   │       ├── static/          # CSS and JavaScript
│   │       ├── templates/       # Thymeleaf HTML templates
│   │       └── application*.properties
│   └── test/                    # JUnit + Mockito tests
├── k8s/                         # Kubernetes manifests
├── Dockerfile                   # Multi-stage Docker build
├── docker-compose.yml           # Local multi-container setup
├── Jenkinsfile                  # Declarative CI/CD pipeline
├── mvnw.cmd                     # Maven Wrapper (Windows)
└── mvnw                         # Maven Wrapper (Linux/Docker)
```

---

## Running the Application

### Option 1: Development Mode (H2 Database - No MySQL needed)

```powershell
# In PowerShell from the project root
.\mvnw.cmd spring-boot:run
```

Open: http://localhost:8080 | Login: `admin` / `admin123`

H2 Console (dev only): http://localhost:8080/h2-console

### Option 2: Docker Compose (MySQL + App)

```bash
# Copy and configure environment
copy .env.example .env
# Edit .env with your passwords

docker compose up -d
```

### Option 3: Build JAR

```powershell
.\mvnw.cmd clean package -DskipTests
java -jar target\inventory-management-1.0.0.jar
```

---

## Running Tests

```powershell
# Run all tests
.\mvnw.cmd clean test

# Tests run against H2 in-memory database (no MySQL needed)
# Results in: target/surefire-reports/
```

---

## Kubernetes Deployment (Minikube)

```powershell
# Start Minikube
minikube start

# Apply all manifests
kubectl apply -f k8s\namespace.yaml
kubectl apply -f k8s\configmap.yaml
kubectl apply -f k8s\secret.yaml
kubectl apply -f k8s\mysql-pvc.yaml
kubectl apply -f k8s\mysql-deployment.yaml
kubectl apply -f k8s\mysql-service.yaml
kubectl apply -f k8s\app-deployment.yaml
kubectl apply -f k8s\app-service.yaml

# Check status
kubectl get pods -n inventory-system
kubectl get services -n inventory-system

# Access the app
minikube service inventory-app-service -n inventory-system
```

---

## Default Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin` | `admin123` |

> **Security Note**: Change the default password using the `ADMIN_PASSWORD` environment variable in production.

---

## Features

- **Dashboard**: Real-time statistics (total products, stock, low-stock alerts)
- **Product CRUD**: Create, read, update, delete with full validation
- **SKU Uniqueness**: Enforced at both application and database level
- **Stock Management**: Record stock-in and stock-out transactions
- **Low Stock Alerts**: Configurable per-product thresholds
- **Transaction History**: Complete audit trail with pagination
- **Search & Filter**: By name, SKU, category, stock status
- **Sortable Tables**: Click column headers to sort
- **Responsive UI**: Works on desktop and tablet
