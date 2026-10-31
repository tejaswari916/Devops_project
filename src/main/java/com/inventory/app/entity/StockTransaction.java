package com.inventory.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * Records every stock movement (incoming or outgoing) for audit and history.
 */
@Entity
@Table(name = "stock_transactions",
        indexes = {
                @Index(name = "idx_transaction_product", columnList = "product_id"),
                @Index(name = "idx_transaction_date", columnList = "transaction_date")
        })
public class StockTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Transaction type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 20)
    private TransactionType transactionType;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Transaction quantity must be at least 1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Size(max = 500, message = "Remarks must not exceed 500 characters")
    @Column(name = "remarks")
    private String remarks;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "created_by", length = 100)
    private String createdBy = "admin";

    @PrePersist
    protected void onCreate() {
        this.transactionDate = LocalDateTime.now();
    }

    // ── Constructors ──────────────────────────────────────────────────────────

    public StockTransaction() {}

    public StockTransaction(Product product, TransactionType transactionType, Integer quantity, String remarks) {
        this.product = product;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.remarks = remarks;
    }

    // ── Getters and Setters ───────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}
