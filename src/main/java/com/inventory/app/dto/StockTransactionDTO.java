package com.inventory.app.dto;

import jakarta.validation.constraints.*;

/**
 * Data Transfer Object for stock-in and stock-out operations.
 */
public class StockTransactionDTO {

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @Size(max = 500, message = "Remarks must not exceed 500 characters")
    private String remarks;

    private Long productId;

    // ── Getters and Setters ───────────────────────────────────────────────────

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
}
