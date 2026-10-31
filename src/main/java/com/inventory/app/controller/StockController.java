package com.inventory.app.controller;

import com.inventory.app.dto.StockTransactionDTO;
import com.inventory.app.entity.Product;
import com.inventory.app.service.ProductService;
import com.inventory.app.service.StockService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;
    private final ProductService productService;

    public StockController(StockService stockService, ProductService productService) {
        this.stockService = stockService;
        this.productService = productService;
    }

    @GetMapping
    public String manage(
            @RequestParam(required = false) Long productId,
            Model model) {
        model.addAttribute("txDTO", new StockTransactionDTO());
        model.addAttribute("products", productService.searchProducts("", "", "",
                PageRequest.of(0, 1000, Sort.by("name"))).getContent());
        if (productId != null) {
            model.addAttribute("selectedProduct", productService.findById(productId));
        }
        model.addAttribute("activePage", "stock");
        return "stock/manage";
    }

    @PostMapping("/in")
    public String stockIn(@RequestParam Long productId,
                          @Valid @ModelAttribute("txDTO") StockTransactionDTO dto,
                          BindingResult result,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("products", productService.searchProducts("", "", "",
                    PageRequest.of(0, 1000, Sort.by("name"))).getContent());
            model.addAttribute("activePage", "stock");
            return "stock/manage";
        }
        Product product = productService.findById(productId);
        stockService.stockIn(productId, dto);
        redirectAttributes.addFlashAttribute("successMessage",
                "Stock received: " + dto.getQuantity() + " units added to '" + product.getName() + "'.");
        return "redirect:/stock";
    }

    @PostMapping("/out")
    public String stockOut(@RequestParam Long productId,
                           @Valid @ModelAttribute("txDTO") StockTransactionDTO dto,
                           BindingResult result,
                           RedirectAttributes redirectAttributes,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("products", productService.searchProducts("", "", "",
                    PageRequest.of(0, 1000, Sort.by("name"))).getContent());
            model.addAttribute("activePage", "stock");
            return "stock/manage";
        }
        Product product = productService.findById(productId);
        stockService.stockOut(productId, dto);
        redirectAttributes.addFlashAttribute("successMessage",
                "Stock issued: " + dto.getQuantity() + " units removed from '" + product.getName() + "'.");
        return "redirect:/stock";
    }

    @GetMapping("/history")
    public String history(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            Model model) {
        model.addAttribute("transactions",
                stockService.findAll(PageRequest.of(page, size,
                        Sort.by(Sort.Direction.DESC, "transactionDate"))));
        model.addAttribute("activePage", "stock");
        return "stock/transactions";
    }
}
