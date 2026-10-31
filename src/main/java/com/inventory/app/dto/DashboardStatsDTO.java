package com.inventory.app.dto;

import com.inventory.app.entity.Product;
import com.inventory.app.entity.StockTransaction;

import java.util.List;

/**
 * Contains all statistics displayed on the admin dashboard.
 */
public class DashboardStatsDTO {

    private long totalProducts;
    private long totalStockUnits;
    private long lowStockCount;
    private long outOfStockCount;
    private List<Product> recentProducts;
    private List<StockTransaction> recentTransactions;
    private List<Product> lowStockProducts;

    // ── Getters and Setters ───────────────────────────────────────────────────

    public long getTotalProducts() { return totalProducts; }
    public void setTotalProducts(long totalProducts) { this.totalProducts = totalProducts; }

    public long getTotalStockUnits() { return totalStockUnits; }
    public void setTotalStockUnits(long totalStockUnits) { this.totalStockUnits = totalStockUnits; }

    public long getLowStockCount() { return lowStockCount; }
    public void setLowStockCount(long lowStockCount) { this.lowStockCount = lowStockCount; }

    public long getOutOfStockCount() { return outOfStockCount; }
    public void setOutOfStockCount(long outOfStockCount) { this.outOfStockCount = outOfStockCount; }

    public List<Product> getRecentProducts() { return recentProducts; }
    public void setRecentProducts(List<Product> recentProducts) { this.recentProducts = recentProducts; }

    public List<StockTransaction> getRecentTransactions() { return recentTransactions; }
    public void setRecentTransactions(List<StockTransaction> recentTransactions) { this.recentTransactions = recentTransactions; }

    public List<Product> getLowStockProducts() { return lowStockProducts; }
    public void setLowStockProducts(List<Product> lowStockProducts) { this.lowStockProducts = lowStockProducts; }
}
