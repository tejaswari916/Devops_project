package com.inventory.app.config;

import com.inventory.app.entity.Product;
import com.inventory.app.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;

/**
 * Seeds sample products into the database on dev startup.
 */
@Configuration
@Profile("dev")
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner seedData(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() > 0) {
                log.info("Database already contains data. Skipping seed.");
                return;
            }
            log.info("Seeding sample data for development...");

            createProduct(productRepository, "Wireless Mouse", "MOUSE-001", "Electronics",
                    "Ergonomic wireless mouse with USB receiver", new BigDecimal("29.99"), 45, 10);
            createProduct(productRepository, "Mechanical Keyboard", "KB-002", "Electronics",
                    "Full-size mechanical keyboard with RGB backlighting", new BigDecimal("89.99"), 8, 15);
            createProduct(productRepository, "USB-C Hub", "HUB-003", "Electronics",
                    "7-in-1 USB-C hub with HDMI, USB 3.0, and SD card reader", new BigDecimal("49.99"), 0, 5);
            createProduct(productRepository, "Office Chair", "CHAIR-004", "Furniture",
                    "Adjustable ergonomic office chair", new BigDecimal("299.99"), 12, 5);
            createProduct(productRepository, "Standing Desk", "DESK-005", "Furniture",
                    "Height-adjustable standing desk 120x60cm", new BigDecimal("599.99"), 3, 5);
            createProduct(productRepository, "A4 Paper Ream", "PAPER-006", "Stationery",
                    "500 sheets A4 80gsm printing paper", new BigDecimal("8.99"), 200, 50);
            createProduct(productRepository, "Ballpoint Pens (Box)", "PEN-007", "Stationery",
                    "Box of 50 blue ballpoint pens", new BigDecimal("12.50"), 4, 10);
            createProduct(productRepository, "Laptop Stand", "STAND-008", "Electronics",
                    "Adjustable aluminium laptop stand", new BigDecimal("35.00"), 22, 8);

            log.info("Seeded {} products.", productRepository.count());
        };
    }

    private void createProduct(ProductRepository repo, String name, String sku,
                                String category, String desc,
                                BigDecimal price, int qty, int minThreshold) {
        Product p = new Product();
        p.setName(name);
        p.setSku(sku);
        p.setCategory(category);
        p.setDescription(desc);
        p.setPrice(price);
        p.setQuantity(qty);
        p.setMinStockThreshold(minThreshold);
        repo.save(p);
    }
}
