package com.inventory.app.controller;

import com.inventory.app.dto.DashboardStatsDTO;
import com.inventory.app.service.ProductService;
import com.inventory.app.service.StockService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final ProductService productService;
    private final StockService stockService;

    public DashboardController(ProductService productService, StockService stockService) {
        this.productService = productService;
        this.stockService = stockService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setTotalProducts(productService.countTotal());
        stats.setTotalStockUnits(productService.sumTotalQuantity());
        stats.setLowStockCount(productService.countLowStock());
        stats.setOutOfStockCount(productService.countOutOfStock());
        stats.setRecentProducts(productService.findRecentProducts());
        stats.setRecentTransactions(stockService.findRecentTransactions());
        stats.setLowStockProducts(productService.findLowStockProducts());

        model.addAttribute("stats", stats);
        model.addAttribute("activePage", "dashboard");
        return "dashboard";
    }
}
