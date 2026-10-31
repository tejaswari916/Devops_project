package com.inventory.app.entity;

/**
 * Represents the direction of a stock movement.
 */
public enum TransactionType {
    STOCK_IN("Stock In", "Inventory received"),
    STOCK_OUT("Stock Out", "Inventory issued / sold");

    private final String displayName;
    private final String description;

    TransactionType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
}
