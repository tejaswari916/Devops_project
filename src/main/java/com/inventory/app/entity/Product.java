package com.inventory.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Product entity representing an item in the inventory.
 */
@Entity
@Table(name = "products",
        indexes = {
                @Index(name = "idx_product_sku", columnList = "sku", unique = true),
                @Index(name = "idx_product_category", columnList = "category")
        })
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "SKU is required")
    @Size(max = 100, message = "SKU must not exceed 100 characters")
    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Size(max = 100, message = "Category must not exceed 100 characters")
    @Column(name = "category")
    private String category;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be zero or greater")
    @Digits(integer = 10, fraction = 2, message = "Price must have at most 10 integer digits and 2 decimal places")
    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be zero or greater")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Min(value = 0, message = "Minimum stock threshold must be zero or greater")
    @Column(name = "min_stock_threshold", nullable = false)
    private Integer minStockThreshold = 10;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ── Constructors ──────────────────────────────────────────────────────────

    public Product() {}

    // ── Getters and Setters ───────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getMinStockThreshold() { return minStockThreshold; }
    public void setMinStockThreshold(Integer minStockThreshold) { this.minStockThreshold = minStockThreshold; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // ── Business logic helpers ────────────────────────────────────────────────

    public boolean isLowStock() {
        return this.quantity > 0 && this.quantity <= this.minStockThreshold;
    }

    public boolean isOutOfStock() {
        return this.quantity == 0;
    }

    public String getStockStatus() {
        if (isOutOfStock()) return "OUT_OF_STOCK";
        if (isLowStock()) return "LOW_STOCK";
        return "IN_STOCK";
    }
}
