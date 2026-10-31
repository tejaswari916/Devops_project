package com.inventory.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Inventory Management System.
 *
 * <p>This application demonstrates a full DevOps CI/CD pipeline with:
 * - Spring Boot 3.3.5 backend
 * - Thymeleaf frontend
 * - MySQL (prod) / H2 (dev) database
 * - Flyway database migrations
 * - Spring Security authentication
 * - Docker containerization
 * - Kubernetes orchestration
 * - Jenkins CI/CD automation
 */
@SpringBootApplication
public class InventoryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryManagementApplication.class, args);
    }
}
