package com.inventory.app.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Data Transfer Object for creating and editing products.
 * Keeps validation logic separate from the entity.
 */
public class ProductDTO {

    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    private String name;

    @NotBlank(message = "SKU is required")
    @Size(max = 100, message = "SKU must not exceed 100 characters")
    private String sku;

    @Size(max = 100)
    private String category;

    @Size(max = 1000)
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be zero or greater")
    @Digits(integer = 10, fraction = 2, message = "Invalid price format")
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @Min(value = 0, message = "Minimum stock threshold cannot be negative")
    private Integer minStockThreshold = 10;

    // ── Getters and Setters ───────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name != null ? name.trim() : null; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku != null ? sku.trim().toUpperCase() : null; }

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
}
